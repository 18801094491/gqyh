package com.zcdy.dsc.modules.centre.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ClassUtil;
import com.zcdy.dsc.modules.centre.constant.CategoryConstant;
import com.zcdy.dsc.modules.centre.entity.OptAttrTree;
import com.zcdy.dsc.modules.centre.service.CentreTreeUniversalService;
import com.zcdy.dsc.modules.centre.service.OptAttrTreeService;
import com.zcdy.dsc.modules.centre.vo.CentreTreeObjectVo;
import com.zcdy.dsc.modules.objecttype.service.SysObjectService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.service.IOptSupplierService;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierListVo;
import com.zcdy.dsc.modules.system.service.ISysCategoryService;
import com.zcdy.dsc.modules.system.service.ISysDictService;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: 四大中心树形结构-通用接口
 * @Author: 在信汇通
 * @Date:   2021-02-20 11:33:45
 * @Version: V1.0
 */
@Slf4j
@Api(tags="中心树通用接口")
@RestController
@RequestMapping("/centre/universal/")
public class CentreTreeUniversalController
{
    @Autowired
    private CentreTreeUniversalService centreTreeUniversalService;

    @Autowired
    private OptAttrTreeService optAttrTreeService;

    @Autowired
    private SysObjectService sysObjectService;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private IOptSupplierService optSupplierService;

    @Autowired
    private ISysCategoryService sysCategoryService;
    /**
     * 根据节点id，获取能与之绑定的对象信息
     * @param treeId
     * @return
     */
    @AutoLog(value = "树与属性-获取要绑定的对象信息")
    @ApiOperation(value="树与属性-获取要绑定的对象信息", notes="树与属性-获取要绑定的对象信息")
    @GetMapping(value = "/getSysObj/")
    public ResultData<?> getSysObjectByTreeId(@RequestParam String treeId)
    {
        CentreTreeObjectVo object = sysObjectService.getSysObjectByTreeId(treeId);
        if(object == null)
        {
            return ResultData.error("未找到对应属性");
        }
        List<Map<String, String>> mapList = ClassUtil.getFieldsAndApiModelProperty(object.getSysObjectVo().getObjType());
        object.getSysObjectVo().setFieldList(mapList);
        return ResultData.ok(object);
    }

    /**
     * 绑定属性
     * @param optAttrTree
     * @param req
     * @return
     */
    @AutoLog(value = "树与属性-绑定属性")
    @ApiOperation(value="树与属性-绑定属性", notes="树与属性-绑定属性")
    @PostMapping(value = "/bind/add")
    public ResultData<?> bindAttr(@RequestBody OptAttrTree optAttrTree, HttpServletRequest req)
    {
        OptAttrTree attr = optAttrTreeService.getAttrByTreeId(optAttrTree.getTreeId());
        if(attr != null)
        {
            return ResultData.error("该节点已经绑定了属性，不能重复绑定");
        }
        String username = JwtUtil.getUserNameByToken(req);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        optAttrTree.setId(uuid);
        optAttrTree.setCreateBy(username);
        optAttrTree.setCreateTime(new Date());
        boolean ok = optAttrTreeService.save(optAttrTree);
        if(ok){
            return ResultData.ok("操作成功");
        }
        else{
            return ResultData.error("操作失败");
        }
    }

    @AutoLog(value = "树与属性-修改绑定属性")
    @ApiOperation(value="树与属性-修改绑定属性", notes="树与属性-修改绑定属性")
    @PostMapping(value = "/bind/edit")
    public ResultData<?> bindEdit(@RequestBody OptAttrTree optAttrTree, HttpServletRequest req)
    {
        if(StringUtils.isBlank(optAttrTree.getId()))
        {
            return ResultData.error("id不能为空");
        }
        String username = JwtUtil.getUserNameByToken(req);
        optAttrTree.setUpdateBy(username);
        optAttrTree.setUpdateTime(new Date());
        boolean ok = optAttrTreeService.updateById(optAttrTree);
        if(ok){
            return ResultData.ok("操作成功");
        }
        else{
            return ResultData.error("操作失败");
        }
    }

    @AutoLog(value = "树与属性-解除绑定（删除）")
    @ApiOperation(value="树与属性-解除绑定（删除）", notes="树与属性-解除绑定（删除）")
    @DeleteMapping(value = "/bind/delete")
    public ResultData<?> bindDel(@RequestParam String id, HttpServletRequest req)
    {
        if(StringUtils.isBlank(id))
        {
            return ResultData.error("id不能为空");
        }
        OptAttrTree optAttrTree = new OptAttrTree();
        optAttrTree.setId(id);
        optAttrTree.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        optAttrTree.setUpdateBy(username);
        optAttrTree.setUpdateTime(new Date());
        boolean ok = optAttrTreeService.updateById(optAttrTree);
        if(ok){
            return ResultData.ok("操作成功");
        }
        else{
            return ResultData.error("操作失败");
        }
    }

    @AutoLog(value = "设备资产-查询条件-设备状态")
    @ApiOperation(value="设备资产-查询条件-设备状态")
    @GetMapping(value="/getDictValue/working_status")
    public Result<List<DictDropdown>> getDictValue() {
        Result<List<DictDropdown>> result=new Result<>();
        List<DictDropdown> value = sysDictService.getDictValue(CategoryConstant.EQUIPMENT_WORKING_STATUS);
        result.setResult(value);
        result.success("查询成功");
        return result;
    }

    @AutoLog(value="设备资产-查询条件-供应商列表")
    @ApiOperation(value="设备资产-查询条件-供应商列表",notes="设备资产-查询条件-供应商列表")
    @GetMapping(value="/queryNameList")
    public Result<List<SupplierListVo>> queryNameList(String name){
        Result<List<SupplierListVo>> result=new Result<List<SupplierListVo>>();
        List<SupplierListVo> queryNameList = optSupplierService.queryNameList(name);
        result.success("success");
        result.setResult(queryNameList);
        return result;
    }

    @AutoLog(value="设备资产-查询条件-设备类型")
    @ApiOperation(value="设备资产-查询条件-设备类型",notes="设备资产-查询条件-设备类型")
    @GetMapping("/typeList")
    public Result<List<SysCateDropdown>> typeList() {
        Result<List<SysCateDropdown>> result=new Result<>();
        List<SysCateDropdown> keyValue = sysCategoryService.queryKeyValue(CategoryConstant.EQUIPMENT_TYPE_PID);
        result.setCode(CommonConstant.SC_OK_200);
        result.setSuccess(true);
        result.setResult(keyValue);
        return result;
    }

    @AutoLog(value="设备资产-查询条件-标段")
    @ApiOperation(value="设备资产-查询条件-标段",notes="设备资产-查询条件-标段")
    @GetMapping("/labelList")
    public Result<List<SysCateDropdown>> labelList() {
        Result<List<SysCateDropdown>> result=new Result<>();
        List<SysCateDropdown> keyValue = sysCategoryService.queryKeyValue(CategoryConstant.EQUIPMENT_LABEL_PID);
        result.setCode(CommonConstant.SC_OK_200);
        result.setSuccess(true);
        result.setResult(keyValue);
        return result;
    }
}
