package com.zcdy.dsc.modules.operation.equipment.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述: 申请审核历史
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05
 * 版本号: V1.0
 */
@Data
@ApiModel(value="OptApplyVerifyAddVo", description="申请审核历史")
public class OptApplyVerifyAddVo {

	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**申请信息id*/
	@Excel(name = "申请信息id", width = 15)
    @ApiModelProperty(value = "申请信息id")
	private String applyId;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
	private String verifyStatus;
	/**审核意见*/
	@Excel(name = "审核意见", width = 15)
    @ApiModelProperty(value = "审核意见")
	private String verifyDesc;
}
