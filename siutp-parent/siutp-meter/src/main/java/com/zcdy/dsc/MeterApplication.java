package com.zcdy.dsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class MeterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeterApplication.class, args);
		log.info("The meter-collection application is running! ");
	}
}
