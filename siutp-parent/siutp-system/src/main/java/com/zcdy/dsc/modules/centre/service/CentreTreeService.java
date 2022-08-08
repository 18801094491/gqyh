package com.zcdy.dsc.modules.centre.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.centre.entity.CentreTree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.centre.param.CustQueryParam;
import com.zcdy.dsc.modules.centre.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentQueryVO;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.settle.vo.CustomerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 四大中心树形结构
 * @Author: 在信汇通
 * @Date:   2021-02-20
 * @Version: V1.0
 */
public interface CentreTreeService extends IService<CentreTree> {

    /**
     * 通过id查询
     * @param centreTree
     * @return
     */
    CentreTree getById(CentreTree centreTree);

    /**
     * 查询树形列表
     * @param centreTree
     * @return
     */
    List<CentreTree> getTreeList(CentreTree centreTree);

    /**
     * 查询根节点
     * @param centreTree
     * @return
     */
    List<CentreTree> getRootTreeList(CentreTree centreTree);

    /**
     * 添加
     * @param centreTree
     * @return
     */
    boolean add(CentreTree centreTree);

    /**
     * 添加根节点
     * @param centreTree
     * @return
     */
    boolean addRoot(CentreTree centreTree);

    /**
     * 获取同一中心+对象的根节点数量
     * @param centreTree
     * @return
     */
    Integer getRootNumByCentreObjId(CentreTree centreTree);

    /**
     * 获取使用某个对象的树的数量
     * @param objId 对象类型id
     * @return
     */
    Integer getRootTreeNumByObjId(String objId);

    /**
     * 获取使用此树节点的对象的数量总和
     * 数据来自：子节点、与对象的关系、与属性的关系
     * @param treeId
     * @return
     */
    Integer getObjNum4UseTreeId(String treeId);

    /**
     * 只更新名称
     * @param centreTree
     * @return
     */
    int updateNameById(CentreTree centreTree);

    /**
     * 获取整棵树
     * @param centreTree
     * @return
     */
    List<CentreTree> getAllTreeList(CentreTree centreTree);

    /**
     * 查询设备列表
     * @param page
     * @param param
     * @return
     */
    IPage<OptEquipmentModel> getEquPageListByParam(Page<OptEquipmentModel> page, EquipmentQueryParam param);

    /**
     * 查询客户列表
     * @param page
     * @param param
     * @return
     */
    IPage<CustomerVo> selectCustPageListByParam(Page<CustomerVo> page, CustQueryParam param);

    /**
     * 获取绑定在某个节点下的所有设备，不分页
     * @param equipmentQueryParam
     * @return
     */
    List<OptEquipmentModel> getEquListByParentIdNoPage(EquipmentQueryParam equipmentQueryParam);

    /**
     * 获取绑定在某个节点下的所有客户信息，不分页
     * @param param
     * @return
     */
    List<CustomerVo> getCustListByParentIdNoPage(CustQueryParam param);

    /**
     * 使用树节点、绑定属性、页面查询条件，联合查询设备列表
     * @param page
     * @param param
     * @param attr
     * @return
     */
    IPage<OptEquipmentModel> getEquListByAllSearch(Page<OptEquipmentModel> page, EquipmentQueryParam param, OptEquipmentModel attr);

    /**
     * 使用树节点、绑定属性、页面查询条件，联合查询客户列表
     * @param page
     * @param param
     * @param attr
     * @return
     */
    IPage<CustomerVo> getCustListByAllSearch(Page<CustomerVo> page, CustQueryParam param, CustomerVo attr);
}
