package com.zcdy.dsc.modules.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zcdy.dsc.modules.inspection.param.InspAreaParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 巡检区域
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@TableName("work_list_inspection_area")
@ApiModel(value="work_list_inspection_area", description="巡检区域")
public class InspArea {

	public InspArea()
	{

	}

	public InspArea(InspAreaParam iap)
	{
		this.id = iap.getId();
		this.name = iap.getName();
		this.code = iap.getCode();
		this.delFlag = iap.getDelFlag();
	}
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**区域名称*/
    @ApiModelProperty(value = "区域名称")
	private java.lang.String name;
	/**区域编号*/
    @ApiModelProperty(value = "区域编号")
	private java.lang.String code;
	/**区域描述*/
    @ApiModelProperty(value = "区域描述")
	private java.lang.String description;
	/**中心点坐标经度*/
    @ApiModelProperty(value = "中心点坐标经度")
	private java.lang.String longitude;
	/**中心点坐标纬度*/
    @ApiModelProperty(value = "中心点坐标纬度")
	private java.lang.String latitude;
	/**删除标识0-正常,1-已删除*/
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
}
