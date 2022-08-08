package com.zcdy.dsc.modules.centre.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.modules.centre.constant.CentreTreeConstant;
import com.zcdy.dsc.modules.centre.constant.CentreTypeEnum;
import com.zcdy.dsc.modules.centre.entity.CentreTree;
import com.zcdy.dsc.modules.centre.entity.OptObjTree;
import com.zcdy.dsc.modules.centre.param.CentreTreeParam;
import com.zcdy.dsc.modules.centre.param.CustQueryParam;
import com.zcdy.dsc.modules.centre.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.centre.vo.TreeListVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.settle.vo.CustomerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @Description: 计量中心-客户树形结构
* @Author: 在信汇通
* @Date:   2021-02-20 11:33:45
* @Version: V1.0
*/
@Slf4j
@Api(tags="计量中心-客户树形结构")
@RestController
@RequestMapping("/centre/met/cust/")
public class CentreTreeMetCustController extends CentreTreeBaseController {

   private Logger logger = LoggerFactory.getLogger(getClass());

   /**
    * 数据类型
    */
   private final static String treeCentreType = CentreTypeEnum.MET.getValue();
   /**
    * 对象类型
    */
   private final static String treeObjectType = CentreTreeConstant.OBJ_TYPE_CUST;

   /**
    * 子节点及绑定属性查询，异步
    *
    * @param centreTreeParam
    * @param req
    * @return
    */
   @AutoLog(value = "计量中心-客户树形结构-子节点及绑定属性查询")
   @ApiOperation(value="计量中心-客户树形结构-子节点及绑定属性查询", notes="计量中心-客户树形结构-子节点及绑定属性查询")
   @GetMapping(value = "/list/")
   public ResultData<TreeListVo> queryList(CentreTreeParam centreTreeParam,
                                           HttpServletRequest req) {
       centreTreeParam.setCentre(treeCentreType);//设置中心类型
       centreTreeParam.setObjType(treeObjectType);//设置数据类型
       return super.queryTreeListAndAttr(centreTreeParam, req);
   }

   /**
    * 获取全部树及节点
    *
    * @param centreTreeParam
    * @param req
    * @return
    */
   @AutoLog(value = "计量中心-客户树形结构-获取全部树及节点-用于选择父节点")
   @ApiOperation(value="计量中心-客户树形结构-获取全部树及节点-用于选择父节点", notes="计量中心-客户树形结构-获取全部树及节点-用于选择父节点")
   @GetMapping(value = "/getAll/")
   public ResultData<List<CentreTree>> getAll(CentreTreeParam centreTreeParam,
                                                  HttpServletRequest req) {
       centreTreeParam.setCentre(treeCentreType);//设置中心类型
       centreTreeParam.setObjType(treeObjectType);//设置数据类型
       return super.getAllTreeList(centreTreeParam, req);
   }

   /**
    * 添加
    * @param centreTree
    * @return
    */
   @AutoLog(value = "计量中心-客户树形结构-添加")
   @ApiOperation(value="计量中心-客户树形结构-添加", notes="计量中心-客户树形结构-添加")
   @PostMapping(value = "/add")
   public ResultData<?> add(@RequestBody CentreTree centreTree, HttpServletRequest req) {
       centreTree.setCentre(treeCentreType);
       centreTree.setObjType(treeObjectType);//设置数据类型
       return super.add(centreTree, req);
   }

   /**
    * 编辑
    * @param centreTree
    * @return
    */
   @AutoLog(value = "计量中心-客户树形结构-编辑")
   @ApiOperation(value="计量中心-客户树形结构-编辑", notes="计量中心-客户树形结构-编辑")
   @PostMapping(value = "/edit")
   public ResultData<?> edit(@RequestBody CentreTree centreTree, HttpServletRequest req) {
       centreTree.setCentre(treeCentreType);
       centreTree.setObjType(treeObjectType);//设置数据类型
       return super.edit(centreTree, req);
   }

   /**
    * 通过id删除
    * @param id
    * @return
    */
   @AutoLog(value = "计量中心-客户树形结构-通过id删除")
   @ApiOperation(value="计量中心-客户树形结构-通过id删除", notes="计量中心-客户树形结构-通过id删除")
   @DeleteMapping(value = "/delete")
   public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
       CentreTree centreTree = new CentreTree();
       centreTree.setId(id);
       return super.deleteById(centreTree, req);
   }

   /**
    * 通过id查询
    * @param id
    * @return
    */
   @AutoLog(value = "计量中心-客户树形结构-通过id查询")
   @ApiOperation(value="计量中心-客户树形结构-通过id查询", notes="计量中心-客户树形结构-通过id查询")
   @GetMapping(value = "/query")
   public ResultData<?> queryById(@RequestParam("id") String id) {
       CentreTree centreTree = new CentreTree();
       centreTree.setId(id);
       return super.queryById(centreTree);
   }

   @AutoLog(value="树与客户-绑定")
   @ApiOperation(value="树与客户-绑定",notes="树与客户-绑定")
   @PostMapping("/treeobj/bind/")
   public ResultData<?> treeObjBind(@RequestBody OptObjTree optObjTree, HttpServletRequest req) {
       optObjTree.setCentre(treeCentreType);
       optObjTree.setObjType(treeObjectType);
       return super.treeObjBind(optObjTree, req);
   }

   @AutoLog(value="树与客户-解除绑定")
   @ApiOperation(value="树与客户-解除绑定",notes="树与客户-解除绑定")
   @PostMapping("/treeobj/unbind/")
   public ResultData<?> treeObjUnbind(@RequestBody OptObjTree optObjTree, HttpServletRequest req) {
       optObjTree.setCentre(treeCentreType);
       optObjTree.setObjType(treeObjectType);
       return super.treeObjUnbind(optObjTree, req);
   }

   /**
    * 客户分页列表查询
    * @param param
    * @return
    */
   @AutoLog(value = "客户信息-分页列表查询")
   @ApiOperation(value = "客户信息-分页列表查询", notes = "客户信息-分页列表查询")
   @GetMapping(value = "/custlist")
   public ResultData<IPage<CustomerVo>> queryCustPageList(CustQueryParam param) {
       param.setCentre(treeCentreType);
       param.setObjType(treeObjectType);
       return super.queryCustPageList(param);
   }

   @AutoLog(value = "客户信息-根据parentid查询节点和属性")
   @ApiOperation(value = "客户信息-根据parentid查询节点和属性", notes = "客户信息-根据parentid查询节点和属性")
   @GetMapping(value = "/getTreeCustAttrList")
   public ResultData<TreeListVo> getTreeCustAttrList(CustQueryParam param) {
       param.setCentre(treeCentreType);
       param.setObjType(treeObjectType);
       return super.getTreeCustAttrList(param);
   }

   @AutoLog(value = "客户信息-根据页面节点、条件、属性查询设备列表")
   @ApiOperation(value = "客户信息-根据页面节点、条件、属性查询设备列表", notes = "客户信息-根据页面节点、条件、属性查询设备列表")
   @GetMapping(value = "/getCustListByAllSearch")
   public ResultData<?> getCustListByAllSearch(CustQueryParam param) {
       param.setCentre(treeCentreType);
       param.setObjType(treeObjectType);
       return super.getCustListByAllSearch(param);
   }
}