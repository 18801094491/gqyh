package com.zcdy.dsc.common.framework.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author： Roberto
 * 创建时间：2020年1月16日 下午6:50:57
 * 描述: <p>influxdb业务逻辑层</p>
 */
public class InfluxService {
	
	private final Logger logger = LoggerFactory.getLogger(InfluxService.class);
	
	private InfluxDB influxdb;
	
	public InfluxService(InfluxDB influxdb){
		this.influxdb = influxdb;
	}
	
	public <T> void write(T item){
		Point point = Point.measurementByPOJO(item.getClass())
				.addFieldsFromPOJO(item)
				.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.build();
		this.influxdb.write(point);
	}
	
	public <T> void writeBatch(List<T> datas){
		BatchPoints batchPoints = BatchPoints.builder().consistency(ConsistencyLevel.ALL).build();
		datas.forEach(item->{
			Point point = Point.measurementByPOJO(item.getClass())
					.addFieldsFromPOJO(item)
					.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
					.build();
			batchPoints.point(point);
		});
		this.influxdb.write(batchPoints);
	}
	
	public <T> void writeBatchWithTime(List<T> datas, Class<T> clazz){
		BatchPoints batchPoints = BatchPoints.builder().consistency(ConsistencyLevel.ALL).build();
		Field timeField;
		try {
			timeField = clazz.getDeclaredField("time");
			timeField.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			logger.error("Field操作错误");
			return;
		}
		datas.forEach(item->{
			try {
				long time = timeField.getLong(item);
				Point point = Point.measurementByPOJO(clazz)
						.addFieldsFromPOJO(item)
						.time(time, TimeUnit.MILLISECONDS)
						.build();
				batchPoints.point(point);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("Field赋值错误");
			}
		});
		this.influxdb.write(batchPoints);
	}
	
	
	public QueryResult query(String command) {
		return influxdb.query(new Query(command));
	}
	
	public QueryResult query(Query query) {
		return influxdb.query(query);
	}
	
	public <T> List<T> query(String command, Class<T> clazz){
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		QueryResult  queryResult = influxdb.query(new Query(command));
		List<T> list = resultMapper.toPOJO(queryResult, clazz);
		return list;
	}
	
	public <T> List<T> query(Query query, Class<T> clazz){
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		QueryResult  queryResult = influxdb.query(query);
		List<T> list = resultMapper.toPOJO(queryResult, clazz);
		return list;
	}
	
	public <T> List<T> query(Query query, Class<T> clazz, String measurement){
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		QueryResult  queryResult = influxdb.query(query);
		List<T> list = resultMapper.toPOJO(queryResult, clazz, measurement);
		return list;
	}
}
