package com.zcdy.dsc.modules.map.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 地图-区域管理表
 * @Author: 在信汇通
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Data
@TableName("map_region_management")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="map_region_management", description="地图-区域管理表")
public class MapRegionManagement {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**区域名字*/
	@Excel(name = "区域名字", width = 15)
    @ApiModelProperty(value = "区域名字")
	private java.lang.String regionName;
	/**区域范围-经纬度合集*/
	@Excel(name = "区域范围-经纬度合集", width = 15)
    @ApiModelProperty(value = "区域范围-经纬度合集")
	private java.lang.Object regionAddressInfo;
	/**区域中心点-经度*/
	@Excel(name = "区域中心点-经度", width = 15)
    @ApiModelProperty(value = "区域中心点-经度")
	private java.lang.String regionPointLongitude;
	/**区域中心店-经度*/
	@Excel(name = "区域中心店-经度", width = 15)
    @ApiModelProperty(value = "区域中心店-经度")
	private java.lang.String regionPointLatitude;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**标识位 true 可用/false 不可用*/
	@Excel(name = "标识位 true 可用/false 不可用", width = 15)
    @ApiModelProperty(value = "标识位 true 可用/false 不可用")
	private java.lang.String active;
	/**删除标识0-正常,1-已删除*/
	@ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
}
