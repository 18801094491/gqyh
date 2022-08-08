package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.List;

/**
 * @author : songguang.jiao
 * @param <T>
 */
@Getter
@Setter
public class TreeVo<T>  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * pid
     */
    @ApiModelProperty(value = "pid")
    private String pid;

    /**
     * key
     */
    @Excel(name = "key")
    @ApiModelProperty(value = "key")
    private String key;

    /**
     * key
     */
    @Excel(name = "value")
    @ApiModelProperty(value = "value")
    private String value;

    /**
     * 区域名称
     */
    @Excel(name = "显示值", width = 15)
    @ApiModelProperty(value = "显示值")
    private String title;

    /**
     * 是否有子节点
     */
    @Excel(name = "是否有子节点", width = 15)
    @ApiModelProperty(value = "是否有子节点")
    private Boolean isLeaf;

    private List<T> children;
    /**
     * 是否有子节点
     */
    @Excel(name = "是否扩展", width = 15)
    @ApiModelProperty(value = "是否扩展")
    private Boolean expanded;

}
