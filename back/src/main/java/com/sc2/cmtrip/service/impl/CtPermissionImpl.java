package com.sc2.cmtrip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.entity.CtPermission;
import com.sc2.cmtrip.entity.CtRolePermission;
import com.sc2.cmtrip.mapper.CtPermissionMapper;
import com.sc2.cmtrip.mapper.CtRolePermissionMapper;
import com.sc2.cmtrip.mapper.CtUserRoleMapper;
import com.sc2.cmtrip.service.CtPermissionService;
import com.sc2.cmtrip.service.CtRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CtPermissionImpl extends ServiceImpl<CtPermissionMapper, CtPermission> implements CtPermissionService{

    @Autowired
    private CtPermissionMapper ctPermissionMapper;

    @Autowired
    private CtUserRoleMapper ctUserRoleMapper;

    @Autowired
    private CtRolePermissionService ctRolePermissionService;

    @Autowired
    private CtRolePermissionMapper ctRolePermissionMapper;

    /**
     * Return the list of all permission entities.
     * 返回所有permission实体列表
     * @return
     */
    @Override
    public List<CtPermission> getPermissionList() {
        List<CtPermission> allPermissions = this.list();
        if (!allPermissions.isEmpty()) {
            return allPermissions;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Add a permission
     * 新增一个权限
     * @param ctPermission
     */
    @Override
    public void addPermission(CtPermission ctPermission) {
        // Check for duplicate names of the permission 查验重名情况
        if (this.isPermissionNameUnique(ctPermission.getPermissionName())) {
            this.save(ctPermission);
        } else {
            throw new RuntimeException("Name of permission already existed!");
        }
    }

    /**
     * Update a permission
     * 更新一个权限
     * @param ctPermission
     */
    @Override
    public void editPermission(CtPermission ctPermission) {
        // Check for duplicate names in the modified form 对修改后的表单查验重名情况
        if (this.isPermissionNameUnique(ctPermission.getPermissionName())) {
            this.updateById(ctPermission);
        } else {
            throw new RuntimeException("Update failed because the new name existed!");
        }
    }

    /**
     * Delete a permission
     * 删除一个权限
     * @param permissionId
     */
    @Override
    public void deletePermission(Long permissionId) {
        // Check whether the permission exists 判断该permission是否存在
        LambdaQueryWrapper<CtPermission> lcp = new LambdaQueryWrapper<>();
        lcp.eq(CtPermission::getId, permissionId);
        if (this.count(lcp) > 0) {
            // Check whether the permission is bound to any role; if so, deletion is not allowed
            // 判断是否绑定了角色，如果是，则不允许删除
            if (!ctRolePermissionService.isPermissionWithRoles(permissionId)) {
                this.remove(lcp);
            } else {
                throw new RuntimeException("The permission has been disputed to at least one user, cannot be removed!");
            }
        } else {
            throw new RuntimeException("This permission does not exist!");
        }
    }

    /**
     * Directly retrieve the list of permissions owned by the user based on their userId
     * 根据用户id，一步到位，直接获取其所拥有的权限列表
     * @param userId
     * @return
     */
    @Override
    public List<CtPermission> findPermissionsByUserId(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        // Query the list of role IDs assigned to the user based on their user ID
        // 根据用户id查询用户拥有的角色id列表
        List<Long> roleIds = ctUserRoleMapper.findRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            // If the user has no roles, return an empty list of permissions
            // 用户没有角色，返回空权限列表
            return new ArrayList<>();
        }
        // Query the list of permission IDs owned by the roles based on the list of role IDs
        // 根据角色id列表查询角色拥有的权限id列表
        List<Long> permissionIds = ctRolePermissionMapper.findPermissionIdsByRoleIds(roleIds);
        if (permissionIds == null || permissionIds.isEmpty()) {
            // If the user's roles have no permissions, return an empty list of permissions
            // 用户拥有的角色没有权限，返回空权限列表
            return new ArrayList<>();
        }
        // Query detailed permission information based on the list of permission IDs
        // 根据权限id列表查询具体的权限信息
        List<CtPermission> permissions = ctPermissionMapper.findPermissionsByIds(permissionIds);
        return permissions;
    }

    /**
     * Retrieve the list of permissions owned by the specified roleId
     * 根据roleId获取其拥有权限列表
     * @param roleId
     * @return
     */
    @Override
    public List<CtPermission> getPermissionsByRoleId(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<CtRolePermission> lrp = new LambdaQueryWrapper<>();
        lrp.eq(CtRolePermission::getRoleId, roleId);
        if (ctRolePermissionMapper.selectCount(lrp) > 0) {
            List<CtRolePermission> crpList = ctRolePermissionMapper.selectList(lrp);
            List<Long> permissionIds = crpList.stream().map(CtRolePermission::getPermissionId)
                    .collect(Collectors.toList());
            LambdaQueryWrapper<CtPermission> lcp = new LambdaQueryWrapper<>();
            lcp.in(CtPermission::getId, permissionIds);
            if (ctPermissionMapper.selectCount(lcp) > 0) {
                List<CtPermission> ctPermissionList = ctPermissionMapper.selectList(lcp);
                return ctPermissionList;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Check whether the permission name is unique; return true only if it is unique to proceed
     * 判断permission名称是否是独一无二的，返回true才可以继续
     * @param permissionName
     * @return
     */
    private boolean isPermissionNameUnique(String permissionName) {
        LambdaQueryWrapper<CtPermission> lcp = new LambdaQueryWrapper<>();
        lcp.eq(CtPermission::getPermissionName, permissionName);
        if (this.count(lcp) > 0) {
            return false;
        } else {
            return true;
        }
    }

}
