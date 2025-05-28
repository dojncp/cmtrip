package com.sc2.cmtrip;

import cn.dev33.satoken.SaManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sc2.cmtrip.mapper")
public class CmtripApplication {

    public static void main(String[] args) {

        SpringApplication.run(CmtripApplication.class, args);

        System.out.println("Welcome to use the SC2-CM-TRIP framework!");
        System.out.println("" +
                "   _____ __  __     _______ _____  _____ _____  \n" +
                "  / ____|  \\/  |   |__   __|  __ \\|_   _|  __ \\ \n" +
                " | |    | \\  / |______| |  | |__) | | | | |__) |\n" +
                " | |    | |\\/| |______| |  |  _  /  | | |  ___/ \n" +
                " | |____| |  | |      | |  | | \\ \\ _| |_| |     \n" +
                "  \\_____|_|  |_|      |_|  |_|  \\_\\_____|_|     \n" +
                "                                                ");
        System.out.println("The configuration of Satoken is: \nSatoken配置如下：\n" + SaManager.getConfig());
    }

}
