package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述: 维保记录 详情返回vo
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-17
 * 版本号: V1.0
 */
@Data
@ApiModel(value="EquipmentUpkeepdetVo", description="维保记录")
public class EquipmentUpkeepdetVo {
	/**维保记录上传的图片信息*/
	@ApiModelProperty(value = "维保记录上传的图片信息")
	private List<AttachVo> upkeepAttachList;

	/**维保人员信息*/
	@ApiModelProperty(value = "维保人员信息")
	private List<UpkeepUsersVo> upkeepUsersList;
}
