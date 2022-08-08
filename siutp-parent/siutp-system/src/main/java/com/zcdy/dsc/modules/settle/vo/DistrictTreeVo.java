package com.zcdy.dsc.modules.settle.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author : songguang.jiao
 */
@Getter
@Setter
public class DistrictTreeVo extends TreeVo<DistrictTreeVo> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void setIsLeaf(Boolean isLeaf){
        super.setIsLeaf(!isLeaf);
    }

}
