package com.sc2.cmtrip.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.CtUser;
import com.sc2.cmtrip.service.CtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @Autowired
    private CtUserService ctUserService;

    @Autowired
    private CtUtils ctUtils;

    /**
     * User Login
     * 用户登录
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping("doLogin")
    public String doLogin(String userName, String password) {
        // Check the database for an existing `user_name` with the same name 根据name查询数据库中是否有同名的user_name
        CtUser user = ctUserService.findByUserName(userName);
        if (user != null) {
            String name = user.getUserName();
            String pwd = user.getPassword();
            String salt = user.getSalt();
            String forCheckPwd = ctUtils.getHashedPassword(pwd, salt);
            if (forCheckPwd.equals(password)) {
                Long userId = user.getId();
                // Perform login 执行登录
                StpUtil.login(userId);
                // Cache the current user information 缓存当前用户信息
                StpUtil.getTokenSession().set("loginUser", user);
                // return the token 返回token
                return StpUtil.getTokenInfo().tokenValue;
            } else {
                throw new RuntimeException("Password is wrong!");
            }
        } else {
            throw new RuntimeException("User does not exist!");
        }
    }

    /**
     * Check the login status
     * 检查登录状态
     * @return
     */
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("Login Status: " + StpUtil.isLogin());
    }

    /**
     * Check the token information
     * 检查token信息
     * @return
     */
    @SaCheckLogin
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    /**
     * User Log out
     * 用户登出
     * @return
     */
    @SaCheckLogin
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("Logged out!");
    }

}
