package com.zcdy.dsc.modules.operation.equipment.vo.knowlege;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeAttach;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.LinkedList;

/**
 * @ClassName : KnowlegeItemVo
 * 描述 : 条目包装类
 * 版本号: V1.0
 * @Author : Lyp
 * 创建时间： 2020-01-09 11:51
 */
@Getter
@Setter
@ApiModel(value="KnowlegeItemVo", description="条目包装类")
public class KnowlegeItemVo{
    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**操作章程名称*/
    @Excel(name = "操作章程名称", width = 15)
    @ApiModelProperty(value = "操作章程名称")
    private java.lang.String operationName;
    /**安全事项*/
    @ApiModelProperty(value = "安全事项集合")
    private LinkedList<KnowlegeCautionVo> knowlegeCautionList;
    /**操作规程*/
    @ApiModelProperty(value = "操作规程集合")
    private LinkedList<KnowlegeOperationVo> knowlegeOperationList;
    /**操作规程*/
    @ApiModelProperty(value = "手册说明")
    private LinkedList<KnowlegeAttach> knowlegeAttachList;
}
