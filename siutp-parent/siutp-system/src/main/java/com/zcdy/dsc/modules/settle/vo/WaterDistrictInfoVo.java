package com.zcdy.dsc.modules.settle.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WaterDistrictInfoVo implements Serializable {
    /**
     * 水表编号
     */

    public String equipmentSn;

    /**
     * 水表位置
     */
    public String equipmentLocation;

}
