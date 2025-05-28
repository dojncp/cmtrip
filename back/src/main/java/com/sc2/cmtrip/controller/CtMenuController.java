package com.sc2.cmtrip.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.sc2.cmtrip.common.ApiResult;
import com.sc2.cmtrip.entity.CtMenu;
import com.sc2.cmtrip.entity.CtUser;
import com.sc2.cmtrip.service.CtMenuService;
import com.sc2.cmtrip.service.CtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class CtMenuController {

    @Autowired
    private CtMenuService ctMenuService;

    /**
     * Automatically retrieve the menu list to be displayed on the frontend based on the user's roles
     * 自动根据用户的不同角色，获取应显示在前端的菜单列表
     * @return
     */
    @SaCheckLogin
    @GetMapping("/list")
    public ApiResult<List<CtMenu>> getMenuList() {
        return ApiResult.success(ctMenuService.getMenuList());
    }

}
