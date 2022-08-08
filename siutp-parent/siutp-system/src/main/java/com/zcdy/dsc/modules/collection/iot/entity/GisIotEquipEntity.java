 package com.zcdy.dsc.modules.collection.iot.entity;

import lombok.Getter;
import lombok.Setter;

/**
  * GIS,IOT,资产信息
  * @author songguang.jiao
  * @date 2020/05/22 09:44:37
  */
 @Getter
 @Setter
 public class GisIotEquipEntity {

     /**
      * 采集id
      */
     private String iotId;
     
     /**
      * gisId
      */
     private String gisId;
     
     /**
      * 资产id
      */
     private String equipId;
    
     /**
      * 资产图片路径
      */
     private String equipImg;
     
     /**
      * 资产名称
      */
     private String equipName;
     
}
