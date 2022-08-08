package com.zcdy.dsc.modules.collection.iot.vo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author： Roberto
 * 创建时间：2020年3月15日 下午11:30:06
 * 描述: <p>查询采集数据历史参数，避免转化问题</p>
 */
@ApiModel("IotHistoryDataMeanParam")
public class IotHistoryDataMeanParam {

	//一组设备id
	@ApiModelProperty(value="一组设备id", required=true)
	private String optIds;
	
	@ApiModelProperty(value="一组设备id", hidden=true)
	private String[] ids;
	
	@ApiModelProperty(value="周期值", notes="数值", required=true)
	private Integer gap;
	
	@ApiModelProperty(value="周期值单位", notes="单位", required=true)
	private String timeUnit;
	
	//颗粒度
	@ApiModelProperty(value="颗粒度", notes="数值", required=true)
	private Integer particle;
	
	@ApiModelProperty(value="颗粒度单位", notes="单位", required=true)
	private String particleUnit;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value="开始时间", notes="格式：2020-03-15 23:32:00", required=true)
	private Date left;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value="结束时间", notes="格式：2020-03-15 23:32:00", required=true)
	private Date right;
	
	@ApiModelProperty(value="时间戳", notes="连接20s有效，导出接口限制")
	private Long timestamp;
	
	@ApiModelProperty(value="token", notes="token字符串，导出接口限制")
	private String token;
	
	public String getOptIds() {
		return optIds;
	}

	public void setOptIds(String optIds) {
		this.optIds = optIds;
	}

	public Integer getGap() {
		if(null==gap||0==this.gap.intValue()){
			this.gap = 1;
		}
		return gap;
	}

	public void setGap(Integer gap) {
		this.gap = gap;
	}

	public String getTimeUnit() {
		if(StringUtils.isEmpty(timeUnit)){
			this.timeUnit = "h";
		}
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public Integer getParticle() {
		return particle;
	}

	public void setParticle(Integer particle) {
		this.particle = particle;
	}

	public String getParticleUnit() {
		return particleUnit;
	}

	public void setParticleUnit(String particleUnit) {
		this.particleUnit = particleUnit;
	}

	public Date getLeft() {
		if(null==this.left){
			Instant instant = Instant.now();
			LocalDateTime dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
			dateTime.plusDays(-1);
			this.left = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		}
		return left;
	}

	public void setLeft(Date left) {
		this.left = left;
	}

	public Date getRight() {
		if(StringUtils.isEmpty(this.timeUnit)){
			this.timeUnit = "h";
		}
		Instant instant = getLeft().toInstant();
		LocalDateTime dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		switch (this.timeUnit) {
		case "m":
			dateTime = dateTime.plusMinutes(this.gap);
			break;
		case "h":
			dateTime = dateTime.plusHours(this.gap);
			break;
		case "d":
			dateTime = dateTime.plusDays(this.gap);
			break;
		case "M":
			dateTime = dateTime.plusMonths(this.gap);
			break;
		case "y":
			dateTime = dateTime.plusYears(this.gap);
			break;
		default:
			break;
		}
		this.right = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		return right;
	}

	public void setRight(Date right) {
		this.right = right;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String[] getIds() {
		if(!StringUtils.isEmpty(this.optIds)){
			this.ids = this.optIds.split(",");
		}
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
}
