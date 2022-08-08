package com.zcdy.dsc.modules.settle.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.settle.entity.District;
import com.zcdy.dsc.modules.settle.mapper.DistrictMapper;
import com.zcdy.dsc.modules.settle.service.DistrictService;
import com.zcdy.dsc.modules.settle.vo.DistrictTreeVo;
import com.zcdy.dsc.modules.settle.vo.DistrictWaterCustomerInfoVo;
import com.zcdy.dsc.modules.system.service.ISysCategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 区域配置
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-04-20
 * @Version: V1.0
 */
@Service
public class DistrictServiceImpl extends ServiceImpl<DistrictMapper, District> implements DistrictService {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	/**
	 * parentId 父节点
	 */
	public List<DistrictTreeVo> getTreeByParentId(String id) {
		return baseMapper.getTreeByParentId(id);
	}

	@Override
	/**
	 * parentId 父节点
	 */
	public List<DistrictTreeVo> getTreeById(String id) {
		return baseMapper.getTreeById(id);
	}

	@Override
	/**
	 * @Author: tangchao 区域新增 新增后父节点haschild更新为1 新增节点haschild 为0,
	 *          parents_ids=父节点parents_ids + 本节点ID
	 */
	public void addDistrict(District district) {
		updateParent(district);
		district.setHasChild("0");
		district.setDelFlag(DelFlagConstant.NORMAL);
		baseMapper.insert(district);
	}

	@Override
	/**
	 * 通过id删除某一个区域 删除当前区域和区域下所有子节点.(逻辑删除) 删除当前区域和区域下所有子节点的用户对应表(物理删除)
	 */
	public void removeDistrict(String id) {
		// 1. 删除当前区域和区域下所有子节点
		District district = baseMapper.selectById(id);
		QueryWrapper<District> query = new QueryWrapper<>();
		SFunction<District, String> getParentIds = District::getParentIds;
		query.lambda().likeRight(getParentIds, concatParentIds(district)).eq(District::getDelFlag, 0);
		List<District> districts = baseMapper.selectList(query);
		districts.add(district);
		for (District d : districts) {
			d.setDelFlag(DelFlagConstant.DELETED);
		}
		this.updateBatchById(districts);
		District parent = baseMapper.selectById(district.getParentId());
		parent.setHasChild(boolToString(hasChild(district.getParentId())));
		// 更新父节点hasFlag 字段
		baseMapper.updateById(parent);
	}

	@Override
	/**
	 * 更新 1.更新 parent_id parent_ids 子节点parent_ids
	 */
	public void updateDistrict(District district) {
		district.setDelFlag(DelFlagConstant.NORMAL);
		baseMapper.updateById(district);
	}

	@Override
	public List<DistrictTreeVo> searchBy(String keyWord) {
		List<DistrictTreeVo> treeByKeyword = this.baseMapper.getTreeByKeyword(keyWord);
		if (treeByKeyword.size() == 0) {
			return treeByKeyword;
		}
		DistrictTreeVo root = null;
		for (DistrictTreeVo dtv : treeByKeyword) {
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
	public void constructTree(List<DistrictTreeVo> tree, DistrictTreeVo pNode) {
		pNode.setChildren(new ArrayList<>());
		for (int i = 0; i < tree.size(); i++) {
			DistrictTreeVo dtv = tree.get(i);
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
			List<DistrictTreeVo> treeByParentId = this.baseMapper.getTreeByParentId(pNode.getId());
			pNode.setChildren(treeByParentId);
		} else {// 默认展开
			pNode.setExpanded(true);
		}
	}

	@Override
	/**
	 * @Auth chaotang 通过区域查询区域下所有的水表,客户,合同等信息
	 */
	public List<DistrictWaterCustomerInfoVo> queryWaterAndCustomerInfoByDistrictId(String districtId) {
		District district = this.baseMapper.selectById(districtId);
		if (district == null) {
			throw new IllegalArgumentException("区域不存在!");
		}
		Map<String, String> param = new HashMap<>(3);
		param.put("districtId", districtId);
		param.put("districtParentIds", concatParentIds(district));
		param.put("now", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
		List<DistrictWaterCustomerInfoVo> districtWaterCustomerInfoVos = this.baseMapper
				.queryWaterAndCustomerInfoByDistrictId(param);
		if (districtWaterCustomerInfoVos == null) {
			return new ArrayList<>();
		}
		for (DistrictWaterCustomerInfoVo d : districtWaterCustomerInfoVos) {
			String rData = stringRedisTemplate.opsForValue()
					.get(String.format(RedisKeyConstantV2.REDISDATAKEY, d.getVariableName()));
			if (StringUtils.isNotBlank(rData)) {
				ValueEntity value = JSON.parseObject(rData, ValueEntity.class);
				d.setCurrentDayFlow(value.getValue());
			}
		}

		return districtWaterCustomerInfoVos;
	}

	/**
	 * 更新节点父节点状态
	 * 
	 * @param district
	 */
	private void updateParent(District district) {
		String parentId = district.getParentId();
		// 根节点
		if (ConvertUtils.isEmpty(parentId)) {
			parentId = ISysCategoryService.ROOT_PID_VALUE;
			district.setParentId(parentId);
		}
		// 更新父节点状态
		District parent = baseMapper.selectById(parentId);
		if (parent == null) {
			throw new IllegalArgumentException("上级节点不正确.");
		}
		if (!"1".equals(parent.getHasChild())) {
			parent.setHasChild("1");
			baseMapper.updateById(parent);
		}
		district.setParentIds(concatParentIds(parent));
	}

	private boolean hasChild(String id) {
		QueryWrapper<District> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(District::getParentId, id).eq(District::getDelFlag, 0);
		List<District> districts = baseMapper.selectList(queryWrapper);
		return districts.size() != 0;
	}

	private String concatParentIds(District district) {
		return district.getParentIds() + district.getId() + ",";
	}

	private String boolToString(boolean condition) {
		return condition ? "1" : "0";
	}
}
