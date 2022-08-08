package com.zcdy.dsc.modules.centre.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * @Description: 挂接在树节点下的树形结构属性vo
 * @Author: 在信汇通
 * @Date:   2021-02-20
 * @Version: V1.0
 */
@Data
@ApiModel(value="树形结构和绑定的属性vo", description="树形结构和绑定的属性vo")
public class AttrTreeVo
{
    /**名称*/
    @ApiModelProperty(value = "名称，显示的名字")
    private java.lang.String attrValue;
    @ApiModelProperty(value = "属性名，代码中的名字")
    private String attrName;
    @ApiModelProperty(value = "上级属性名集合，逗号隔开")
    private String parentAttrNames;
    @ApiModelProperty(value = "上级属性值集合，逗号隔开")
    private String parentAttrValues;
    @ApiModelProperty(value = "所属树节点id")
    private String treeId;

    /**
     * 子节点集合
     */
    @ApiModelProperty(value = "子节点集合")
    private List<AttrTreeVo> children;

    /**
     * 子节点数量
     * @return
     */
    public Integer getChildrenNum()
    {
        if(children == null)
            return 0;
        return children.size();
    }

    /**
     * 是否叶子节点
     * @return
     */
    public Boolean isLeaf()
    {
        return getChildrenNum() == 0;
    }

    /**
     * 是否叶子节点，前台树形结构用
     * @return
     */
    public Boolean getIsLeaf()
    {
        return getChildrenNum() == 0;
    }

    /**
     * 前台树形结构用
     * @return
     */
    public String getKey()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    /**
     * 前台树形结构用
     * @return
     */
    public String getValue()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    /**
     * 前台树形结构用
     * @return
     */
    public String getTitle()
    {
        return attrValue;
    }
}
