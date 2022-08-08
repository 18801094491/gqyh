package com.zcdy.dsc.modules.collection.iot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipment;
import com.zcdy.dsc.modules.collection.iot.entity.IotProxy;
import com.zcdy.dsc.modules.collection.iot.entity.IotProxyEquipment;
import com.zcdy.dsc.modules.collection.iot.mapper.IotEquipmentMapper;
import com.zcdy.dsc.modules.collection.iot.mapper.IotProxyEquipmentMapper;
import com.zcdy.dsc.modules.collection.iot.mapper.IotProxyMapper;
import com.zcdy.dsc.modules.collection.iot.service.IotProxyService;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipVo;
import com.zcdy.dsc.modules.collection.iot.vo.IotProxyVo;

/**
 * 描述: 采集代理管理
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-03-13
 * 版本号: V1.0
 */
@Service
public class IotProxyServiceImpl extends ServiceImpl<IotProxyMapper, IotProxy> implements IotProxyService {

	@Autowired
	private IotProxyMapper iotProxyMapper;

	@Autowired
	private IotEquipmentMapper iotEquipmentMapper;

	@Autowired
	private IotProxyEquipmentMapper iotProxyEquipmentMapper;

	@Override
	public void editProxyStatus(String id) {
		iotProxyMapper.editProxyStatus(id);
	}

	@Override
	public List<IotProxyVo> queryData() {
		return iotProxyMapper.queryData();
	}

	@Override
	public IPage<IotEquipVo> queryAllEquip(Page<IotEquipVo> page, String iotSn, String iotName, String iotCategory) {
		return iotProxyMapper.queryAllEquip(page, iotSn, iotName, iotCategory);
	}

	@Override
	public IPage<IotEquipVo> getBindEquip(Page<IotEquipVo> page, String proxyId, String iotSn, String iotName,
			String iotCategory) {
		return iotProxyMapper.getBindEquip(page, proxyId, iotSn, iotName, iotCategory);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void bindAll(String proxyId) {
		// 删除现有的,在批量插入,每次插入500个
		QueryWrapper<IotProxyEquipment> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(IotProxyEquipment::getProxyId, proxyId);
		iotProxyEquipmentMapper.delete(wrapper);
		//查询所有采集设备id
		List<Object> objs = iotEquipmentMapper.selectObjs(new QueryWrapper<IotEquipment>().select("id"));
		List<IotProxyEquipment> list = new ArrayList<IotProxyEquipment>();
		for (Object object : objs) {
			IotProxyEquipment iotProxyEquipment=new IotProxyEquipment();
			iotProxyEquipment.setEquipId(object.toString());
			iotProxyEquipment.setProxyId(proxyId);
			iotProxyEquipment.setId(UUIDGenerator.generate());
			list.add(iotProxyEquipment);
			if(list.size()>=500){
				iotProxyMapper.saveBatch(list);
				list.clear();
			}
		}
		if(list.size()>0){
			iotProxyMapper.saveBatch(list);
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void bindCate(String category, String proxyId) {
		//查出所有这个类型设备id
		QueryWrapper<IotEquipment> queryWrapper = new QueryWrapper<IotEquipment>();
		queryWrapper.select("id").lambda().eq(IotEquipment::getIotCategory, category);
		List<Object> objs = iotEquipmentMapper.selectObjs(queryWrapper);
		
		//删除已经关联的
		if(objs.size()>0 && !objs.isEmpty()){
			QueryWrapper<IotProxyEquipment> wrapper=new QueryWrapper<>();
			wrapper.lambda().in(IotProxyEquipment::getEquipId, objs);
			iotProxyEquipmentMapper.delete(wrapper);
		}
		
		//重启插入这个类型的设备id
		List<IotProxyEquipment> list = new ArrayList<IotProxyEquipment>();
		for (Object object : objs) {
			IotProxyEquipment iotProxyEquipment=new IotProxyEquipment();
			iotProxyEquipment.setEquipId(object.toString());
			iotProxyEquipment.setProxyId(proxyId);
			iotProxyEquipment.setId(UUIDGenerator.generate());
			list.add(iotProxyEquipment);
			if(list.size()>=500){
				iotProxyMapper.saveBatch(list);
				list.clear();
			}
		}
		if(list.size()>0){
			iotProxyMapper.saveBatch(list);
		}
	}

}
