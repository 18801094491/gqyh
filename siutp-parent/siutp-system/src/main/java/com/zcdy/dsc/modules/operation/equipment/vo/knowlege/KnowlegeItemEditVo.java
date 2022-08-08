package com.zcdy.dsc.modules.operation.equipment.vo.knowlege;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @ClassName : KnowlegeItemVo
 * 描述 : 条目包装类
 * 版本号: V1.0
 * @Author : Lyp
 * 创建时间： 2020-01-09 11:51
 */
@Getter
@Setter
@ApiModel(value="KnowlegeItemEditVo", description="条目包装类")
public class KnowlegeItemEditVo {
    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
    /**操作章程名称*/
    @Excel(name = "操作章程名称", width = 15)
    @ApiModelProperty(value = "操作章程名称")
    private String operationName;
}
