package com.zcdy.dsc.modules.collection.iot.vo;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 监控工况设备信息
 * 
 * @author songguang.jiao
 * @date 2020/05/21 15:20:56
 */
@Getter
@Setter
@ApiModel(value = "监控工况设备信息")
public class OperateEquipInfomation {

    @ApiModelProperty(value="已经绑定的id列表")
    private List<Object> ids;
    
    
    @ApiModelProperty(value="详细设备信息")
    private IPage<OperateEquipInfoVo> operateEquipInfoVo;

    @Getter
    @Setter
    @ApiModel(value="详细设备信息")
    public static class OperateEquipInfoVo {
        @ApiModelProperty(value = "设备名称")
        private String equipName;

        @ApiModelProperty(value = "简称")
        private String abbreviation;

        @ApiModelProperty(value = "id")
        private String equipId;

        @ApiModelProperty(value = "状态 1-选中,0-未选中")
        private String selectStatus;
    }
}
