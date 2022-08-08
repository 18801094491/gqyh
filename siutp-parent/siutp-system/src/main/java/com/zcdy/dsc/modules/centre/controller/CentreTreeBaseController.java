package com.zcdy.dsc.modules.centre.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.centre.constant.CentreTreeConstant;
import com.zcdy.dsc.modules.centre.entity.CentreTree;
import com.zcdy.dsc.modules.centre.entity.OptAttrTree;
import com.zcdy.dsc.modules.centre.entity.OptObjTree;
import com.zcdy.dsc.modules.centre.param.CentreTreeParam;
import com.zcdy.dsc.modules.centre.param.CustQueryParam;
import com.zcdy.dsc.modules.centre.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.centre.service.CentreTreeService;
import com.zcdy.dsc.modules.centre.service.OptAttrTreeService;
import com.zcdy.dsc.modules.centre.service.OptObjTreeService;
import com.zcdy.dsc.modules.centre.utils.AttrOptUtil;
import com.zcdy.dsc.modules.centre.vo.AttrTreeVo;
import com.zcdy.dsc.modules.centre.vo.TreeListVo;
import com.zcdy.dsc.modules.objecttype.service.SysObjectService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.settle.vo.CustomerVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* @Description: 四大中心树形结构
* @Author: 在信汇通
* @Date:   2021-02-20 11:33:45
* @Version: V1.0
*/
@Slf4j
public class CentreTreeBaseController extends BaseController<CentreTree, CentreTreeService> {

    @Autowired
    protected CentreTreeService centreTreeService;

    @Autowired
    protected OptAttrTreeService optAttrTreeService;

    @Autowired
    protected SysObjectService sysObjectService;

    @Autowired
    protected OptObjTreeService optObjTreeService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 查询树形结构，异步
     * @param centreTreeParam
     * @param req
     * @return
     */
    public ResultData<TreeListVo> queryTreeListAndAttr(CentreTreeParam centreTreeParam, HttpServletRequest req)
    {
       CentreTree searchParam = new CentreTree(centreTreeParam);
       searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
       TreeListVo treeListVo = new TreeListVo();
       List<CentreTree> treeList = centreTreeService.getTreeList(searchParam);
       treeListVo.setTreeList(treeList);
       if(StringUtils.isNotBlank(searchParam.getParentId()))
       {
           //非根显示节点，则查询与之绑定的属性
           treeListVo.setOptAttrTree(optAttrTreeService.getAttrByTreeId(searchParam.getParentId()));
       }
       return ResultData.ok(treeListVo);
    }

    /**
     * 获取整棵树及所有节点
     * @param centreTreeParam
     * @param req
     * @return
     */
    public ResultData<List<CentreTree>> getAllTreeList(CentreTreeParam centreTreeParam, HttpServletRequest req)
    {
        CentreTree searchParam = new CentreTree(centreTreeParam);
        searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        List<CentreTree> treeList = centreTreeService.getAllTreeList(searchParam);
        List<CentreTree> list = new ArrayList<>();
        getTreeList(list, treeList, null);
        return ResultData.ok(list);
    }

