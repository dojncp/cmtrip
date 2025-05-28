package com.sc2.cmtrip.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CtController {

    /**
     * Text returned when the user accesses the backend at `<url:port>`.
     * 用户访问后端程序运行的<url:端口号>时返回的文本
     * @return
     */
    @GetMapping
    public String hello() {
        return "Thank you for using CM-TRIP!";
    }
}
