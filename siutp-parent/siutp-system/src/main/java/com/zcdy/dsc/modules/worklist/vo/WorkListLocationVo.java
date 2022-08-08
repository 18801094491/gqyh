package com.zcdy.dsc.modules.worklist.vo;

import com.zcdy.dsc.common.util.DateAgoFormat;
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
public class WorkListLocationVo
{
    /**经度*/
    @ApiModelProperty(value = "经度")
    private String longitude;
    /**纬度*/
    @ApiModelProperty(value = "纬度")
    private String latitude;
    /**时间戳*/
    @ApiModelProperty(value = "时间戳")
    private Date dateTime;
    /**用户名*/
    @ApiModelProperty(value = "用户名")
    private String realname;

    /**
     * 返回几秒前、几分钟前格式
     * @return
     */
    public String getDateTimeFormat()
    {
        return DateAgoFormat.format(dateTime);
    }
}
