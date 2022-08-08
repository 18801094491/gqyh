package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by 柯昌年 on 2020/8/27
 * 地图数据展示封装
 */
@Getter
@Setter
@ApiModel(value = "GisDataVo", description = "gis模型数据展示封装")
public class GisDataVo {

    @ApiModelProperty("坐标点Id")
    private String gisId;

    @ApiModelProperty("坐标标签显示名称")
    private String title;

    @ApiModelProperty("坐标经度")
    private String latitude;

    @ApiModelProperty("坐标纬度")
    private String longitude;

    @ApiModelProperty("坐标显示的图片地址")
    private String imgUrl;

    @ApiModelProperty("坐标点设备状态")
    private String iotStatus;

    @ApiModelProperty("坐标点设备参数数值")
    private List<IotDataVO> iotDataVos;

    @ApiModelProperty("图片height大小")
    private Integer height;

    @ApiModelProperty("图片width大小")
    private Integer width;
}
