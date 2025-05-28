package com.sc2.cmtrip.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sc2.cmtrip.common.ApiResult;
import com.sc2.cmtrip.dto.CtUpdateRolePermissionsDTO;
import com.sc2.cmtrip.entity.CtRole;
import com.sc2.cmtrip.service.CtRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class CtRoleController {

    @Autowired
    private CtRoleService ctRoleService;

    /**
     * Retrieve all role entities
     * 获取所有role实体
     * @return
     */
    @SaCheckPermission("list-all-roles")
    @GetMapping("/list")
    public ApiResult getRoleList() {
        return ApiResult.success(ctRoleService.getRoleList());
    }


    /**
     * Retrieve all roles except `superAdmin`
     * 获取除superAdmin之外的所有role
     * @return
     */
    @SaCheckPermission("list-all-roles-besides-superAdmin")
    @GetMapping("/list-besides-superAdmin")
    public ApiResult getRoleListBesidesSuperAdmin() {
        return ApiResult.success(ctRoleService.getRoleListBesidesSuperAdmin());
    }

    /**
     * Retrieve the list of role entities associated with the given user ID
     * 根据用户id获取其所拥有的角色实体列表
     * @param userId
     * @return
     */
    @SaCheckPermission("get-role-list-by-user-id")
    @GetMapping("/get-list-by-user-id")
    public ApiResult getRolesByUserId(Long userId) {
        return ApiResult.success(ctRoleService.getRolesByUserId(userId));
    }

    /**
     * Add a role
     * 新增一个角色
     * @param ctRole
     */
    @SaCheckPermission("add-role")
    @PostMapping("/add")
    public void addRole(@RequestBody CtRole ctRole) {
        ctRoleService.addRole(ctRole);
    }

    /**
     * Update a role
     * 更新一个角色
     * @param ctRole
     */
    @SaCheckPermission("edit-role")
    @PostMapping("/edit")
    public void editRole(@RequestBody CtRole ctRole) {
        ctRoleService.editRole(ctRole);
    }

    /**
     * Delete a role
     * 删除一个角色
     * @param id
     */
    @SaCheckPermission("delete-role")
    @DeleteMapping("/delete")
    public void deleteRole(Long id) {
        ctRoleService.deleteRole(id);
    }

    /**
     * Update the permissions of a role
     * 更新角色的权限
     * @param dto
     */
    @SaCheckPermission("update-role-permissions")
    @PostMapping("/update-role-permissions")
    public void updatePermissionsToUser(@RequestBody CtUpdateRolePermissionsDTO dto) {
        ctRoleService.updatePermissionsToRole(dto.getRoleId(), dto.getAssignPermissionIds(),dto.getRemovePermissionIds());
    }


    /**
     * Retrieve the list of permission names for a specific role
     * 获取某个角色的权限名列表
     * @param id
     * @return
     */
    @SaCheckPermission("get-role-permissions")
    @GetMapping("/get-role-permissions")
    public ApiResult getRolePermissions(Long id) {
        List<String> permissionNames = ctRoleService.getRolePermissionNames(id);
        return ApiResult.success(permissionNames);
    }

}
