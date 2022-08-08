package com.zcdy.dsc.modules.settle.param;

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
 * 描述: 每日模块用水量分页统计
 * @author: songguang.jiao
 * 创建时间:  2020年5月6日 上午11:36:00
 * 版本: V1.0
 */
@ApiModel(value="FlowRecordPageParam")
public class FlowRecordPageParam{

	@ApiModelProperty(value="周期值", notes="数值", required=true)
	private Integer gap;
	
	@ApiModelProperty(value="周期值单位", notes="单位", required=true)
	private String timeUnit;
	
	//颗粒度
	@ApiModelProperty(value="颗粒度(查询间隔)", notes="数值", required=true)
	private Integer particle;
	
	@ApiModelProperty(value="颗粒度单位", notes="单位", required=true)
	private String particleUnit;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value="开始时间(yyyy-MM-dd)", notes="开始时间(yyyy-MM-dd)", required=true)
	private Date left;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value="结束时间(yyyy-MM-dd)", notes="结束时间(yyyy-MM-dd)")
	private Date right;

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
			this.timeUnit = "d";
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
			this.timeUnit = "d";
		}
		Instant instant = getLeft().toInstant();
		LocalDateTime dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		switch (this.timeUnit) {
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
	
	
}
