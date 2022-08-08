package com.zcdy.dsc.modules.operation.equipment.vo.knowlege;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName : KnowlegeVo
 * 描述 : 知识库包装类
 * 版本号: V1.0
 * @Author : Lyp
 * 创建时间： 2020-01-09 11:42
 */
@Data
@ApiModel(value = "KnowlegeVo", description = "知识库包装类对象")
public class KnowlegeVo {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 知识内容
     */
    @NotEmpty(message = "知识名称不能为空")
    @ApiModelProperty(value = "知识名称")
    private java.lang.String knowlegeName;
    /**
     * 知识类型
     */
    @NotEmpty(message = "知识类型不能为空")
    @ApiModelProperty(value = "知识类型")
    private java.lang.String type;
    /**
     * 设备类型
     */
    @NotEmpty(message = "设备类型不能为空")
    @ApiModelProperty(value = "设备类型")
    private java.lang.String equipmentType;
    /**
     * 设备型号
     */
    @NotEmpty(message = "设备型号不能为空")
    @ApiModelProperty(value = "设备型号")
    private java.lang.String equipmentModel;
    /**
     * 设备规格
     */
    @NotEmpty(message = "设备规格不能为空")
    @ApiModelProperty(value = "设备规格")
    private java.lang.String equipmentSpecs;
    /**
     * 供应商Id
     */
    @NotEmpty(message = "供应商Id不能为空")
    @ApiModelProperty(value = "供应商Id")
    private String supplierId;

    /**
     * 所属资源
     */
    @ApiModelProperty(value = "所属资源")
    private String resource;
    /**
     * 条目集合
     */
    @ApiModelProperty(value = "条目集合")
    private List<KnowlegeItemVo> knowlegeItemVoList;
}
