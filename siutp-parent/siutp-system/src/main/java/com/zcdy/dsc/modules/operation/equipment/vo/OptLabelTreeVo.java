package com.zcdy.dsc.modules.operation.equipment.vo;

import com.zcdy.dsc.modules.settle.vo.TreeVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author tangchao
 */
@Getter
@Setter
public class OptLabelTreeVo extends TreeVo<OptLabelTreeVo> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void setIsLeaf(Boolean isLeaf){
        super.setIsLeaf(!isLeaf);
    }

}
