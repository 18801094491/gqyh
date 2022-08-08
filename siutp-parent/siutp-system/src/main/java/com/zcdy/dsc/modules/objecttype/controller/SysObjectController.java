package com.zcdy.dsc.modules.objecttype.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.exception.BusinessException;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ClassUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.centre.service.CentreTreeService;
import com.zcdy.dsc.modules.objecttype.entity.SysObject;
import com.zcdy.dsc.modules.objecttype.param.SysObjectParam;
import com.zcdy.dsc.modules.objecttype.service.SysObjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import lombok.extern.slf4j.Slf4j;
import com.zcdy.dsc.common.system.base.controller.BaseController;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 对象类型
 * @Author: 在信汇通
 * @Date:   2021-03-05 14:50:19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="对象类型")
@RestController
@RequestMapping("/objecttype/sysObject")
public class SysObjectController extends BaseController<SysObject, SysObjectService> {
	@Autowired
	private SysObjectService sysObjectService;

	@Autowired
	private CentreTreeService centreTreeService;

	/**
	 * 分页列表查询
	 *
	 * @param sysObjectParam
	 * @param req
	 * @return
	 */
	@AutoLog(value = "对象类型-分页列表查询")
	@ApiOperation(value="对象类型-分页列表查询", notes="对象类型-分页列表查询")
	@GetMapping(value = "/list")
	public ResultData<?> queryPageList(SysObjectParam sysObjectParam, HttpServletRequest req) {
		SysObject searchParam = new SysObject(sysObjectParam);
		searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        if(StringUtils.isNotBlank(searchParam.getName()))
        {
            searchParam.setName("*" + searchParam.getName() + "*");
        }
        if(StringUtils.isNotBlank(searchParam.getObjType()))
        {
            searchParam.setObjType("*" + searchParam.getObjType() + "*");
        }
		QueryWrapper<SysObject> queryWrapper = QueryGenerator.initQueryWrapper(searchParam, req.getParameterMap());
		queryWrapper.lambda().orderByDesc(SysObject::getCreateTime);//默认创建时间倒序
		Page<SysObject> page = new Page<SysObject>(sysObjectParam.getPageNo(), sysObjectParam.getPageSize());
		IPage<SysObject> pageList = sysObjectService.page(page, queryWrapper);
		return ResultData.ok(pageList);
	}

	@AutoLog(value = "对象类型-获取全部")
    @ApiOperation(value="对象类型-获取全部", notes="对象类型-获取全部")
    @GetMapping(value = "/getAllList")
    public ResultData<?> getAllList(){
        SysObject searchParam = new SysObject();
        searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        QueryWrapper<SysObject> queryWrapper = QueryGenerator.initQueryWrapper(searchParam, null);
        queryWrapper.lambda().orderByDesc(SysObject::getCreateTime);//默认创建时间倒序
        List<SysObject> list = sysObjectService.list(queryWrapper);
        return ResultData.ok(list);
    }

    /**
     * 验证类名是否存在
     * @param objType
     * @return
     */
    @AutoLog(value = "对象类型-验证类名是否可用")
    @ApiOperation(value="对象类型-验证类名是否可用", notes="对象类型-验证类名是否可用")
    @GetMapping(value = "/checkObjType")
	public ResultData checkObjType(@RequestParam String objType)
    {
        if(ConvertUtils.isEmpty(objType))
        {
            return ResultData.error("对象不能为空");
        }
        try {
            List<Map<String, String>> list = ClassUtil.getFieldsAndApiModelProperty(objType);
            if(list.size() == 0)
            {
                return ResultData.ok("对象名可以使用，但未找到可用属性，建议确认后再提交");
            }
            SysObject searchParam = new SysObject();
            searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
            searchParam.setObjType(objType);
            QueryWrapper<SysObject> queryWrapper = QueryGenerator.initQueryWrapper(searchParam, null);
            List<SysObject> pageList = sysObjectService.list(queryWrapper);
            if(pageList.size() > 0)
            {
                return ResultData.error("对象名已存在，可以直接使用，无需重复添加");
            }
        } catch (BusinessException e) {
            return ResultData.error(e.getMessage());
        }
        return ResultData.ok("可以使用");
    }
	
