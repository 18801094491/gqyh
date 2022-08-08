package com.zcdy.dsc.modules.operation.equipment.vo;

import java.util.List;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 维保记录
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-17
 * 版本号: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EquipmentUpkeepVo", description="维保记录")
public class EquipmentUpkeepVo {
    
	/**资产名称*/
	@Excel(name = "资产名称", width = 15)
	@ApiModelProperty(value = "资产名称")
	private String equipmentName;
	/**维保类型值*/
	@Excel(name = "维保类型", width = 15)
	@ApiModelProperty(value = "维保类型值")
	private String typeText;
	/**维保内容*/
	@Excel(name = "维保内容", width = 15)
    @ApiModelProperty(value = "维保内容")
	private String upkeepContent;
	/**维保原因*/
	@Excel(name = "维保原因", width = 15)
    @ApiModelProperty(value = "维保原因")
	private String upkeepReason;
	/**维保结果*/
	@Excel(name = "维保结果", width = 15)
    @ApiModelProperty(value = "维保结果")
	private String upkeepResult;
	/**上报人*/
	@Excel(name = "上报人", width = 15)
	@ApiModelProperty(value = "上报人")
	private String upkeepCreatorName;
	/**上报时间*/
	@Excel(name = "上报时间", width = 20, format = "yyyy-MM-dd HH:mm")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "上报时间")
	private java.util.Date upkeepTime;
	
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**资产*/
    @ApiModelProperty(value = "资产")
	private String equipmentId;
	/**上报人id*/
    @ApiModelProperty(value = "上报人id")
	private String upkeepCreator;
	 /**参与人id*/
    @ApiModelProperty(value="参与人id")
    private String upkeepUsersId;
    /**参与人*/
    @Excel(name = "参与人", width = 25)
    @ApiModelProperty(value="参与人")
    private String upkeepUsers;
    //附件列表
    @ApiModelProperty(value="附件列表")
    private  List<AttachVo> list;
	/**维保类型*/
	@ApiModelProperty(value = "维保类型")
	private String type;
	
}