    /**
     * 将数据库查询出的list整理成树形结构
     * @param treeList
     * @param metaList
     * @param temp
     */
    private void getTreeList(List<CentreTree> treeList, List<CentreTree> metaList, CentreTree temp) {
        for (CentreTree centreTree : metaList) {
            String tempPid = centreTree.getParentId();
            if (temp == null && ConvertUtils.isEmpty(tempPid)) {
                //一级
                treeList.add(centreTree);
                if (!centreTree.isLeaf()) {
                    getTreeList(treeList, metaList, centreTree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
                //temp的子节点
                List<CentreTree> children = temp.getChildren();
                if(children == null)
                    children = new ArrayList<>();
                children.add(centreTree);
                temp.setChildren(children);
                if (!centreTree.isLeaf()) {
                    getTreeList(treeList, metaList, centreTree);
                }
            }
        }
    }

    /**
    * 添加
    * @param centreTree
    * @return
    */
    public ResultData<?> add(CentreTree centreTree, HttpServletRequest req) {
        ResultData<CentreTree> resultData = new ResultData<CentreTree>();
        try {
            String username = JwtUtil.getUserNameByToken(req);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            centreTree.setId(uuid);
            centreTree.setCreateTime(new Date());
            centreTree.setCreateBy(username);
            boolean ok = centreTreeService.add(centreTree);
            if(ok){
                resultData.success("保存成功！");
            }
            else{
                resultData.error500("操作失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

    /**
    * 编辑
    * @param centreTree
    * @return
    */
    public ResultData<?> edit(CentreTree centreTree, HttpServletRequest req) {
        ResultData<CentreTree> resultData = new ResultData<CentreTree>();
        try {
            if (centreTree == null || centreTree.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            centreTree.setUpdateTime(new Date());
            centreTree.setUpdateBy(username);
            int i = centreTreeService.updateNameById(centreTree);//只允许修改名称
            if (i == 1) {
                resultData.success("编辑成功！");
            } else {
                resultData.error500("操作失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

    /**
     * 删除前验证是否允许操作
     * 允许true；不允许false
     * @param id
     * @return
     */
    private boolean checkBeforUpdateAndDel(String id)
    {
        Integer num = centreTreeService.getObjNum4UseTreeId(id);
        if(num > 0)
        {
            //已被对象引用
            return false;
        }
        else
        {
            //未被引用
            return true;
        }
    }

    /**
    * 通过id删除
    * @param centreTree
    * @return
    */
    public ResultData<?> deleteById(CentreTree centreTree, HttpServletRequest req) {
        ResultData<CentreTree> resultData = new ResultData<CentreTree>();
        if (centreTree == null || centreTree.getId() == null) {
            return ResultData.error("id不能为空");
        }
        if(!checkBeforUpdateAndDel(centreTree.getId()))
        {
            return ResultData.error("数据正在使用中，不能删除");
        }
        centreTree.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        centreTree.setUpdateTime(new Date());
        centreTree.setUpdateBy(username);

        boolean ok = centreTreeService.updateById(centreTree);//逻辑删除
        if (ok) {
            resultData.success("删除成功!");
        } else {
            resultData.error500("删除失败!");
        }
        return resultData;
    }

    /**
    * 通过id查询
    * @param centreTree
    * @return
    */
    public ResultData<?> queryById(CentreTree centreTree) {
        ResultData<CentreTree> resultData = new ResultData<CentreTree>();
        try {
            if(centreTree == null || ConvertUtils.isEmpty(centreTree.getId())) {
                resultData.error500("id不能为空");
                return resultData;
            }
            centreTree.setDelFlag(DelFlagConstant.NORMAL);
            centreTree = centreTreeService.getById(centreTree);
            resultData.setData(centreTree);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 绑定树与对象关系
     * @param optObjTree
     * @param req
     * @return
     */
    public ResultData<?> treeObjBind(OptObjTree optObjTree, HttpServletRequest req) {
        if(StringUtils.isBlank(optObjTree.getTreeId()))
        {
            return ResultData.error("树id不能为空");
        }
        if(StringUtils.isBlank(optObjTree.getDataId()))
        {
            return ResultData.error("对象id不能为空");
        }

        String username = JwtUtil.getUserNameByToken(req);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        optObjTree.setId(uuid);
        optObjTree.setCreateTime(new Date());
        optObjTree.setCreateBy(username);
        optObjTree.setDelFlag(DelFlagConstant.DELETED);
        optObjTree.setUpdateBy(username);
        optObjTree.setUpdateTime(new Date());
        int i = optObjTreeService.treeObjBind(optObjTree);
        if(i == 1)
        {
            return ResultData.ok();
        }
        else
        {
            return ResultData.error("操作失败");
        }
    }

    /**
     * 解除树与对象关系
     * @param optObjTree
     * @param req
     * @return
     */
    public ResultData<?> treeObjUnbind(OptObjTree optObjTree, HttpServletRequest req) {
        if(StringUtils.isBlank(optObjTree.getDataId()))
        {
            return ResultData.error("对象id不能为空");
        }

        String username = JwtUtil.getUserNameByToken(req);
        optObjTree.setDelFlag(DelFlagConstant.DELETED);
        optObjTree.setUpdateBy(username);
        optObjTree.setUpdateTime(new Date());
        int i = optObjTreeService.deleteTreeObjBind(optObjTree);
        if(i > 0)
        {
            return ResultData.ok();
        }
        else
        {
            return ResultData.error("操作失败");
        }
    }

    /**
     * 设备列表
     * @param param
     * @return
     */
    public ResultData<IPage<OptEquipmentModel>> queryEquPageList(EquipmentQueryParam param) {
        ResultData<IPage<OptEquipmentModel>> result = new ResultData<IPage<OptEquipmentModel>>();
        Page<OptEquipmentModel> page = new Page<OptEquipmentModel>(param.getPageNo(), param.getPageSize());
        IPage<OptEquipmentModel> list = centreTreeService.getEquPageListByParam(page, param);
        result.setData(list);
        result.success("查询成功");
        return result;
    }

    /**
     * 客户列表
     * @param param
     * @return
     */
    public ResultData<IPage<CustomerVo>> queryCustPageList(CustQueryParam param) {
        ResultData<IPage<CustomerVo>> result = new ResultData<>();
        Page<CustomerVo> page = new Page<CustomerVo>(param.getPageNo(), param.getPageSize());
        IPage<CustomerVo> list = centreTreeService.selectCustPageListByParam(page, param);
        result.setData(list);
        result.success("查询成功");
        return result;
    }

    /**
     * 信息设备-根据parentid查询节点和属性
     * @param param
     * @return
     */
    public ResultData<TreeListVo> getTreeEquAttrList(EquipmentQueryParam param) {
        //节点
        TreeListVo treeListVo = new TreeListVo();
        CentreTree centreTree = new CentreTree();
        centreTree.setCentre(param.getCentre());
        centreTree.setObjType(param.getObjType());
        centreTree.setParentId(param.getParentId());
        centreTree.setDelFlag(DelFlagConstant.NORMAL);
        List<CentreTree> treeList = centreTreeService.getTreeList(centreTree);
        treeListVo.setTreeList(treeList);
        OptAttrTree optAttrTree = optAttrTreeService.getAttrByTreeId(param.getParentId());//绑定的属性
        if(optAttrTree != null)
        {
            //属性不为空，获取绑定在当前节点的设备
            List<OptEquipmentModel> equipmentModelList = centreTreeService.getEquListByParentIdNoPage(param);
            //根据属性列表排序和设备属性，整合挂接在节点上的“节点”
            List<AttrTreeVo> attrTreeVoList = AttrOptUtil.attrInputCentreList(equipmentModelList, optAttrTree.getAttrs(), "", "", param.getParentId());
            treeListVo.setAttrTreeVoList(attrTreeVoList);
        }
        return ResultData.ok(treeListVo);
    }

    /**
     * 信息设备-根据parentid查询节点和属性
     * @param param
     * @return
     */
    public ResultData<TreeListVo> getTreeCustAttrList(CustQueryParam param) {
        //节点
        TreeListVo treeListVo = new TreeListVo();
        CentreTree centreTree = new CentreTree();
        centreTree.setCentre(param.getCentre());
        centreTree.setObjType(param.getObjType());
        centreTree.setParentId(param.getParentId());
        centreTree.setDelFlag(DelFlagConstant.NORMAL);
        List<CentreTree> treeList = centreTreeService.getTreeList(centreTree);
        treeListVo.setTreeList(treeList);
        OptAttrTree optAttrTree = optAttrTreeService.getAttrByTreeId(param.getParentId());//绑定的属性
        if(optAttrTree != null)
        {
            //属性不为空，获取绑定在当前节点的设备
            List<CustomerVo> custModelList = centreTreeService.getCustListByParentIdNoPage(param);
            //根据属性列表排序和设备属性，整合挂接在节点上的“节点”
            List<AttrTreeVo> attrTreeVoList = AttrOptUtil.attrInputCentreList(custModelList, optAttrTree.getAttrs(), "", "", param.getParentId());
            treeListVo.setAttrTreeVoList(attrTreeVoList);
        }
        return ResultData.ok(treeListVo);
    }

    /**
     * 使用树节点、绑定属性、页面查询条件，联合查询设备列表
     * @param param
     * @return
     */
    public ResultData<?> getEquListByAllSearch(EquipmentQueryParam param) {
        try {
            Page<OptEquipmentModel> page = new Page<OptEquipmentModel>(param.getPageNo(), param.getPageSize());
            //页面查询条件，无需处理
            //节点查询条件，支持父节点，支持多选
            String parentIds = param.getParentIds();
            if(StringUtils.isNotBlank(parentIds))
            {
                String[] parentId = parentIds.split(",");
                String parentIdsQueryStr = "";
                List<String> parentIdList = new ArrayList<>();
                parentIdsQueryStr = parentId[0];
                parentIdList.add(parentId[0]);
                for(int i = 1; i < parentId.length; i++)
                {
                    parentIdsQueryStr = parentIdsQueryStr + "|"  + parentId[i];
                    parentIdList.add(parentId[i]);
                }
                param.setParentIdsQueryStr(parentIdsQueryStr);
                param.setParentIdList(parentIdList);
            }
            //属性查询条件，属性可单选、多级，但父节点只能单一
            OptEquipmentModel equ = new OptEquipmentModel();
            String attrNames = param.getAttrNames();
            String attrValues = param.getAttrValues();
            if(StringUtils.isNotBlank(attrNames) && StringUtils.isNotBlank(attrValues))
            {
                String[] attrNameArray = attrNames.split(",");
                String[] attrValueArray = attrValues.split(",");
                if(attrNameArray.length != attrValueArray.length)
                {
                    return ResultData.error("属性查询参数错误！");
                }
                for(int i = 0; i < attrNameArray.length; i++)
                {
                    String fieldTypeName = PropertyUtils.getPropertyType(equ, attrNameArray[i]).getName();
                    if(fieldTypeName.equals("java.lang.String"))
                    {
                        PropertyUtils.setProperty(equ, attrNameArray[i], attrValueArray[i]);
                    }
                    else if(fieldTypeName.equals("java.util.Date"))
                    {
                        //如果属性是日期类型，要将String转为Date
                        if(CentreTreeConstant.Attr_BLANK_SHOW_TEXT.equals(attrValueArray[i]))
                        {
                            //如果Date类型在页面显示为“空值”，处理方法：将“空值”赋值给“date属性名+EmptyStr”的属性，以便mybatis拼接查询条件
                            PropertyUtils.setProperty(equ, attrNameArray[i] + CentreTreeConstant.ATTR_BLANK_EMPTY_STR, CentreTreeConstant.Attr_BLANK_SHOW_TEXT);
                        }
                        else
                        {
                            //不为空时，直接转换、赋值
                            Date dateValue = DateUtil.string2Date(attrValueArray[i], DateUtil.dateFormatStr);
                            PropertyUtils.setProperty(equ, attrNameArray[i], dateValue);
                        }
                    }

                }
            }
            IPage<OptEquipmentModel> list = centreTreeService.getEquListByAllSearch(page, param, equ);
            return ResultData.ok(list);
        } catch (Exception e) {
            logger.error("装载查询属性时出错", e);
            return ResultData.error("属性查询参数不匹配！");
        }
    }

    /**
     * 使用树节点、绑定属性、页面查询条件，联合查询客户列表
     * @param param
     * @return
     */
    public ResultData<?> getCustListByAllSearch(CustQueryParam param) {
        try {
            Page<CustomerVo> page = new Page<>(param.getPageNo(), param.getPageSize());
            //页面查询条件，无需处理
            //节点查询条件，支持父节点，支持多选
            String parentIds = param.getParentIds();
            if(StringUtils.isNotBlank(parentIds))
            {
                String[] parentId = parentIds.split(",");
                String parentIdsQueryStr = "";
                List<String> parentIdList = new ArrayList<>();
                parentIdsQueryStr = parentId[0];
                parentIdList.add(parentId[0]);
                for(int i = 1; i < parentId.length; i++)
                {
                    parentIdsQueryStr = parentIdsQueryStr + "|" +  parentId[i];
                    parentIdList.add(parentId[i]);
                }
                param.setParentIdsQueryStr(parentIdsQueryStr);
                param.setParentIdList(parentIdList);
            }
            //属性查询条件，属性可单选、多级，但父节点只能单一
            CustomerVo cust = new CustomerVo();
            String attrNames = param.getAttrNames();
            String attrValues = param.getAttrValues();
            if(StringUtils.isNotBlank(attrNames) && StringUtils.isNotBlank(attrValues))
            {
                String[] attrNameArray = attrNames.split(",");
                String[] attrValueArray = attrValues.split(",");
                if(attrNameArray.length != attrValueArray.length)
                {
                    return ResultData.error("属性查询参数错误！");
                }
                for(int i = 0; i < attrNameArray.length; i++)
                {
                    PropertyUtils.setProperty(cust, attrNameArray[i], attrValueArray[i]);
                }
            }
            IPage<CustomerVo> list = centreTreeService.getCustListByAllSearch(page, param, cust);
            return ResultData.ok(list);
        } catch (Exception e) {
            logger.error("装载查询属性时出错", e);
            return ResultData.error("属性查询参数不匹配！");
        }
    }
}