package com.zcdy.dsc.common.framework.influxdb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.common.framework.influxdb.InfluxdbOperaterBean;

/**
 * @author： Roberto
 * 创建时间：2020年1月16日 下午6:41:15
 * 描述: <p>influxdb配置</p>
 */
@Configuration
public class InfluxDbConfig {

	@Value("${spring.influx.url:''}")
	private String influxDbUrl;
	@Value("${spring.influx.user:''}")
	private String userName;

	@Value("${spring.influx.password:''}")
	private String password;

	@Value("${spring.influx.database:''}")
	private String database;

	// 数据保存策略
	@Value("${spring.influx.retention:''}")
	private String policyNamePix;

	@Bean
	public InfluxdbOperaterBean influxdbOperaterBean() {
		return new InfluxdbOperaterBean(userName, password, influxDbUrl, database, policyNamePix);
	}
	
	@Bean
	@ConditionalOnBean(name="influxdbOperaterBean")
	public InfluxService influxService(InfluxdbOperaterBean influxdbOperaterBean){
		return new InfluxService(influxdbOperaterBean.getInfluxdb());
	}
}
