package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述: 维保记录 详情 维保记录上传的图片信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-17
 * 版本号: V1.0
 */
@Data
@ApiModel(value="EquipmentUpkeepdetVo", description="维保记录上传的图片信息")
public class UpkeepUsersVo {
	//人员信息
	@ApiModelProperty(value="人员信息")
	private String upkeepUser;
}
