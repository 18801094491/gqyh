package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 班组人员树
 * @author songguang.jiao
 * @date 2020/7/2 17:13
 */
@Getter
@Setter
@ApiOperation("WorkTeamTree")
public class WorkTeamTree {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("父id")
    private String parentId;

    @ApiModelProperty("是否叶子节点")
    private boolean isLeaf;

    @ApiModelProperty("数据")
    private List<WorkTeamTree> data;
}
