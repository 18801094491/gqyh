package com.zcdy.dsc.modules.operation.equipment.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 货物信息表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-03
 * 版本号: V1.0
 */
@Data
@TableName("opt_goods_item")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_goods_item", description="货物信息表")
public class OptGoodsItem {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**外部数据id*/
	@Excel(name = "外部数据id", width = 15)
    @ApiModelProperty(value = "外部数据id")
	private java.lang.String dataId;
	/**货物编码*/
	@Excel(name = "货物编码", width = 15)
    @ApiModelProperty(value = "货物编码")
	private java.lang.String itemSn;
	/**货物类型*/
	@Excel(name = "货物类型", width = 15)
    @ApiModelProperty(value = "货物类型")
	private java.lang.String itemType;
	/**批次号*/
	@Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号")
	private java.lang.String batchSn;
	/**货物状态0-初始化，1-在库，2-外借，3-损坏，4-报废，5-丢失，6-过期，7-已使用,8-盘亏,9-已出库*/
	@Excel(name = "货物状态0-初始化，1-在库，2-外借，3-损坏，4-报废，5-丢失，6-过期，7-已使用,8-盘亏,9-已出库", width = 15)
    @ApiModelProperty(value = "货物状态0-初始化，1-在库，2-外借，3-损坏，4-报废，5-丢失，6-过期，7-已使用,8-盘亏,9-已出库")
	private java.lang.String itemStatus;
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
	/**删除标识0-正常,1-已删除*/
	@Excel(name = "删除标识0-正常,1-已删除", width = 15)
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
}
