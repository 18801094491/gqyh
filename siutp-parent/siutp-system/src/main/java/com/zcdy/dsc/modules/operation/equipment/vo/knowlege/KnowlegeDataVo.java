package com.zcdy.dsc.modules.operation.equipment.vo.knowlege;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述: 知识库管理
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */

@Data
@ApiModel(value = "KnowlegeListVo", description = "知识库管理")
public class KnowlegeDataVo {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 知识内容
     */
    @ApiModelProperty(value = "知识名称")
    private String knowlegeName;
    /**
     * 类型-维护分类字典
     */
    @ApiModelProperty(value = "知识类型")
    private java.lang.String type;
    /**
     * 类型
     */
    @ApiModelProperty(value = "知识类型名称")
    private java.lang.String typeName;
    /**
     * 设备类型名称
     */
    @ApiModelProperty(value = "设备类型名称")
    private java.lang.String equipmentTypeName;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private java.lang.String equipmentType;
    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号")
    private java.lang.String equipmentModel;
    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号名称")
    private java.lang.String equipmentModelName;
    /**
     * 设备规格
     */
    @ApiModelProperty(value = "设备规格")
    private java.lang.String equipmentSpecs;
    /**
     * 设备规格名称
     */
    @ApiModelProperty(value = "设备规格名称")
    private java.lang.String equipmentSpecsName;
    /**
     * 知识条目
     */
    @ApiModelProperty(value = "知识条目")
    private java.lang.Integer itemCount;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;

    /**
     * 所属资源
     */
    @ApiModelProperty(value = "所属资源")
    private String resource;
    /**
     * 所属资源
     */
    @ApiModelProperty(value = "所属资源名称")
    private String resourceName;
    /**
     * 供应商Id
     */
    @ApiModelProperty(value = "供应商Id")
    private String supplierId;
    /**
     * 供应商Id
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;
}
