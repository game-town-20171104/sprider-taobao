package com.ylfin.spider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author godslhand
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "shell")
@Data
public class ShellProperties {

    private String host;
    private String username ;
    private String password;

}
