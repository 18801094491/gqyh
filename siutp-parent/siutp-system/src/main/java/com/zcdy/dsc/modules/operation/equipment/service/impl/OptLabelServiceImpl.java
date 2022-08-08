package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.OptLabel;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptLabelMapper;
import com.zcdy.dsc.modules.operation.equipment.service.OptLabelService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptLabelTreeVo;
import com.zcdy.dsc.modules.system.service.ISysCategoryService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 区域配置
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-04-20
 * @Version: V1.0
 */
@Service
public class OptLabelServiceImpl extends ServiceImpl<OptLabelMapper, OptLabel> implements OptLabelService {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	/**
	 * parentId 父节点
	 */
	public List<OptLabelTreeVo> getTreeByParentId(String id) {
		return baseMapper.getTreeByParentId(id);
	}

	@Override
	/**
	 * parentId 父节点
	 */
	public List<OptLabelTreeVo> getTreeById(String id) {
		return baseMapper.getTreeById(id);
	}

	@Override
	/**
	 * @Author: tangchao 区域新增 新增后父节点haschild更新为1 新增节点haschild 为0,
	 *          parents_ids=父节点parents_ids + 本节点ID
	 */
	public void addOptLabel(OptLabel optLabel) {
		updateParent(optLabel);
		optLabel.setHasChild("0");
		optLabel.setDelFlag(DelFlagConstant.NORMAL);
		baseMapper.insert(optLabel);
	}

	@Override
	/**
	 * 通过id删除某一个区域 删除当前区域和区域下所有子节点.(逻辑删除) 删除当前区域和区域下所有子节点的用户对应表(物理删除)
	 */
	public void removeOptLabel(String id) {
		// 1. 删除当前区域和区域下所有子节点
		OptLabel optLabel = baseMapper.selectById(id);
		QueryWrapper<OptLabel> query = new QueryWrapper<>();
		SFunction<OptLabel, String> getParentIds = OptLabel::getParentIds;
		query.lambda().likeRight(getParentIds, concatParentIds(optLabel)).eq(OptLabel::getDelFlag, 0);
		List<OptLabel> optLabels = baseMapper.selectList(query);
		optLabels.add(optLabel);
		for (OptLabel d : optLabels) {
			d.setDelFlag(DelFlagConstant.DELETED);
		}
		this.updateBatchById(optLabels);
		OptLabel parent = baseMapper.selectById(optLabel.getParentId());
		parent.setHasChild(boolToString(hasChild(optLabel.getParentId())));
		// 更新父节点hasFlag 字段
		baseMapper.updateById(parent);
	}

	@Override
	/**
	 * 更新 1.更新 parent_id parent_ids 子节点parent_ids
	 */
	public void updateOptLabel(OptLabel optLabel) {
		optLabel.setDelFlag(DelFlagConstant.NORMAL);
		baseMapper.updateById(optLabel);
	}

	@Override
	public List<OptLabelTreeVo> searchBy(String keyWord) {
		List<OptLabelTreeVo> treeByKeyword = this.baseMapper.getTreeByKeyword(keyWord);
		if (treeByKeyword.size() == 0) {
			return treeByKeyword;
		}
		OptLabelTreeVo root = null;
		for (OptLabelTreeVo dtv : treeByKeyword) {
			if (dtv.getId().equals(ISysCategoryService.ROOT_PID_VALUE)) {
				root = dtv;
				treeByKeyword.remove(dtv);
				break;
			}
		}
		if (root == null) {
			throw new IllegalArgumentException("数据错误, 没有根节点");
		}
		constructTree(treeByKeyword, root);
		// 孤节点 和 树节点
		treeByKeyword.add(root);
		return treeByKeyword;
	}

	/**
	 * 根据list 构造树
	 * 
	 * @param tree
	 * @param pNode
	 */
	public void constructTree(List<OptLabelTreeVo> tree, OptLabelTreeVo pNode) {
		pNode.setChildren(new ArrayList<>());
		for (int i = 0; i < tree.size(); i++) {
			OptLabelTreeVo dtv = tree.get(i);
			if (pNode.getId().equals(dtv.getPid())) {
				pNode.getChildren().add(dtv);
				tree.remove(dtv);
				i--;
				// 递归构建树
				constructTree(tree, dtv);
			}
		}
		// 如果没有子节点添加并且数据库中有子节点
		if (pNode.getChildren().size() == 0 && !pNode.getIsLeaf()) {
			List<OptLabelTreeVo> treeByParentId = this.baseMapper.getTreeByParentId(pNode.getId());
			pNode.setChildren(treeByParentId);
		} else {// 默认展开
			pNode.setExpanded(true);
		}
	}

	/**
	 * 更新节点父节点状态
	 * 
	 * @param optLabel
	 */
	private void updateParent(OptLabel optLabel) {
		String parentId = optLabel.getParentId();
		// 根节点
		if (ConvertUtils.isEmpty(parentId)) {
			parentId = ISysCategoryService.ROOT_PID_VALUE;
			optLabel.setParentId(parentId);
		}
		// 更新父节点状态
		OptLabel parent = baseMapper.selectById(parentId);
		if (parent == null) {
			throw new IllegalArgumentException("上级节点不正确.");
		}
		if (!"1".equals(parent.getHasChild())) {
			parent.setHasChild("1");
			baseMapper.updateById(parent);
		}
		optLabel.setParentIds(concatParentIds(parent));
	}

	private boolean hasChild(String id) {
		QueryWrapper<OptLabel> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(OptLabel::getParentId, id).eq(OptLabel::getDelFlag, 0);
		List<OptLabel> optLabels = baseMapper.selectList(queryWrapper);
		return optLabels.size() != 0;
	}

	private String concatParentIds(OptLabel optLabel) {
		return optLabel.getParentIds() + optLabel.getId() + ",";
	}

	private String boolToString(boolean condition) {
		return condition ? "1" : "0";
	}
}
