 package com.zcdy.dsc.modules.collection.iot.vo;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author songguang.jiao
 * @date 2020/05/21 15:13:32
 */
@ApiModel(value="工况监控设备查询入参")
@Getter
@Setter
public class OperateEquipPageParam extends AbstractPageParam{

   @ApiModelProperty(value="查询参数")
   private String name;
   
   @ApiModelProperty(value="资产类型")
   private String equipType;
}
