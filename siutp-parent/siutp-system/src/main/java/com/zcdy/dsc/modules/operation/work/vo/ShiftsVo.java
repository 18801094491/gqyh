package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述: 班次列表
 *
 * @author： songguang.jiao
 * 创建时间：  2020年1月10日 下午1:31:33
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "ShiftsVo", description = "班次列表")
public class ShiftsVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 班次名称
     */
    @ApiModelProperty(value = "班次名称")
    @Excel(name = "班次名称", width = 150)
    private java.lang.String shiftsName;
    /**
     * 班次描述
     */
    @Excel(name = "班次描述", width = 150)
    @ApiModelProperty(value = "班次描述")
    private java.lang.String shiftsDescribe;
    /**
     * 上班时间
     */
    @Excel(name = "上班时间", width = 150)
    @ApiModelProperty(value = "上班时间")
    private String startTime;
    /**
     * 下班时间
     */
    @Excel(name = "下班时间", width = 150)
    @ApiModelProperty(value = "下班时间")
    private String overTime;
    /**
     * 下班是否需要打卡,0-否，1-是
     */
    @ApiModelProperty(value = "下班是否需要打卡,0-否，1-是")
    private java.lang.String offSign;
    /**
     * 班次状态
     */
    @ApiModelProperty(value = "班次状态")
    private java.lang.String shiftsStatus;
    /**
     * 开始休息时间
     */
    @Excel(name = "开始休息时间", width = 150)
    @ApiModelProperty(value = "开始休息时间")
    private String restStartTime;
    /**
     * 结束休息时间
     */
    @Excel(name = "结束休息时间", width = 150)
    @ApiModelProperty(value = "结束休息时间")
    private String restOverTime;
    /**
     * 上班时长
     */
    @Excel(name = "上班时长", width = 150)
    @ApiModelProperty(value = "上班时长")
    private String workHours;


}
