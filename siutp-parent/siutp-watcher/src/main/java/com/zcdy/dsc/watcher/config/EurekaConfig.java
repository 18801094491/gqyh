package com.zcdy.dsc.watcher.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * @author Roberto
 * @date 2020/06/01
 */
@Profile(value={"uat","pro"})
@Configuration
@EnableDiscoveryClient
public class EurekaConfig {

}
