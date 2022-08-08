package com.zcdy.dsc.modules.worklist.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 工单信息-执行人的位置坐标
 * @Author: 在信汇通
 * @Date:   2021-03-03
 * @Version: V1.0
 */
@Data
@ApiModel(value="工单信息-执行人的位置坐标", description="工单信息-执行人的位置坐标")
public class WorkListLocation
{
    /**经度*/
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
    /**纬度*/
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;

    /**时间戳*/
    @ApiModelProperty(value = "时间戳")
    private Date dateTime;
}
