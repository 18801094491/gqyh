package com.zcdy.dsc.modules.centre.vo;

import com.zcdy.dsc.modules.centre.entity.CentreTree;
import com.zcdy.dsc.modules.centre.entity.OptAttrTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: 树形结构前端显示vo
 * @Author: 在信汇通
 * @Date:   2021-02-20
 * @Version: V1.0
 */
@Data
@ApiModel(value="树形结构前端显示vo", description="树形结构前端显示vo")
public class TreeListVo
{
    /**树、节点列表*/
    @ApiModelProperty(value = "树、节点列表")
    private List<CentreTree> treeList;
    /**绑定的属性*/
    @ApiModelProperty(value = "绑定的属性")
    private OptAttrTree optAttrTree;

    @ApiModelProperty(value = "挂接在节点上的属性列表")
    private List<AttrTreeVo> attrTreeVoList;
}
