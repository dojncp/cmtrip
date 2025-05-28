package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtPermission;

import java.util.List;

public interface CtPermissionService extends IService<CtPermission> {
    List<CtPermission> getPermissionList();

    void addPermission(CtPermission ctPermission);

    void editPermission(CtPermission ctPermission);

    void deletePermission(Long permissionId);

    List<CtPermission> findPermissionsByUserId(Long userid);

    List<CtPermission> getPermissionsByRoleId(Long roleId);
}
