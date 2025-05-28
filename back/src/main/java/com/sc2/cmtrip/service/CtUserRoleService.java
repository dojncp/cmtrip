package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtUserRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CtUserRoleService extends IService<CtUserRole> {
    boolean isRoleWithUsers(Long roleId);

    void addRolesOfUserByRoleIds(Long userId, List<Long> roleIds);

    void deleteRolesOfUserByRoleIds(Long userId, List<Long> roleIds);

    @Transactional
    void updateRolesOfUserByRoleIds(Long userId, List<Long> addRoleIds, List<Long> deleteRoleIds);

    void deleteRolesOfUserByUserId(Long userId);
}
