package com.zcdy.dsc.modules.operation.work.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * 工单描述
 * @author songguang.jiao
 * @date 2020/7/8/0008  12:56:41
 */
@Getter
@Setter
@ApiModel("WorkJobVo")
public class WorkJobVo {


    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 工单名称(计划名称+巡检日期)
     */
    @ApiModelProperty(value = "工单名称(计划名称+巡检日期)")
    private java.lang.String workName;
    /**
     * 工单编号
     */
    @ApiModelProperty(value = "工单编号")
    private java.lang.String workSn;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板id")
    private java.lang.String tplId;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板")
    private java.lang.String tplName;
    /**
     * 路线名称
     */
    @ApiModelProperty(value = "路线名称")
        private java.lang.String routeName;
    /**
     * 路线注意事项
     */
    @ApiModelProperty(value = "路线注意事项")
    private java.lang.String routeAttention;
    /**
     * 路线备注
     */
    @ApiModelProperty(value = "路线备注")
    private java.lang.String routeRemark;

    /**
     * 地图画线
     */
    @ApiModelProperty(value = "地图画线")
    private java.lang.String routeMapLines;

    /**
     * 实际巡检日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "实际巡检日期")
    private java.util.Date inspectTime;
    /**
     * 工单完成时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "工单完成时间")
    private java.util.Date finishedTime;
    /**
     * 工单附件
     */
    @ApiModelProperty(value = "工单附件")
    private java.lang.String workFile;
    /**
     * 工单附件
     */
    @ApiModelProperty(value = "巡检人")
    private java.lang.String inspectorName;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;



    @ApiModelProperty("巡检点列表")
    private List<WorkJobPoint> points;

    @Getter
    @Setter
    public static final class WorkJobPoint{
        @ApiModelProperty("巡检点id")
        private String pointId;

        @ApiModelProperty("巡检点名称")
        private String pointName;

        @ApiModelProperty("模板id")
        private String tplId;
    }

}
