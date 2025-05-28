package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.entity.CtUserRole;
import com.sc2.cmtrip.mapper.CtUserRoleMapper;
import com.sc2.cmtrip.service.CtUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CtUserRoleServiceImpl extends ServiceImpl<CtUserRoleMapper, CtUserRole> implements CtUserRoleService {

    /**
     * Check whether a specific role has been assigned to any user
     * 查找某个角色是否被分配给用户使用
     * @param roleId
     * @return
     */
    @Override
    public boolean isRoleWithUsers(Long roleId) {
        LambdaQueryWrapper<CtUserRole> lcur = new LambdaQueryWrapper<>();
        lcur.eq(CtUserRole::getRoleId, roleId);
        if (this.count(lcur) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Batch add user-role relationships
     * 批量添加user-role关系
     * @param userId
     * @param roleIds
     */
    @Override
    public void addRolesOfUserByRoleIds(Long userId, List<Long> roleIds) {
        // Handle the case where there may be nothing to add; perform the operation only if roleIds is not empty
        // 适应可能没有需要添加的情况，仅在roleIds为非空时进行操作
        if (!roleIds.isEmpty()) {
            List<CtUserRole> relations = new ArrayList<>();
            for(Long roleId : roleIds) {
                CtUserRole relation = new CtUserRole();
                relation.setUserId(userId);
                relation.setRoleId(roleId);
                relation.setCreateTime(LocalDateTime.now());
                relation.setCreateBy((String) StpUtil.getLoginId());
                relations.add(relation);
            }
            // Batch add 批量添加
            this.saveBatch(relations);
        }
    }

    /**
     * Batch clear user-role relationships
     * 批量清除user-role关系
     * @param userId
     * @param roleIds
     */
    @Override
    public void deleteRolesOfUserByRoleIds(Long userId, List<Long> roleIds) {
        // Handle the case where there may be nothing to delete; perform the operation only if roleIds is not empty
        // 适应可能没有需要删除的情况，仅在roleIds为非空时进行操作
        if (!roleIds.isEmpty()) {
            LambdaQueryWrapper<CtUserRole> lcur = new LambdaQueryWrapper<>();
            lcur.eq(CtUserRole::getUserId, userId)
                    .in(CtUserRole::getRoleId, roleIds);
            this.remove(lcur);
        }
    }

    /**
     * Update multiple roles for a user (with additions and removals)
     * 更新用户的多个角色（有增有减）
     * @param userId
     * @param addRoleIds
     * @param deleteRoleIds
     */
    @Transactional
    @Override
    public void updateRolesOfUserByRoleIds(Long userId, List<Long> addRoleIds, List<Long> deleteRoleIds) {
        addRolesOfUserByRoleIds(userId, addRoleIds);
        deleteRolesOfUserByRoleIds(userId, deleteRoleIds);
    }

    /**
     * Batch delete all role relationships of a specific user
     * 批量删除某个用户的所有角色关系
     * @param userId
     */
    @Override
    public void deleteRolesOfUserByUserId(Long userId) {
        LambdaQueryWrapper<CtUserRole> lcur = new LambdaQueryWrapper<>();
        lcur.eq(CtUserRole::getUserId, userId);
        if (this.count(lcur) > 0) {
            this.remove(lcur);
        }
    }

}
