package com.sc2.cmtrip.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    /**
     * Register Sa-Token interceptor to enable annotation-based authentication
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**") // In principle, all endpoints require login authentication 原则上所有接口都要登录认证
                .excludePathPatterns("/user/login") // exclude 排除 /user/login
                .excludePathPatterns("/user/register") // exclude 排除 /user/register
                .excludePathPatterns("/user/forget-password") // exclude 排除 /user/forget-password
                .excludePathPatterns("/profile/**"); // exclude 排除 /profile/**
    }

    /**
     * Register [Sa-Token global filter]
     * 注册 [Sa-Token 全局过滤器]
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // Specify [intercepted routes] and \[excluded routes] 指定 [拦截路由] 与 [放行路由]
                .addInclude("/**")
                .addExclude("/favicon.ico")
                // Authentication function: executed on every request 认证函数: 每次请求执行
                .setAuth(obj -> {
                    SaManager.getLog().debug("----- 请求path={}  提交token={}",
                            SaHolder.getRequest().getRequestPath(),
                            StpUtil.getTokenValue());
                    // ...
                })
                // Exception handler function: executed whenever an exception occurs in the authentication function 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    return SaResult.error(e.getMessage());
                })
                // Pre-function: executed before each authentication function 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    // ---------- Set Cross-Origin Response Headers ---------- ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            // Allow specified domains to access cross-origin resources 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // Allow all request methods 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "*")
                            // Allowed header parameters 允许的请求头参数
                            .setHeader("Access-Control-Allow-Headers", "*")
                            // Validity period 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                    ;
                    // If it is a preflight request, immediately return to the frontend 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> System.out.println("\\-------- OPTIONS preflight request, no processing performed. --------\n"))
                            .back();
                });
    }

}
