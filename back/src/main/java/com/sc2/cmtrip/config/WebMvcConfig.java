package com.sc2.cmtrip.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private Sc2Config sc2Config;

    /**
     * Static resource mapping: When a user accesses a specific URL, the request is not handled by a Controller but directly serves the corresponding file from the local file system to the frontend
     * 静态资源映射：当用户访问某个特定URL时，不是去找Controller，而是直接从本地文件系统读取对应文件返回给前端
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profile" + "/**")
                .addResourceLocations("file:" + sc2Config.getProfile() + "/");
    }
}
