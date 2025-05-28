package com.sc2.cmtrip.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.sc2.cmtrip.common.ApiResult;
import com.sc2.cmtrip.dto.CtUpdateUserRolesDTO;
import com.sc2.cmtrip.entity.CtUser;
import com.sc2.cmtrip.service.CtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class CtUserController {

    @Autowired
    private CtUserService ctUserService;

    /**
     * Register a new user
     * 新用户注册
     * @param newUser
     */
    @PostMapping("/register")
    public void registerUser(@RequestBody CtUser newUser) {
        ctUserService.registerUser(newUser);
    }

    /**
     * User login
     * 用户登录
     * @return
     */
    @PostMapping("/login")
    public ApiResult doLogin(@RequestBody Map<String, String> loginData) {
        String userName = loginData.get("userName");
        String password = loginData.get("password");
        String saToken = ctUserService.doLogin(userName, password);
        return ApiResult.success(saToken);
    }


    /**
     * Delete a user
     * 删除用户信息
     * @param id
     */
    @SaCheckPermission("delete-user")
    @DeleteMapping("/delete")
    public void deleteUser(Long id) {
        ctUserService.deleteUser(id);
    }

    /**
     * Return the user entity with the specified ID
     * 返回指定id的用户实体
     * @param userId
     * @return
     */
    @SaCheckPermission("get-user-info-by-id")
    @GetMapping("/info")
    public CtUser getUserInfo(Long userId) {
        return ctUserService.getById(userId);
    }

    /**
     * Log out the user with the specified ID
     * 注销指定id用户的登录
     * @param
     */
    @SaCheckLogin
    @PostMapping("/logout")
    public void doLogout() {
        ctUserService.doLogout();
    }

    /**
     * Retrieve the user list
     * 获取用户列表
     * @return
     */
    @SaCheckPermission("get-user-list")
    @GetMapping("/list")
    public ApiResult getUserList() {
        List<CtUser> userList = ctUserService.getUserList();
        return ApiResult.success(userList);
    }

    /**
     * Retrieve the roles of a user
     * 获取某个用户的角色名列表
     * @param id
     * @return
     */
    @SaCheckPermission("get-user-roles")
    @GetMapping("/get-user-roles")
    public ApiResult getUserRoleNames(Long id) {
        List<String> roleNames = ctUserService.getRoleNamesByUserId(id);
        return ApiResult.success(roleNames);
    }

    /**
     * Update the roles of a user
     * 更新用户的角色
     * @param dto
     */
    @SaCheckPermission("update-user-roles")
    @PostMapping("/update-user-roles")
    public void updateRolesToUser(@RequestBody CtUpdateUserRolesDTO dto) {
        ctUserService.updateRolesToUser(dto.getUserId(), dto.getAssignRoleIds(), dto.getRemoveRoleIds());
    }

    /**
     * Retrieve the permissions of a user
     * 获取某个用户的权限名列表
     * @param id
     * @return
     */
    @SaCheckPermission("get-user-permissions")
    @GetMapping("/get-user-permissions")
    public ApiResult getUserPermissionNames(Long id) {
        List<String> permissionNames = ctUserService.getPermissionNamesByUserId(id);
        return ApiResult.success(permissionNames);
    }

}
