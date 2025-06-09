package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.*;
import com.sc2.cmtrip.mapper.CtUserMapper;
import com.sc2.cmtrip.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Permissions;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CtUserServiceImpl extends ServiceImpl<CtUserMapper, CtUser> implements CtUserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CtUserMapper ctUserMapper;

    @Autowired
    private CtRoleService ctRoleService;

    @Autowired
    private CtPermissionService ctPermissionService;

    @Autowired
    private CtUtils ctUtils;

    @Autowired
    private CtUserRoleService ctUserRoleService;

    @Autowired
    private CtUserTripService ctUserTripService;

    @Autowired
    private CtTripService ctTripService;

    /**
     * Return the user entity based on the username
     * 通过用户名返回用户实体
     * @param userName
     * @return
     */
    @Override
    public CtUser findByUserName(String userName) {
        LambdaQueryWrapper<CtUser> cu = new LambdaQueryWrapper<>();
        cu.eq(CtUser::getUserName, userName);
        if (this.count(cu) > 0) {
            CtUser user = ctUserMapper.selectOne(cu);
            // Ensure case sensitivity 加一重保障，避免大小写不敏感
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Register a new user
     * 注册新用户
     * @param newUser
     */
    @Override
    public void registerUser(CtUser newUser) {
        if (!isUserNameUnique(newUser.getUserName())) {
            throw new RuntimeException("This username already exists!");
        }
        String userName = newUser.getUserName();
        String password = newUser.getPassword();
        // Check whether the username is the disallowed **superAdmin** for registration
        // 检查用户名是否为不准注册的superAdmin
        if (userName.equals("superAdmin")) {
            throw new RuntimeException("This account can not be registered!");
        }
        // Check whether the username already exists 检查用户名是否已经存在
        if (this.findByUserName(userName) != null) {
            throw new RuntimeException("Username already exists!");
        }
        // Generate a salt value 生成盐值
        String salt = UUID.randomUUID().toString();
        // Perform SHA-256 encryption on the password 对密码进行SHA-256加密
        String hashedPassword = ctUtils.getHashedPassword(password, salt);
        // Create a CtUser object and save it to the database 创建CtUser对象并保存到数据库
        newUser.setPassword(hashedPassword); // 改写密码
        newUser.setSalt(salt);
        newUser.setEnabled(true);
        this.save(newUser);
    }

    /**
     * Delete a user
     * 删除用户信息
     * @param id
     */
    @Transactional
    @Override
    public void deleteUser(Long id) {
        CtUser ctUser = ctUserMapper.selectById(id);
        String userName = ctUser.getUserName();
        if (id == 1L || userName.equals("superAdmin")) {
            throw new RuntimeException("superAdmin cannot be deleted!");
        } else {
            // Delete the user itself if the conditions are met 符合条件，删除用户本身
            this.removeById(id);
            // Delete the relationship records between the user and roles 删除用户与角色的关系记录
            ctUserRoleService.deleteRolesOfUserByUserId(id);
            // Delete the user's trip records (if any trip contains actions, the transaction will roll back)
            // 删除用户的旅行记录（如果某个trip下有action，事务则会回滚）
            deleteTripsByUserId(id);
        }
    }

    /**
     * User login
     * 用户登录
     * @param userName
     * @param password
     * @return
     */
    @Override
    public Map<String, Object> doLogin(String userName, String password) {
        // Prepare the redis to record user lock times 建立两个redis的键以管理用户登录失败的锁定状态
        String lockKey = "login:lock:" + userName;
        String failKey = "login:fail" + userName;
        // Check whether the user is locked 检查用户是否被锁定
        Boolean isLocked = redisTemplate.hasKey(lockKey);
        if (Boolean.TRUE.equals(isLocked)) {
            throw new RuntimeException("This account is temporarily locked due to too many failed login attempts. Please try again after 10 minutes.");
        }
        // Check whether there is a userName with the same name in the database based on the given name
        // 根据name查询数据库中是否有同名的userName
        CtUser user = this.findByUserName(userName);
        if (user != null) {
            String salt = user.getSalt();
            String forCheckPwd = ctUtils.getHashedPassword(password, salt);
            String pwd = user.getPassword();
            if (forCheckPwd.equals(pwd)) {
                // Correct password, clear the fail record 密码正确，清除失败记录
                redisTemplate.delete(failKey);
                redisTemplate.delete(lockKey);
                // Execute login 执行登录
                Long userId = user.getId();
                StpUtil.login(userId);
                // Cache current user information 缓存当前用户信息
                StpUtil.getTokenSession().set("loginUser", user);
                // Create return data 构建返回数据
                Map<String, Object> result = new HashMap<>();
                result.put("token", StpUtil.getTokenInfo().tokenValue);
                result.put("roles", StpUtil.getRoleList());
                result.put("permissions", StpUtil.getPermissionList());
                return result;
            } else {
                // Wrong password, record fail times
                Long fails = redisTemplate.opsForValue().increment(failKey);
                if (fails == 1) {
                    // Set valid period as 10 minutes 设置有效期为10分钟
                    redisTemplate.expire(failKey, Duration.ofMinutes(10));
                }
                if (fails >= 5) {
                    // Lock the account 锁定账号
                    redisTemplate.opsForValue().set(lockKey, "LOCKED", Duration.ofMinutes(10));
                    throw new RuntimeException("Too many failed attempts. Account is locked for 10 minutes.");
                } else {
                    throw new RuntimeException("Incorrect password. Attempt " + fails + "/5");
                }
            }
        } else {
            throw new RuntimeException("User does not exist!");
        }
    }

    /**
     * User logout
     * 用户注销登录
     */
    @Override
    public void doLogout() {
        // 强制下线
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        StpUtil.logout(loginId);
    }

    /**
     * Return the list of all users
     * 返回所有用户的列表
     * @return
     */
    @Override
    public List<CtUser> getUserList() {
        LambdaQueryWrapper<CtUser> lc = new LambdaQueryWrapper<>();
        lc.select(CtUser::getId, CtUser::getUserName,
                CtUser::getEmail, CtUser::getPhoneNumber, CtUser::isEnabled);
        return ctUserMapper.selectList(lc);
    }

    /**
     * Get the list of role names for the user
     * 获取用户的角色名列表
     * @param userId
     * @return
     */
    @Override
    public List<String> getRoleNamesByUserId(Long userId) {
        List<CtRole> roles = ctRoleService.getRolesByUserId(userId);
        List<String> roleNames = roles.stream().map(CtRole::getRoleName).collect(Collectors.toList());
        return roleNames;
    }

    /**
     * Grant a single role to the user
     * 为用户授予单个角色
     * @param userId
     * @param roleId
     */
    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        // Check whether it is valid 判断是否合法
        LambdaQueryWrapper<CtUser> lct = new LambdaQueryWrapper<>();
        lct.eq(CtUser::getId, userId);
        LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
        lcr.eq(CtRole::getId, roleId);
        // Valid only when both IDs exist 仅在两种id都存在的情况下合法
        if (this.count(lct) > 0 && ctRoleService.count(lcr) > 0) {
            // Create a relationship table instance and write it to the relationship table
            // 构建关系表实例，写入关系表
            CtUserRole ctUserRole = new CtUserRole();
            ctUserRole.setUserId(userId);
            ctUserRole.setRoleId(roleId);
            ctUserRole.setCreateBy((String) StpUtil.getLoginId());
            ctUserRole.setCreateTime(LocalDateTime.now());
            ctUserRoleService.save(ctUserRole);
        } else {
            throw new RuntimeException("Invalid user id or role id!");
        }
    }

    /**
     * Grant multiple roles to the user (net addition)
     * 为用户授予多个角色（净增加）
     * @param userId
     * @param roleIds
     */
    @Override
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // Perform the operation only when the list of roles to be handled is not empty
        // 仅在待操作角色列表非空时进行
        if (!roleIds.isEmpty()) {
            // Check whether it is valid 判断是否合法
            LambdaQueryWrapper<CtUser> lct = new LambdaQueryWrapper<>();
            lct.eq(CtUser::getId, userId);
            LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
            lcr.in(CtRole::getId, roleIds);
            // Query the actual number of roles existing in the database
            // 查询数据库中实际存在的角色数量
            long count = ctRoleService.count(lcr);
            // It is only valid when both types of IDs exist, and the number of roles in roleIds that actually exist in the database equals the number of roleIds themselves (meaning roleIds ∩ query result = roleIds)
            // 仅在两种id都存在，且roleIds中在数据库中实际存在的数量与其本身数量相等（意味着roleIds ∩ 查询结果 = roleIds）的情况下合法
            if (this.count(lct) > 0 && count == roleIds.size()) {
                // Call the batch processing method of the relationship implementation interface
                // 调用关系实现接口的批量处理方法
                ctUserRoleService.addRolesOfUserByRoleIds(userId, roleIds);
            } else {
                throw new RuntimeException("Invalid user id or role ids, assign failed!");
            }
        }
    }

    /**
     * Remove multiple roles from a user (net reduction)
     * 取消用户的多个角色（净减少）
     * @param userId
     * @param roleIds
     */
    @Override
    public void removeRolesToUser(Long userId, List<Long> roleIds) {
        // Perform the operation only when the list of roles to be handled is not empty
        // 仅在待操作角色列表非空时进行
        if (!roleIds.isEmpty()) {
            // Check whether it is valid 判断是否合法
            LambdaQueryWrapper<CtUser> lct = new LambdaQueryWrapper<>();
            lct.eq(CtUser::getId, userId);
            LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
            lcr.in(CtRole::getId, roleIds);
            // Query the actual number of roles existing in the database
            // 查询数据库中实际存在的角色数量
            long count = ctRoleService.count(lcr);
            // It is only valid when both types of IDs exist, and the number of roles in roleIds that actually exist in the database equals the number of roleIds themselves (meaning roleIds ∩ query result = roleIds)
            // 仅在两种id都存在，且roleIds中在数据库中实际存在的数量与其本身数量相等（意味着roleIds ∩ 查询结果 = roleIds）的情况下合法
            if (this.count(lct) > 0 && count == roleIds.size()) {
                // Call the batch processing method of the relationship implementation interface
                // 调用关系实现接口的批量处理方法
                ctUserRoleService.deleteRolesOfUserByRoleIds(userId, roleIds);
            } else {
                throw new RuntimeException("Invalid user id or role ids, remove failed!");
            }
        }
    }

    /**
     * 更新用户的多个角色（有增有减）
     * @param userId
     * @param assignRoleIds
     * @param removeRoleIds
     */
    @Override
    public void updateRolesToUser(Long userId, List<Long> assignRoleIds, List<Long> removeRoleIds) {
        // Check whether it is valid 判断是否合法
        LambdaQueryWrapper<CtUser> lct = new LambdaQueryWrapper<>();
        lct.eq(CtUser::getId, userId);
        // Only perform .in query when assignRoleIds is not empty
        // assignRoleIds 非空才执行 .in 查询
        long countAdd = 0L;
        if (assignRoleIds != null && !assignRoleIds.isEmpty()) {
            LambdaQueryWrapper<CtRole> lcrAdd = new LambdaQueryWrapper<>();
            lcrAdd.in(CtRole::getId, assignRoleIds);
            countAdd = ctRoleService.count(lcrAdd);
        }
        // Only perform .in query when removeRoleIds is not empty
        // removeRoleIds 非空才执行 .in 查询
        long countDelete = 0L;
        if (removeRoleIds != null && !removeRoleIds.isEmpty()) {
            LambdaQueryWrapper<CtRole> lcrDelete = new LambdaQueryWrapper<>();
            lcrDelete.in(CtRole::getId, removeRoleIds);
            countDelete = ctRoleService.count(lcrDelete);
        }
        // Validation 合法性校验
        boolean userExists = this.count(lct) > 0;
        boolean addValid = countAdd == (assignRoleIds == null ? 0 : assignRoleIds.size());
        boolean deleteValid = countDelete == (removeRoleIds == null ? 0 : removeRoleIds.size());
        if (userExists && addValid && deleteValid) {
            // Execute update 执行更新
            ctUserRoleService.updateRolesOfUserByRoleIds(userId, assignRoleIds, removeRoleIds);
        } else {
            throw new RuntimeException("Invalid user id or role ids, updateFailed!");
        }
    }

    /**
     * Get the list of permission names for the user
     * 获取用户的权限名列表
     * @param userId
     * @return
     */
    @Override
    public List<String> getPermissionNamesByUserId(Long userId) {
        List<CtPermission> permissions = ctPermissionService.findPermissionsByUserId(userId);
        List<String> permissionNames = permissions.stream().map(CtPermission::getPermissionName).collect(Collectors.toList());;
        return permissionNames;
    }

    /**
     * Delete trips of the special user
     * 删除指定用户的所有旅行
     * @param userId
     */
    @Transactional
    @Override
    public void deleteTripsByUserId(Long userId) {
        LambdaQueryWrapper<CtUserTrip> lcut = new LambdaQueryWrapper<>();
        lcut.eq(CtUserTrip::getUserId, userId);
        if (ctUserTripService.count(lcut) > 0) {
            List<Long> tripIds = ctUserTripService.list(lcut).stream().map(CtUserTrip::getTripId).collect(Collectors.toList());
            ctUserTripService.remove(lcut);
            ctTripService.deleteTrips(tripIds);
        }
    }

    /**
     * Check whether the username is unique. If it is, return true; otherwise, return false
     * 判断用户名称是否唯一，若唯一则输出true，否则输出false
     * @param userName
     * @return
     */
    private boolean isUserNameUnique(String userName) {
        LambdaQueryWrapper<CtUser> lct = new LambdaQueryWrapper<>();
        lct.eq(CtUser::getUserName, userName);
        if (this.count(lct) > 0) {
            return false;
        } else {
            return true;
        }
    }

}
