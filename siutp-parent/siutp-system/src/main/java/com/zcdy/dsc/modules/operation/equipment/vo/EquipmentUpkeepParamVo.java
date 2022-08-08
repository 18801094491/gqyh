package com.zcdy.dsc.modules.operation.equipment.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentUpkeepAttach;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 维保新增修改入参
 * @author：  songguang.jiao
 * 创建时间： 2020年2月24日 下午2:45:28 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="EquipmentUpkeepParamVo",description="维保新增修改入参")
public class EquipmentUpkeepParamVo {

	/**id*/
    @ApiModelProperty(value = "id")
	private String id;
	/**资产*/
    @NotBlank(message="资产不能为空")
    @NotNull(message="资产不能为空")
    @ApiModelProperty(value = "资产")
	private String equipmentId;
	/**上报人id*/
    @NotBlank(message="上报人不能为空")
    @NotNull(message="上报人不能为空")
    @ApiModelProperty(value = "上报人id")
	private String upkeepCreator;
    /**参与人ids*/
    @NotBlank(message="参与人不能为空")
    @NotNull(message="参与人不能为空")
    @ApiModelProperty(value="参与人ids")
    private String upkeepUsers;
    /**上传图片路径*/
    @ApiModelProperty(value="上传图片")
    private List<EquipmentUpkeepAttach>  list;
	/**维保内容*/
    @NotBlank(message="维保内容不能为空")
    @NotNull(message="维保内容不能为空")
    @ApiModelProperty(value = "维保内容")
	private String upkeepContent;
	/**维保原因*/
    @NotBlank(message="维保原因不能为空")
    @NotNull(message="维保原因不能为空")
    @ApiModelProperty(value = "维保原因")
	private String upkeepReason;
	/**维保结果*/
    @NotBlank(message="维保结果不能为空")
    @NotNull(message="维保结果不能为空")
    @ApiModelProperty(value = "维保结果")
	private String upkeepResult;
	/**维保类型*/
    @NotBlank(message="维保类型不能为空")
    @NotNull(message="维保类型不能为空")
	@ApiModelProperty(value = "维保类型")
	private String type;
}
