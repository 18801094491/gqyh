package com.zcdy.dsc.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author: Roberto
 * @CreateTime:2019年11月20日 下午2:15:56
 * @Description: <p>eureka服务启动器(配置中心)</p>
 */
@SpringBootApplication
@EnableEurekaServer
public class Application {


	private static Logger logger = LoggerFactory.getLogger(Application.class); 
	
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
		logger.info("The eureka Server register center start successfully");
	}
}
