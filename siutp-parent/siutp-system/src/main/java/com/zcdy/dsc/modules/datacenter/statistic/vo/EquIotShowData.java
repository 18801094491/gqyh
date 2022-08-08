 package com.zcdy.dsc.modules.datacenter.statistic.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

 /**
  * APP需要单独展示的数据跟所有采集数据
  * @author songguang.jiao
  * @date 2020/05/22 16:02:42
  */
 @Getter
 @Setter
 public class EquIotShowData {

     /**
      * 单独展示数据
      */
      private String showData;

      /**
       * 采集数据
       */
      List<EquIotDataVO> equIotDataVOS;

 }
