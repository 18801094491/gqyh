package com.zcdy.dsc.modules.centre.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.centre.entity.CentreTree;
import com.zcdy.dsc.modules.centre.mapper.CentreTreeMapper;
import com.zcdy.dsc.modules.centre.param.CustQueryParam;
import com.zcdy.dsc.modules.centre.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.centre.service.CentreTreeService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.settle.vo.CustomerVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 四大中心树形结构
 * @Author: 在信汇通
 * @Date:   2021-02-20
 * @Version: V1.0
 */
@Service
public class CentreTreeServiceImpl extends ServiceImpl<CentreTreeMapper, CentreTree> implements CentreTreeService {

    @Override
    public CentreTree getById(CentreTree centreTree) {
        return baseMapper.getById(centreTree);
    }

    @Override
    public List<CentreTree> getTreeList(CentreTree centreTree) {
        return baseMapper.getTreeList(centreTree);
    }

    @Override
    public List<CentreTree> getRootTreeList(CentreTree centreTree) {
        return baseMapper.getRootTreeList(centreTree);
    }

    @Override
    public boolean add(CentreTree centreTree) {
        return baseMapper.add(centreTree) > 0;
    }

    @Override
    public boolean addRoot(CentreTree centreTree) {
        return baseMapper.addRoot(centreTree) > 0;
    }

    @Override
    public Integer getRootNumByCentreObjId(CentreTree centreTree)
    {
        return baseMapper.getRootNumByCentreObjId(centreTree);
    }

    @Override
    public Integer getRootTreeNumByObjId(String objId) {
        return baseMapper.getRootTreeNumByObjId(objId);
    }

    @Override
    public Integer getObjNum4UseTreeId(String treeId) {
        return baseMapper.getObjNum4UseTreeId(treeId);
    }

    @Override
    public int updateNameById(CentreTree centreTree) {
        return baseMapper.updateNameById(centreTree);
    }

    @Override
    public List<CentreTree> getAllTreeList(CentreTree centreTree) {
        return baseMapper.getAllTreeList(centreTree);
    }

    @Override
    public IPage<OptEquipmentModel> getEquPageListByParam(Page<OptEquipmentModel> page, EquipmentQueryParam param) {
        return baseMapper.selectEquPageListByParam(page, param);
    }

    @Override
    public IPage<CustomerVo> selectCustPageListByParam(Page<CustomerVo> page, CustQueryParam param) {
        return baseMapper.selectCustPageListByParam(page, param);
    }

    @Override
    public List<OptEquipmentModel> getEquListByParentIdNoPage(EquipmentQueryParam equipmentQueryParam) {
        return baseMapper.getEquListByParentIdNoPage(equipmentQueryParam);
    }

    @Override
    public List<CustomerVo> getCustListByParentIdNoPage(CustQueryParam param) {
        return baseMapper.getCustListByParentIdNoPage(param);
    }

    @Override
    public IPage<OptEquipmentModel> getEquListByAllSearch(Page<OptEquipmentModel> page, EquipmentQueryParam param, OptEquipmentModel attr) {
        return baseMapper.getEquListByAllSearch(page, param, attr);
    }

    @Override
    public IPage<CustomerVo> getCustListByAllSearch(Page<CustomerVo> page, CustQueryParam param, CustomerVo attr) {
        return baseMapper.getCustListByAllSearch(page, param, attr);
    }
}
