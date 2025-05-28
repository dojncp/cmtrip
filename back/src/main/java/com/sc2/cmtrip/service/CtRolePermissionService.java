package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtRolePermission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CtRolePermissionService extends IService<CtRolePermission> {
    boolean isPermissionWithRoles(Long permissionId);

    void addPermissionsOfRoleByPermissionIds(Long roleId, List<Long> permissionIds);

    void deletePermissionsOfRoleByRoleId(Long roleId);

    void deletePermissionsOfRoleByRoleId(Long roleId, List<Long> permissionIds);

    @Transactional
    void updatePermissionsOfRoleByPermissionIds(Long roleId,
                                                List<Long> addPermissionIds,
                                                List<Long> deletePermissionIds);
}
