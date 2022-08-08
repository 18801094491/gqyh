 package com.zcdy.dsc.modules.collection.iot.vo;

import java.util.List;

import com.zcdy.dsc.modules.collection.gis.vo.IotDataVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工况监控信息
 * @author songguang.jiao
 * @date 2020/05/21 16:37:05
 */
@Getter
@Setter
@ApiModel(value="工况监控信息")
 public class OperateDataInfo {

    @ApiModelProperty(value="设备图片")
    private String equipImg;
    
    @ApiModelProperty(value="设备名称")
    private String equipName;
    
    @ApiModelProperty(value="采集数据")
    private List<IotDataVO> datas;
    
}
