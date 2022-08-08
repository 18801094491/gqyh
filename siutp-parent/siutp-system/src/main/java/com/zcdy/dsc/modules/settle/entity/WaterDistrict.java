package com.zcdy.dsc.modules.settle.entity;

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
 * @Description: 水表地区管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-21
 * @Version: V1.0
 */
@Data
@TableName("settle_water_district")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="settle_water_district", description="水表地区管理")
public class WaterDistrict {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**地区id*/
	@Excel(name = "地区id", width = 15)
    @ApiModelProperty(value = "地区id")
	private java.lang.String districtId;
	/**水表设备id*/
	@Excel(name = "水表设备id", width = 15)
    @ApiModelProperty(value = "水表设备id")
	private java.lang.String equipmentId;
	/**有效无效状态  1-有效，0-无效*/
	@Excel(name = "有效无效状态  1-有效，0-无效", width = 15)
    @ApiModelProperty(value = "有效无效状态  1-有效，0-无效")
	private java.lang.Short validStatus;
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
}
