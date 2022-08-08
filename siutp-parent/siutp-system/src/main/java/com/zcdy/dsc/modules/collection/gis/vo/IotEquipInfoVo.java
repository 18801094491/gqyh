 package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备信息
 * @author songguang.jiao
 * @date 2020/05/20 11:56:13
 */
@ApiModel(value="设备信息")
@Getter
@Setter
 public class IotEquipInfoVo {

     /**
      * 采集设备id
      */
     @ApiModelProperty("采集设备id")
     private String gisId;
     
     /**
      * 设备名称
      */
     @ApiModelProperty("设备名称")
     private String equipmentName;
     
     /**
      * 列表需要展示的采集数据
      */
     @ApiModelProperty("列表需要展示的采集数据")
     private String  showData;
     
}
