package com.zcdy.dsc.common.framework.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;

import lombok.extern.slf4j.Slf4j;

/**
 * @author： Roberto
 * 创建时间：2020年1月16日 下午6:42:37
 * 描述: <p>influxdb操作bean</p>
 */
@Slf4j
public class InfluxdbOperaterBean {

	private String userName;
	private String password;
	private String url;
	public String database;
	private String retentionPolicy;
	// InfluxDB实例
	private InfluxDB influxDb;

	public InfluxdbOperaterBean(String userName, String password, String url, String database, String retentionPolicy) {
		this.userName = userName;
		this.password = password;
		this.url = url;
		this.database = database;
		this.retentionPolicy = retentionPolicy == null || "".equals(retentionPolicy) ? "autogen" : retentionPolicy;
		this.influxDb = influxDbBuild();
	}

	/**
	 * 连接数据库 ，若不存在则创建
	 * @return influxDb实例
	 */
	private InfluxDB influxDbBuild() {
		if (influxDb == null) {
			influxDb = InfluxDBFactory.connect(url, userName, password);
		}
		try {
			createDb(database);
			influxDb.setDatabase(database);
		} catch (Exception e) {
			log.error("create influx db failed, error: {}", e.getMessage());
		} finally {
			influxDb.setRetentionPolicy(retentionPolicy);
		}
		influxDb.setLogLevel(InfluxDB.LogLevel.BASIC);
		return influxDb;
	}

	/****
	 * 创建数据库
	 * @param database
	 */
	private void createDb(String database) {
		influxDb.query(new Query("CREATE DATABASE " + database));
	}
	
	public InfluxDB getInfluxdb(){
		return this.influxDb;
	}
}
