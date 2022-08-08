package com.zcdy.dsc.modules.operation.equipment.vo.knowlege;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value="KnowlegeCautionVo", description="知识库条目操作注意事项")
public class KnowlegeCautionVo {
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "id")
	private java.lang.String id;
	/**安全事项内容*/
	@Excel(name = "安全事项内容", width = 15)
    @ApiModelProperty(value = "安全事项内容")
	private String cautionItem;
	/**展示顺序，升序*/
	@Excel(name = "展示顺序，升序", width = 15)
    @ApiModelProperty(value = "展示顺序，升序")
	private Integer displayOrder;

}
