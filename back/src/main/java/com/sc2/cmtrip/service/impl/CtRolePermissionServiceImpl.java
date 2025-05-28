package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.entity.CtRolePermission;
import com.sc2.cmtrip.mapper.CtRolePermissionMapper;
import com.sc2.cmtrip.service.CtRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CtRolePermissionServiceImpl extends ServiceImpl<CtRolePermissionMapper, CtRolePermission> implements CtRolePermissionService {

    /**
     * Check if a specific permission is assigned to any role
     * 查找某个权限是否被分配给角色使用
     * @param permissionId
     * @return
     */
    @Override
    public boolean isPermissionWithRoles(Long permissionId) {
        LambdaQueryWrapper<CtRolePermission> lcrp = new LambdaQueryWrapper<>();
        lcrp.eq(CtRolePermission::getPermissionId, permissionId);
        if (this.count(lcrp) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Batch add role-permission relationships of a role
     * 为某个role批量添加role-permission关系
     * @param roleId
     * @param permissionIds
     */
    @Override
    public void addPermissionsOfRoleByPermissionIds(Long roleId, List<Long> permissionIds) {
        // Handle the case where there may be nothing to add; perform the operation only if permissionIds is not empty
        // 适应可能没有需要添加的情况，仅在permissionIds为非空时进行操作
        if (!permissionIds.isEmpty()) {
            List<CtRolePermission> relations = new ArrayList<>();
            for (Long permissionId: permissionIds) {
                CtRolePermission relation = new CtRolePermission();
                relation.setRoleId(roleId);
                relation.setPermissionId(permissionId);
                relation.setCreateTime(LocalDateTime.now());
                relation.setCreateBy((String) StpUtil.getLoginId());
                relations.add(relation);
            }
            // Batch add 批量添加
            this.saveBatch(relations);
        }
    }

    /**
     * Batch remove role-permission relationships for a specific role
     * 为某个role批量清除role-permission关系
     * @param roleId
     */
    @Override
    public void deletePermissionsOfRoleByRoleId(Long roleId, List<Long> permissionIds) {
        // Handle the case where there may be nothing to add; perform the operation only if permissionIds is not empty
        // 适应可能没有需要删除的情况，仅在permissionIds为非空时进行操作
        if (!permissionIds.isEmpty()) {
            LambdaQueryWrapper<CtRolePermission> lcrp = new LambdaQueryWrapper<>();
            lcrp.eq(CtRolePermission::getRoleId, roleId)
                    .in(CtRolePermission::getPermissionId, permissionIds);
            this.remove(lcrp);
        }
    }

    /**
     * Update multiple permissions for a user (including additions and removals)
     * 更新用户的多个权限（有增有减）
     * @param roleId
     * @param addPermissionIds
     * @param deletePermissionIds
     */
    @Transactional
    @Override
    public void updatePermissionsOfRoleByPermissionIds(Long roleId,
                                                       List<Long> addPermissionIds,
                                                       List<Long> deletePermissionIds) {
        addPermissionsOfRoleByPermissionIds(roleId, addPermissionIds);
        deletePermissionsOfRoleByRoleId(roleId, deletePermissionIds);
    }

    /**
     * Batch delete all permissions of a specific role
     * 批量删除某个角色的所有权限
     * @param roleId
     */
    @Override
    public void deletePermissionsOfRoleByRoleId(Long roleId) {
        LambdaQueryWrapper<CtRolePermission> lcrp = new LambdaQueryWrapper<>();
        lcrp.eq(CtRolePermission::getRoleId, roleId);
        if (this.count(lcrp) > 0) {
            this.remove(lcrp);
        }
    }


}
