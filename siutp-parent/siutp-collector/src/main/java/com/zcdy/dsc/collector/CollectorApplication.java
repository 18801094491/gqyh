package com.zcdy.dsc.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: Roberto
 * @CreateTime:2019年11月20日 下午3:09:39
 * @Description:采集系统启动器
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableScheduling
public class CollectorApplication {

    private static Logger logger = LoggerFactory.getLogger(CollectorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
        logger.info("The eureka client service [collection] is start successfully!");
    }
}
