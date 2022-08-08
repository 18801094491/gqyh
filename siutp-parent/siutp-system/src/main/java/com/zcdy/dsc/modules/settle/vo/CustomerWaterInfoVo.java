package com.zcdy.dsc.modules.settle.vo;

import java.util.List;

import com.zcdy.dsc.modules.collection.gis.vo.IotDataVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 客户信息页面水表动态相关信息
 * @author：  songguang.jiao
 * 创建时间：  2020年1月6日 下午2:34:18
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "CustomerWaterInfoVo", description = "客户信息页面水表动态相关信息")
public class CustomerWaterInfoVo {

    //设备编号
    @ApiModelProperty(value = "id")
    private java.lang.String id;

    //设备编号
    @ApiModelProperty(value = "设备id")
    private java.lang.String equipmentId;

    //设备编号
    @ApiModelProperty(value = "设备编号")
    private java.lang.String equipmentSn;

    //设备名称
    @ApiModelProperty(value = "设备名称")
    private java.lang.String equipmentName;

    //客户水表编号
    @ApiModelProperty(value = "客户水表编号")
    private String customerWaterSn;

    //所在位置
    @ApiModelProperty(value = "所在位置")
    private String equipmentLocation;

    //设备采集数据
    @ApiModelProperty(value = "设备采集数据")
    private List<IotDataVO> equipData;

    //gis模型id
    @ApiModelProperty(value = "gis模型id")
    private String modelId;

    //绑定状态0-未绑定,1-已绑定
    @ApiModelProperty(value = "绑定状态,0-未绑定,1-已绑定")
    private String bindStatus;

    //绑定时间
    @ApiModelProperty(value = "绑定时间")
    private String bindTime;
    //解绑时间
    @ApiModelProperty(value = "解绑时间")
    private String unbindTime;

}
