package com.sc2.cmtrip.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sc2")
@Getter
@Setter
@ToString
public class Sc2Config {

    private String name;
    private String version;
    private String copyRightYear;
    private String profile;

}