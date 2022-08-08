package com.zcdy.dsc.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.exception.BusinessException;
import com.zcdy.dsc.common.util.SequenceUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.system.entity.SysCategory;
import com.zcdy.dsc.modules.system.mapper.SysCategoryMapper;
import com.zcdy.dsc.modules.system.model.TreeSelectModel;
import com.zcdy.dsc.modules.system.service.ISysCategoryService;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 描述: 分类字典
 @author : songguang.jiao
 
 * 版本号: V1.0
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements ISysCategoryService {

	@Override
	public void addSysCategory(SysCategory sysCategory) {
		String categoryCode = "";
		String categoryPid = ISysCategoryService.ROOT_PID_VALUE;
		String parentCode = null;
		if(ConvertUtils.isNotEmpty(sysCategory.getPid())){
			categoryPid = sysCategory.getPid();

			//PID 不是根节点 说明需要设置父节点 hasChild 为1
			if(!ISysCategoryService.ROOT_PID_VALUE.equals(categoryPid)){
				SysCategory parent = baseMapper.selectById(categoryPid);
				parentCode = parent.getCode();
				if(parent!=null && !"1".equals(parent.getHasChild())){
					parent.setHasChild("1");
					baseMapper.updateById(parent);
				}
			}
		}
		/*
		* 分成三种情况
		* 1.数据库无数据 调用YouBianCodeUtil.getNextYouBianCode(null);
		* 2.添加子节点，无兄弟元素 YouBianCodeUtil.getSubYouBianCode(parentCode,null);
		* 3.添加子节点有兄弟元素 YouBianCodeUtil.getNextYouBianCode(lastCode);
		* 找同类 确定上一个最大的code值
		* */
		LambdaQueryWrapper<SysCategory> query = new LambdaQueryWrapper<SysCategory>()
				.eq(SysCategory::getPid,categoryPid)
				.orderByDesc(SysCategory::getCode);
		List<SysCategory> list = baseMapper.selectList(query);
		if(list==null || list.size()==0){
			if(ISysCategoryService.ROOT_PID_VALUE.equals(categoryPid)){
				//情况1
				categoryCode = SequenceUtil.getNextYouBianCode(null);
			}else{
				//情况2
				categoryCode = SequenceUtil.getSubYouBianCode(parentCode,null);
			}
		}else{
			//情况3
			categoryCode = SequenceUtil.getNextYouBianCode(list.get(0).getCode());
		}
		sysCategory.setCode(categoryCode);
		sysCategory.setPid(categoryPid);
		baseMapper.insert(sysCategory);
	}
	
	@Override
	public void updateSysCategory(SysCategory sysCategory) {
		if(ConvertUtils.isEmpty(sysCategory.getPid())){
			sysCategory.setPid(ISysCategoryService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChild 为1
			SysCategory parent = baseMapper.selectById(sysCategory.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.updateById(sysCategory);
	}

	@Override
	public List<TreeSelectModel> queryListByCode(String pcode) throws BusinessException{
		String pid = ROOT_PID_VALUE;
		if(ConvertUtils.isNotEmpty(pcode)) {
			List<SysCategory> list = baseMapper.selectList(new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getCode, pcode));
			if(list==null || list.size() ==0) {
				throw new BusinessException("该编码【"+pcode+"】不存在，请核实!");
			}
			if(list.size()>1) {
				throw new BusinessException("该编码【"+pcode+"】存在多个，请核实!");
			}
			pid = list.get(0).getId();
		}
		return baseMapper.queryListByPid(pid,null);
	}


	@Override
	public List<TreeSelectModel> queryListByPid(String pid) {
		if(ConvertUtils.isEmpty(pid)) {
			pid = ROOT_PID_VALUE;
		}
		return baseMapper.queryListByPid(pid,null);
	}

	@Override
	public List<TreeSelectModel> queryListByPid(String pid, Map<String, String> condition) {
		if(ConvertUtils.isEmpty(pid)) {
			pid = ROOT_PID_VALUE;
		}
		return baseMapper.queryListByPid(pid,condition);
	}

	@Override
	public String queryIdByCode(String code) {
		return baseMapper.queryIdByCode(code);
	}

	@Override
	public List<SysCateDropdown> queryKeyValue(String pid) {
		return baseMapper.queryKeyValue(pid);
	}
	@Override
	public String  queryKeyValueCode(String pcode) {
        List<TreeSelectModel> list = baseMapper.queryKeyValueCode(pcode);
        if(list==null || list.size() ==0) {
			throw new BusinessException("该编码【"+pcode+"】不存在，请核实!");
		}
		if(list.size()>1) {
			throw new BusinessException("该编码【"+pcode+"】存在多个，请核实!");
		}
        return list.get(0).getKey();
	}

	@Override
	public List<SysCateDropdown> queryKeyValueByParentCode(String key) {
		return this.baseMapper.queryKeyValueByCode(key);
	}
}
