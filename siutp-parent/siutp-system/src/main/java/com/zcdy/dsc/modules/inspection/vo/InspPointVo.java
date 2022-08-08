package com.zcdy.dsc.modules.inspection.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 巡检点
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list_inspection_point", description="巡检点Vo")
public class InspPointVo {
    
	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**所属区域*/
    @ApiModelProperty(value = "所属区域")
	private java.lang.String areaId;
	/**巡检点名称*/
    @ApiModelProperty(value = "巡检点名称")
	private java.lang.String name;
	/**巡检点编号*/
    @ApiModelProperty(value = "巡检点编号")
	private java.lang.String code;
	/**是否重点1是0否*/
    @ApiModelProperty(value = "是否重点1是0否")
	private java.lang.Integer important;
	/**注意事项*/
    @ApiModelProperty(value = "注意事项")
	private java.lang.String notices;
	/**巡检点类型*/
    @ApiModelProperty(value = "巡检点类型")
	private java.lang.String type;
	/**经度*/
    @ApiModelProperty(value = "经度")
	private java.lang.String longitude;
	/**纬度*/
    @ApiModelProperty(value = "纬度")
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
