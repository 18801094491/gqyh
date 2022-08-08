package com.zcdy.dsc.modules.operation.equipment.entity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 仓库树形结构基础数据
 * @author： songguang.jiao
 * 创建时间： 2020年3月4日 下午3:18:48
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "StoreTree", description = "仓库树形结构基础数据")
public class StoreTree {

	// 树id
	private String treeId;

	// 树名称
	private String treeName;

	// 父级id
	private String pid;

	// 是否叶子节点: 1:是 0:不是
	private boolean isLeaf;
}
