package com.zcdy.dsc.modules.operation.equipment.vo.knowlege;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述: 安全事项包装类
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Data
@ApiModel(value="KnowlegeCaAddVo", description="知识库条目操作注意事项")
public class KnowlegeCaAddVo {
	/**id*/
	@ApiModelProperty(value = "id")
	private String id;
	/**知识库分项id*/
	@Excel(name = "条目id", width = 15)
	@ApiModelProperty(value = "条目id")
	private java.lang.String knowlegeItemId;
	/**安全事项内容*/
	@Excel(name = "安全事项内容", width = 15)
    @ApiModelProperty(value = "安全事项内容")
	private String cautionItem;
	/**展示顺序，升序*/
	@Excel(name = "展示顺序，升序", width = 15)
    @ApiModelProperty(value = "展示顺序，升序")
	private Integer displayOrder;

}
