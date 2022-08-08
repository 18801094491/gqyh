package com.zcdy.dsc.modules.operation.work.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 巡检点Vo类
 *
 * @author songguang.jiao
 * @date 2020/7/2 9:26
 */
@Getter
@Setter
@ApiModel("WorkInspectionPointVo")
public class WorkInspectionPointVo {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 巡检点名称
     */
    @ApiModelProperty(value = "巡检点名称")
    private java.lang.String name;
    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private java.lang.String attention;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板")
    private java.lang.String tplName;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板Id")
    private java.lang.String tplId;
    /**
     * 数据Id
     */
    @ApiModelProperty(value = "数据Id")
    private java.lang.String dataId;
    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备Id")
    private java.lang.String equipmentId;
    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    private java.lang.String category;
    /**
     * 类别
     */
    @ApiModelProperty(value = "类别名称")
    private java.lang.String categoryName;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
}