	/**
	 * 添加
	 * @param sysObject
	 * @return
	 */
	@AutoLog(value = "对象类型-添加")
	@ApiOperation(value="对象类型-添加", notes="对象类型-添加")
	@PostMapping(value = "/add")
	public ResultData<?> add(@RequestBody SysObject sysObject, HttpServletRequest req) {
	    ResultData<SysObject> resultData = new ResultData<SysObject>();
	    try {
            String username = JwtUtil.getUserNameByToken(req);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            sysObject.setId(uuid);
            sysObject.setCreateTime(new Date());
            sysObject.setCreateBy(username);
            boolean ok = sysObjectService.save(sysObject);
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
	 * @param sysObject
	 * @return
	 */
	@AutoLog(value = "对象类型-编辑")
	@ApiOperation(value="对象类型-编辑", notes="对象类型-编辑")
	@PostMapping(value = "/edit")
	public ResultData<?> edit(@RequestBody SysObject sysObject, HttpServletRequest req) {
        ResultData<SysObject> resultData = new ResultData<SysObject>();
        try {
            if (sysObject == null || sysObject.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            sysObject.setUpdateTime(new Date());
            sysObject.setUpdateBy(username);
            //修改多个属性
//            if(!checkBeforUpdateAndDel(sysObject.getId()))
//            {
//                resultData.error500("该对象已被引用，不能修改");
//                return resultData;
//            }
//            boolean ok = sysObjectService.updateById(sysObject);
            //只修改名称
            boolean ok = sysObjectService.updateNameById(sysObject);
            if (ok) {
                resultData.success("编辑成功！");
            } else {
                resultData.error500("操作失败");
            }
        } catch (
                Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
	}

     /**
      * 在修改和删除前验证是否允许操作
      * 允许true；不允许false
      * @param id
      * @return
      */
	private boolean checkBeforUpdateAndDel(String id)
    {
        Integer num = centreTreeService.getRootTreeNumByObjId(id);
        if(num > 0)
        {
            //已有树在使用，不能修改或删除
            return false;
        }
        else
        {
            //没有被使用，可以修改或删除
            return true;
        }
    }

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "对象类型-通过id删除")
	@ApiOperation(value="对象类型-通过id删除", notes="对象类型-通过id删除")
	@DeleteMapping(value = "/delete")
	public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<SysObject> resultData = new ResultData<SysObject>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        if(!checkBeforUpdateAndDel(id))
        {
            resultData.error500("该对象已被引用，不能删除");
            return resultData;
        }
        SysObject sysObject = new SysObject();
        sysObject.setId(id);
        sysObject.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        sysObject.setUpdateTime(new Date());
        sysObject.setUpdateBy(username);
        boolean ok = sysObjectService.updateById(sysObject);//逻辑删除
        if (ok) {
            resultData.success("删除成功!");
        } else {
            resultData.error500("删除失败!");
        }
        return resultData;
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "对象类型-批量删除")
	@ApiOperation(value="对象类型-批量删除", notes="对象类型-批量删除")
	@DeleteMapping(value = "/deletes")
	public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<SysObject> resultData = new ResultData<SysObject>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            List<String> list = Arrays.asList(ids.split(","));
            List<SysObject> entityList = new ArrayList<>();
            boolean isAllDel = true;
            for(String s : list)
            {
                if(!checkBeforUpdateAndDel(s))
                {
                    isAllDel = false;
                    continue;
                }
                SysObject sysObject = new SysObject();
                sysObject.setId(s);
                sysObject.setDelFlag(DelFlagConstant.DELETED);//删除标记
                sysObject.setUpdateTime(new Date());
                String username = JwtUtil.getUserNameByToken(req);
                sysObject.setUpdateBy(username);
                entityList.add(sysObject);
            }
            if(entityList.size() == 0)
            {
                return ResultData.error("对象均在使用中，不能删除");
            }
            boolean ok = sysObjectService.updateBatchById(entityList);//批量逻辑删除

            if(ok){
                if(isAllDel)
                {
                    resultData.success("批量删除成功!");
                }
                else
                {
                    resultData.success("部分对象已被引用，未能删除，其余对象已删除！");
                }
            }
            else{
                resultData.error500("操作失败");
            }
        }
        return resultData;
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "对象类型-通过id查询")
	@ApiOperation(value="对象类型-通过id查询", notes="对象类型-通过id查询")
	@GetMapping(value = "/query")
	public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<SysObject> resultData = new ResultData<SysObject>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            SysObject sysObject = sysObjectService.getById(id);
            resultData.setData(sysObject);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
	}
}