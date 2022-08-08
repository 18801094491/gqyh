package com.zcdy.dsc.watcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Roberto
 * @CreateTime:2020年2月27日 上午9:57:37
 * @Description: <p>admin Server启动器</p>
 */
@Slf4j
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class AdminServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AdminServerApplication.class, args);
		if(log.isInfoEnabled()){
		    log.info("Now, the springboot admin server is working!");
		}
	}
}
