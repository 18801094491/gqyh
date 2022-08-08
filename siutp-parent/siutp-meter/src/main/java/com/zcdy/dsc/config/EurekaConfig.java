package com.zcdy.dsc.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Roberto
 * @date 2020/06/01
 */
@Configuration
public class EurekaConfig {

    @Profile(value = {"dev"})
    @EnableDiscoveryClient(autoRegister = false)
    private class DevConfig {

    }
    
    @Profile(value = {"uat", "pro"})
    @EnableDiscoveryClient
    private class UatConfig {

    }
}
