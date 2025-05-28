package com.sc2.cmtrip.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sc2.cmtrip.common.ApiResult;
import com.sc2.cmtrip.entity.CtPermission;
import com.sc2.cmtrip.service.CtPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class CtPermissionController {

    @Autowired
    private CtPermissionService ctPermissionService;

    /**
     * Return all permission entities
     * 返回所有permission实体
     * @return
     */
    @SaCheckPermission("list-all-permissions")
    @GetMapping("/list")
    public ApiResult getPermissionList() {
        return ApiResult.success(ctPermissionService.getPermissionList());
    }

    /**
     * Retrieve the list of permission entities associated with the given roleId
     * 根据roleId获取其所拥有的权限实体列表
     * @param roleId
     * @return
     */
    @SaCheckPermission("get-permission-list-by-role-id")
    @GetMapping("/get-list-by-role-id")
    public ApiResult getPermissionsByRoleId(Long roleId) {
        return ApiResult.success(ctPermissionService.getPermissionsByRoleId(roleId));
    }

    /**
     * Add a permission
     * 新增一个权限
     * @param ctPermission
     */
    @SaCheckPermission("add-permission")
    @PostMapping("/add")
    public void addPermission(@RequestBody CtPermission ctPermission) {
        ctPermissionService.addPermission(ctPermission);
    }

    /**
     * Update a permission
     * 更新一个权限
     * @param ctPermission
     */
    @SaCheckPermission("edit-permission")
    @PostMapping("/edit")
    public void editPermission(@RequestBody CtPermission ctPermission) {
        ctPermissionService.editPermission(ctPermission);
    }

    /**
     * Delete a permission
     * 删除一个权限
     * @param id
     */
    @SaCheckPermission("delete-permission")
    @DeleteMapping("/delete")
    public void deletePermission(Long id) {
        ctPermissionService.deletePermission(id);
    }



}
