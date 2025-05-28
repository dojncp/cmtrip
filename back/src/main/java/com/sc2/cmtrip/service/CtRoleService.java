package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtRole;

import java.util.List;

public interface CtRoleService extends IService<CtRole> {
    List<CtRole> getRoleList();

    List<CtRole> getRoleListBesidesSuperAdmin();

    List<CtRole> getRolesByUserId(Long userId);

    void addRole(CtRole ctRole);

    void editRole(CtRole ctRole);

    void deleteRole(Long roleId);

    void assignPermissionToRole(Long roleId, Long permissionId);

    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    List<String> getRolePermissionNames(Long id);

    void removePermissionsToRole(Long roleId, List<Long> permissionIds);

    void updatePermissionsToRole(Long roleId, List<Long> assignPermissionIds, List<Long> removePermissionIds);
}
