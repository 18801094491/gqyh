package com.zcdy.dsc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 海康综合安防监控点资源Vo类
 * @Author: 在信汇通
 * @Date: 2020-12-14 10:57:13
 * @Version: V1.0
 */
@ApiModel(value="ArtemisMonitorVo", description="海康综合安防监控点资源Vo类")
@Getter
@Setter
public class ArtemisMonitorVo {
    /* 设备唯一编码 */
    @ApiModelProperty(value = "设备唯一编码")
    private String indexCode;
    /*	资源类型，camera：监控点 */
    @ApiModelProperty(value = "资源类型，camera：监控点")
    private String resourceType;
    /* 设备名称 */
    @ApiModelProperty(value = "监控设备名称")
    private String name;
    /* 经度，精确到小数点后8位 */
    @ApiModelProperty(value = "经度，精确到小数点后8位")
    private String longitude;
    /* 纬度，精确到小数点后8位 */
    @ApiModelProperty(value = "纬度，精确到小数点后8位")
    private String latitude;
    /*
    * 监控点类型：
    *   0、枪机
    *   1、半球
    *   2、快球
    *   3、带云台枪机
    **/
    @ApiModelProperty(value = "监控点类型：0、枪机 1、半球 2、快球 3、带云台枪机")
    private Integer cameraType;
    /*
    * 能力集
    * V1.3.0详见附录A.22设备能力集：https://open.hikvision.com/docs/f1ab264b3c7b4786bcc5851d9f82360f#e0c61748
    * V1.4.1详见附录A.44设备能力集：https://open.hikvision.com/docs/d0b998b4501040e38dbc61c2878a6fba#e043207b
    * */
    @ApiModelProperty(value = "能力集\n" +
            "V1.3.0详见附录A.22设备能力集：https://open.hikvision.com/docs/f1ab264b3c7b4786bcc5851d9f82360f#e0c61748\n" +
            "V1.4.1详见附录A.44设备能力集：https://open.hikvision.com/docs/d0b998b4501040e38dbc61c2878a6fba#e043207b")
    private String capability;
    /* 所属区域路径，由唯一标示组成，最大10级，格式： @根节点@子区域1@子区域2@ */
    @ApiModelProperty(value = "所属区域路径，由唯一标示组成，最大10级，格式： @根节点@子区域1@子区域2@")
    private String regionName;
    /* 区域路径名称，"/"分隔 */
    @ApiModelProperty(value = "区域路径名称，/ 分隔")
    private String regionPathName;
    /* 创建时间，IOS8601格式 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间，IOS8601格式")
    private String createTime;

}
