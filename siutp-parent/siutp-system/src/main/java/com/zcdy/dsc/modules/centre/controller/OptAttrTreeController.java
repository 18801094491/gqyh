package com.zcdy.dsc.modules.centre.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.centre.entity.OptAttrTree;
import com.zcdy.dsc.modules.centre.param.OptAttrTreeParam;
import com.zcdy.dsc.modules.centre.service.OptAttrTreeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import lombok.extern.slf4j.Slf4j;
import com.zcdy.dsc.common.system.base.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 属性与树形结构关系
 * @Author: 在信汇通
 * @Date:   2021-03-10 10:55:31
 * @Version: V1.0
 */
@Slf4j
@Api(tags="属性与树形结构关系")
@RestController
@RequestMapping("/centre/optAttrTree")
public class OptAttrTreeController extends BaseController<OptAttrTree, OptAttrTreeService> {
	@Autowired
	private OptAttrTreeService optAttrTreeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param optAttrTreeParam
	 * @param req
	 * @return
	 */
	@AutoLog(value = "属性与树形结构关系-分页列表查询")
	@ApiOperation(value="属性与树形结构关系-分页列表查询", notes="属性与树形结构关系-分页列表查询")
	@GetMapping(value = "/list")
	public ResultData<?> queryPageList(OptAttrTreeParam optAttrTreeParam, HttpServletRequest req) {
		OptAttrTree searchParam = new OptAttrTree(optAttrTreeParam);
		searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
		QueryWrapper<OptAttrTree> queryWrapper = QueryGenerator.initQueryWrapper(searchParam, req.getParameterMap());
		queryWrapper.lambda().orderByDesc(OptAttrTree::getCreateTime);//默认创建时间倒序
		Page<OptAttrTree> page = new Page<OptAttrTree>(optAttrTreeParam.getPageNo(), optAttrTreeParam.getPageSize());
		IPage<OptAttrTree> pageList = optAttrTreeService.page(page, queryWrapper);
		return ResultData.ok(pageList);
	}
	
	/**
	 * 添加
	 * @param optAttrTree
	 * @return
	 */
	@AutoLog(value = "属性与树形结构关系-添加")
	@ApiOperation(value="属性与树形结构关系-添加", notes="属性与树形结构关系-添加")
	@PostMapping(value = "/add")
	public ResultData<?> add(@RequestBody OptAttrTree optAttrTree, HttpServletRequest req) {
	    ResultData<OptAttrTree> resultData = new ResultData<OptAttrTree>();
	    try {
            String username = JwtUtil.getUserNameByToken(req);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            optAttrTree.setId(uuid);
            optAttrTree.setCreateTime(new Date());
            optAttrTree.setCreateBy(username);
            boolean ok = optAttrTreeService.save(optAttrTree);
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
	 * @param optAttrTree
	 * @return
	 */
	@AutoLog(value = "属性与树形结构关系-编辑")
	@ApiOperation(value="属性与树形结构关系-编辑", notes="属性与树形结构关系-编辑")
	@PostMapping(value = "/edit")
	public ResultData<?> edit(@RequestBody OptAttrTree optAttrTree, HttpServletRequest req) {
        ResultData<OptAttrTree> resultData = new ResultData<OptAttrTree>();
        try {
            if (optAttrTree == null || optAttrTree.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            optAttrTree.setUpdateTime(new Date());
            optAttrTree.setUpdateBy(username);
            boolean ok = optAttrTreeService.updateById(optAttrTree);
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
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "属性与树形结构关系-通过id删除")
	@ApiOperation(value="属性与树形结构关系-通过id删除", notes="属性与树形结构关系-通过id删除")
	@DeleteMapping(value = "/delete")
	public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<OptAttrTree> resultData = new ResultData<OptAttrTree>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        OptAttrTree optAttrTree = new OptAttrTree();
        optAttrTree.setId(id);
        optAttrTree.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        optAttrTree.setUpdateTime(new Date());
        optAttrTree.setUpdateBy(username);
        boolean ok = optAttrTreeService.updateById(optAttrTree);//逻辑删除
        //boolean ok = optAttrTreeService.removeById(id); //物理删除
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
	@AutoLog(value = "属性与树形结构关系-批量删除")
	@ApiOperation(value="属性与树形结构关系-批量删除", notes="属性与树形结构关系-批量删除")
	@DeleteMapping(value = "/deletes")
	public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<OptAttrTree> resultData = new ResultData<OptAttrTree>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            List<String> list = Arrays.asList(ids.split(","));
            List<OptAttrTree> entityList = new ArrayList<>();
            for(String s : list)
            {
                OptAttrTree optAttrTree = new OptAttrTree();
                optAttrTree.setId(s);
                optAttrTree.setDelFlag(DelFlagConstant.DELETED);//删除标记
                optAttrTree.setUpdateTime(new Date());
                String username = JwtUtil.getUserNameByToken(req);
                optAttrTree.setUpdateBy(username);
                entityList.add(optAttrTree);
            }
            boolean ok = optAttrTreeService.updateBatchById(entityList);//批量逻辑删除
            //optAttrTreeService.removeByIds(Arrays.asList(ids.split(",")));//批量物理删除

            if(ok){
                resultData.success("批量删除成功!");
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
	@AutoLog(value = "属性与树形结构关系-通过id查询")
	@ApiOperation(value="属性与树形结构关系-通过id查询", notes="属性与树形结构关系-通过id查询")
	@GetMapping(value = "/query")
	public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<OptAttrTree> resultData = new ResultData<OptAttrTree>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            OptAttrTree optAttrTree = optAttrTreeService.getById(id);
            resultData.setData(optAttrTree);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
	}
}