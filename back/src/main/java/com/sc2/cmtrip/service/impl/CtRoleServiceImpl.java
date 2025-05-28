package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.entity.*;
import com.sc2.cmtrip.mapper.CtRoleMapper;
import com.sc2.cmtrip.mapper.CtUserRoleMapper;
import com.sc2.cmtrip.service.CtPermissionService;
import com.sc2.cmtrip.service.CtRolePermissionService;
import com.sc2.cmtrip.service.CtRoleService;
import com.sc2.cmtrip.service.CtUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CtRoleServiceImpl extends ServiceImpl<CtRoleMapper, CtRole> implements CtRoleService {

    @Autowired
    private CtUserRoleService ctUserRoleService;

    @Autowired
    private CtUserRoleMapper ctUserRoleMapper;

    @Autowired
    private CtRoleMapper ctRoleMapper;

    @Autowired
    private CtPermissionService ctPermissionService;

    @Autowired
    private CtRolePermissionService ctRolePermissionService;

    /**
     * Return the list of all role entities
     * 返回所有role实体列表
     * @return
     */
    @Override
    public List<CtRole> getRoleList() {
        List<CtRole> allRoles = this.list();
        if (!allRoles.isEmpty()) {
            return allRoles;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Query the list of all roles excluding superAdmin
     * 查询不为superAdmin的所有role列表
     * @return
     */
    @Override
    public List<CtRole> getRoleListBesidesSuperAdmin() {
        LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
        lcr.ne(CtRole::getRoleName, "superAdmin");
        List<CtRole> allRolesBesidesSuperAdmin = ctRoleMapper.selectList(lcr);
        return allRolesBesidesSuperAdmin;
    }

    /**
     * Return the list of roles assigned to the user based on their userId.
     * 根据用户id返回其roles
     * @param userId
     * @return
     */
    @Override
    public List<CtRole> getRolesByUserId(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<CtUserRole> lur = new LambdaQueryWrapper<>();
        lur.eq(CtUserRole::getUserId, userId);
        if (ctUserRoleMapper.selectCount(lur) > 0) {
            List<CtUserRole> curList = ctUserRoleMapper.selectList(lur);
            List<Long> roleIds = curList.stream().map(CtUserRole::getRoleId)
                    .collect(Collectors.toList());
            LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
            lcr.in(CtRole::getId, roleIds);
            if (ctRoleMapper.selectCount(lcr) > 0) {
                List<CtRole> ctRoleList = ctRoleMapper.selectList(lcr);
                return ctRoleList;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Add a role
     * 新增一个角色
     * @param ctRole
     */
    @Override
    public void addRole(CtRole ctRole) {
        // Check for whether the duplicate names of role exist 查验重名情况
        if (this.isRoleNameUnique(ctRole.getRoleName())) {
            this.save(ctRole);
        } else {
            throw new RuntimeException("Name of role already existed!");
        }
    }

    /**
     * Update a role
     * 更新一个角色
     * @param ctRole
     */
    @Override
    public void editRole(CtRole ctRole) {
        // Check for duplicate names in the modified form 对修改后的表单查验重名情况
        if (this.isRoleNameUnique(ctRole.getRoleName())) {
            this.updateById(ctRole);
        } else {
            throw new RuntimeException("Update failed because the new name existed!");
        }
    }

    /**
     * Delete a role
     * 删除一个角色
     * @param roleId
     */
    @Override
    public void deleteRole(Long roleId) {
        // Check whether the specified role exists 判断该role是否存在
        LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
        lcr.eq(CtRole::getId, roleId);
        if (this.count(lcr) > 0) {
            // Check whether the role is assigned to any user; if so, deletion is not allowed
            // 判断是否绑定了用户，如果是，则不允许删除
            if (!ctUserRoleService.isRoleWithUsers(roleId)) {
                this.remove(lcr);
            } else {
                throw new RuntimeException("The role has been disputed to at least one user, cannot be removed!");
            }
        } else {
            throw new RuntimeException("This role does not exist!");
        }
    }

    /**
     * Grant a single permission to a role
     * 为角色授予单个权限
     * @param roleId
     * @param permissionId
     */
    @Override
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        // Check whether it is valid 判断是否合法
        LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
        lcr.eq(CtRole::getId, roleId);
        LambdaQueryWrapper<CtPermission> lcp = new LambdaQueryWrapper<>();
        lcp.eq(CtPermission::getId, permissionId);
        // Valid only when both IDs exist 仅在两种id都存在的情况下合法
        if (this.count(lcr) > 0 && ctPermissionService.count(lcp) > 0) {
            // Create a relationship table instance and write it to the relationship table
            // 构建关系表实例，写入关系表
            CtRolePermission ctRolePermission = new CtRolePermission();
            ctRolePermission.setRoleId(roleId);
            ctRolePermission.setPermissionId(permissionId);
            ctRolePermission.setCreateBy((String) StpUtil.getLoginId());
            ctRolePermission.setCreateTime(LocalDateTime.now());
        } else {
            throw new RuntimeException("Invalid role id or permission id!");
        }
    }

    /**
     * Grant multiple permissions to a role (net addition)
     * 为角色授予多个权限（净增加）
     * @param roleId
     * @param permissionIds
     */
    @Override
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // Perform only when the permission list to operate on is not empty
        // 仅在待操作权限列表非空时进行
        if (!permissionIds.isEmpty()) {
            // Check whether it is valid 判断是否合法
            LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
            lcr.eq(CtRole::getId, roleId);
            LambdaQueryWrapper<CtPermission> lcp = new LambdaQueryWrapper<>();
            lcp.in(CtPermission::getId, permissionIds);
            // Query the actual number of permissions existing in the database
            // 查询数据库中实际存在的权限数量
            long count = ctPermissionService.count(lcp);
            // Validation passes if and only if both IDs exist, and permissionIds ∩ query result = permissionIds
            // 当且仅当两种id都存在，且permissionIds ∩ 查询结果 = permissionIds时通过校验
            if (this.count(lcr) > 0 && count == permissionIds.size()) {
                // Call the batch processing method of the relationship implementation interface
                // 调用关系实现接口的批量处理方法
                ctRolePermissionService.addPermissionsOfRoleByPermissionIds(roleId, permissionIds);
            } else {
                throw new RuntimeException("Invalid role id or permission ids, assign failed!");
            }
        }
    }

    /**
     * Revoke multiple permissions from a role (net decrease)
     * 取消角色的多个权限（净减少）
     * @param roleId
     * @param permissionIds
     */
    @Override
    public void removePermissionsToRole(Long roleId, List<Long> permissionIds) {
        // Perform only when the permission list to operate on is not empty
        // 仅在待操作权限列表非空时进行
        if (!permissionIds.isEmpty()) {
            // Check whether it is valid 判断是否合法
            LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
            lcr.eq(CtRole::getId, roleId);
            LambdaQueryWrapper<CtPermission> lcp = new LambdaQueryWrapper<>();
            lcp.in(CtPermission::getId, permissionIds);
            // Query the actual number of permissions existing in the database
            // 查询数据库中实际存在的角色数量
            long count = ctPermissionService.count(lcp);
            // Validation passes if and only if both IDs exist, and permissionIds ∩ query result = permissionIds
            // 仅在两种id都存在，且permissionIds中在数据库中实际存在的数量与其本身数量相等（意味着permissionIds ∩ 查询结果 = permissionIds）的情况下合法
            if (this.count(lcr) > 0 && count == permissionIds.size()) {
                // Call the batch processing method of the relationship implementation interface
                // 调用关系实现接口的批量处理方法
                ctRolePermissionService.deletePermissionsOfRoleByRoleId(roleId, permissionIds);
            } else {
                throw new RuntimeException("Invalid role id or permission ids, remove failed!");
            }
        }
    }

    /**
     * Update multiple permissions of a role (with additions and deletions)
     * 更新角色的多个权限（有增有减）
     * @param roleId
     * @param assignPermissionIds
     * @param removePermissionIds
     */
    @Override
    public void updatePermissionsToRole(Long roleId, List<Long> assignPermissionIds, List<Long> removePermissionIds) {
        // Check whether it is valid 判断是否合法
        LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
        lcr.eq(CtRole::getId, roleId);
        // If assignPermissionIds is empty, then countAdd is 0; otherwise, perform the query
        // 如果 assignPermissionIds 为空，countAdd 为 0；否则才执行查询
        long countAdd = 0L;
        if (assignPermissionIds != null && !assignPermissionIds.isEmpty()) {
            LambdaQueryWrapper<CtPermission> lcpAdd = new LambdaQueryWrapper<>();
            lcpAdd.in(CtPermission::getId, assignPermissionIds);
            countAdd = ctPermissionService.count(lcpAdd);
        }
        // If removePermissionIds is empty, then countDelete is 0; otherwise, perform the query.
        // 如果 removePermissionIds 为空，countDelete 为 0；否则才执行查询
        long countDelete = 0L;
        if (removePermissionIds != null && !removePermissionIds.isEmpty()) {
            LambdaQueryWrapper<CtPermission> lcpDelete = new LambdaQueryWrapper<>();
            lcpDelete.in(CtPermission::getId, removePermissionIds);
            countDelete = ctPermissionService.count(lcpDelete);
        }
        // Check whether the role exists
        // 判断角色是否存在
        boolean roleExists = this.count(lcr) > 0;
        boolean addValid = countAdd == (assignPermissionIds == null ? 0 : assignPermissionIds.size());
        boolean deleteValid = countDelete == (removePermissionIds == null ? 0 : removePermissionIds.size());
        if (roleExists && addValid && deleteValid) {
            // Execute the update operation 执行更新操作
            ctRolePermissionService.updatePermissionsOfRoleByPermissionIds(roleId, assignPermissionIds, removePermissionIds);
        } else {
            throw new RuntimeException("Invalid role id or permission ids, updateFailed!");
        }
    }

    /**
     * Retrieve the list of permission names corresponding to a specific role
     * 获取某个角色对应的权限名列表
     * @param id
     * @return
     */
    @Override
    public List<String> getRolePermissionNames(Long id) {
        List<CtPermission> permissions = ctPermissionService.getPermissionsByRoleId(id);
        List<String> permissionNames = permissions.stream().map(CtPermission::getPermissionName).collect(Collectors.toList());
        return permissionNames;
    }

    /**
     * Check if the role name is unique; return true only if it is unique to proceed
     * 判断role名称是否是独一无二的，返回true才可以继续
     * @param roleName
     * @return
     */
    private boolean isRoleNameUnique(String roleName) {
        LambdaQueryWrapper<CtRole> lcr = new LambdaQueryWrapper<>();
        lcr.eq(CtRole::getRoleName, roleName);
        if (this.count(lcr) > 0) {
            return false;
        } else {
            return true;
        }
    }
}
