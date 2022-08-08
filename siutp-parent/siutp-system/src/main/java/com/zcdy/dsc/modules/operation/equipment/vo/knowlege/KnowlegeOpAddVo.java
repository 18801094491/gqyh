package com.zcdy.dsc.modules.operation.equipment.vo.knowlege;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述: 操作规程包装类
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Data
@ApiModel(value="KnowlegeOpAddVo", description="知识库条目操作章程")
public class KnowlegeOpAddVo {
	/**规程内容*/
	@Excel(name = "规程内容", width = 15)
    @ApiModelProperty(value = "规程内容")
	private String operationItem;
	/**知识库分项id*/
	@Excel(name = "条目id", width = 15)
	@ApiModelProperty(value = "条目id")
	private java.lang.String knowlegeItemId;
	/**展示顺序，升序*/
	@Excel(name = "展示顺序，升序", width = 15)
    @ApiModelProperty(value = "展示顺序，升序")
	private Integer displayOrder;

}
