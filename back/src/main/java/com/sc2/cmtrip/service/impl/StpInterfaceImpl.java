package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.sc2.cmtrip.entity.CtPermission;
import com.sc2.cmtrip.entity.CtRole;
import com.sc2.cmtrip.service.CtPermissionService;
import com.sc2.cmtrip.service.CtRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the StpInterface interface to leverage the Sa-Token framework for custom role and permission code functionality
 * 该类实现了StpInterface接口，目的是借用sa-token框架实现自定义角色和权限码的功能
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private CtRoleService ctRoleService;

    @Autowired
    private CtPermissionService ctPermissionService;

    /**
     * Return the set of permission codes owned by an account.
     * 返回一个账号所拥有的权限码集合
     * @param loginId
     * @param loginType
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.parseLong((String) loginId);
        // If the user is a superAdmin, assign all permissions 如果是superAdmin，则赋予所有权限
        List<CtRole> roleList = ctRoleService.getRolesByUserId(userId);
        if (!roleList.isEmpty()) {
            List<String> roleNameList = roleList.stream().map(CtRole::getRoleName).collect(Collectors.toList());
            if (!roleNameList.isEmpty()) {
                if (roleNameList.contains("superAdmin")) {
                    // Retrieve all permissions 调取所有权限
                    List<CtPermission> allPermissionList = ctPermissionService.list();
                    if (!allPermissionList.isEmpty()) {
                        List<String> allPermissionNameList = allPermissionList.stream().map(CtPermission::getPermissionName).collect(Collectors.toList());
                        return allPermissionNameList;
                    }
                }
            }
        }
        // If not superAdmin, assign permissions as usual 如果不是superAdmin，照常赋予权限
        List<CtPermission> permissionList = ctPermissionService.findPermissionsByUserId(userId);
        if (!permissionList.isEmpty()) {
            List<String> permissionNameList = permissionList.stream().map(CtPermission::getPermissionName).collect(Collectors.toList());
            return permissionNameList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Return the set of role identifiers owned by an account (permissions and roles can be checked separately)
     * 返回一个账号拥有的角色标识集合（权限与角色可分开校验）
     * @param loginId
     * @param loginType
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = null;
        if (loginId instanceof Long) {
            userId = (Long) loginId;
        } else if (loginId instanceof String) {
            userId = Long.parseLong((String) loginId);
        }
        List<CtRole> roleList = ctRoleService.getRolesByUserId(userId);
        if (roleList != null) {
            List<String> roleNameList = roleList.stream()
                    .map(CtRole::getRoleName).collect(Collectors.toList());
            return roleNameList;
        } else {
            return null;
        }
    }

}
