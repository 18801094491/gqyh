package com.zcdy.dsc.modules.worklist.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatterFile;
import com.zcdy.dsc.modules.worklist.param.WorkListMatterFileParam;
import com.zcdy.dsc.modules.worklist.service.WorkListMatterFileService;
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
 * @Description: 工单任务附件
 * @Author: 在信汇通
 * @Date:   2021-01-15 16:30:15
 * @Version: V1.0
 */
@Slf4j
@Api(tags="工单任务附件")
@RestController
@RequestMapping("/worklist/workListMatterFile")
public class WorkListMatterFileController extends BaseController<WorkListMatterFile, WorkListMatterFileService> {
	@Autowired
	private WorkListMatterFileService workListMatterFileService;
	
	/**
	 * 分页列表查询
	 *
	 * @param workListMatterFileParam
	 * @param req
	 * @return
	 */
	@AutoLog(value = "工单任务附件-分页列表查询")
	@ApiOperation(value="工单任务附件-分页列表查询", notes="工单任务附件-分页列表查询")
	@GetMapping(value = "/list")
	public ResultData<?> queryPageList(WorkListMatterFileParam workListMatterFileParam,
								   HttpServletRequest req) {
		WorkListMatterFile searchParam = workListMatterFileParam.getSearchParam();
		searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
		QueryWrapper<WorkListMatterFile> queryWrapper = QueryGenerator.initQueryWrapper(searchParam, req.getParameterMap());
		queryWrapper.lambda().orderByDesc(WorkListMatterFile::getCreateTime);//默认创建时间倒序
		Page<WorkListMatterFile> page = new Page<WorkListMatterFile>(workListMatterFileParam.getPageNo(), workListMatterFileParam.getPageSize());
		IPage<WorkListMatterFile> pageList = workListMatterFileService.page(page, queryWrapper);
		return ResultData.ok(pageList);
	}
	
	/**
	 * 添加
	 * @param workListMatterFile
	 * @return
	 */
	@AutoLog(value = "工单任务附件-添加")
	@ApiOperation(value="工单任务附件-添加", notes="工单任务附件-添加")
	@PostMapping(value = "/add")
	public ResultData<?> add(@RequestBody WorkListMatterFile workListMatterFile, HttpServletRequest req) {
	    ResultData<WorkListMatterFile> resultData = new ResultData<WorkListMatterFile>();
	    try {
            String username = JwtUtil.getUserNameByToken(req);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            workListMatterFile.setId(uuid);
            workListMatterFile.setCreateTime(new Date());
            workListMatterFile.setCreateBy(username);
            boolean ok = workListMatterFileService.save(workListMatterFile);
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
	 * @param workListMatterFile
	 * @return
	 */
	@AutoLog(value = "工单任务附件-编辑")
	@ApiOperation(value="工单任务附件-编辑", notes="工单任务附件-编辑")
	@PostMapping(value = "/edit")
	public ResultData<?> edit(@RequestBody WorkListMatterFile workListMatterFile, HttpServletRequest req) {
        ResultData<WorkListMatterFile> resultData = new ResultData<WorkListMatterFile>();
        try {
            if (workListMatterFile == null || workListMatterFile.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            workListMatterFile.setUpdateTime(new Date());
            workListMatterFile.setUpdateBy(username);
            boolean ok = workListMatterFileService.updateById(workListMatterFile);
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
	@AutoLog(value = "工单任务附件-通过id删除")
	@ApiOperation(value="工单任务附件-通过id删除", notes="工单任务附件-通过id删除")
	@DeleteMapping(value = "/delete")
	public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<WorkListMatterFile> resultData = new ResultData<WorkListMatterFile>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        WorkListMatterFile workListMatterFile = new WorkListMatterFile();
        workListMatterFile.setId(id);
        workListMatterFile.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        workListMatterFile.setUpdateTime(new Date());
        workListMatterFile.setUpdateBy(username);
        boolean ok = workListMatterFileService.updateById(workListMatterFile);//逻辑删除
        //boolean ok = workListMatterFileService.removeById(id); //物理删除
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
	@AutoLog(value = "工单任务附件-批量删除")
	@ApiOperation(value="工单任务附件-批量删除", notes="工单任务附件-批量删除")
	@DeleteMapping(value = "/deletes")
	public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<WorkListMatterFile> resultData = new ResultData<WorkListMatterFile>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            List<String> list = Arrays.asList(ids.split(","));
            List<WorkListMatterFile> entityList = new ArrayList<>();
            for(String s : list)
            {
                WorkListMatterFile workListMatterFile = new WorkListMatterFile();
                workListMatterFile.setId(s);
                workListMatterFile.setDelFlag(DelFlagConstant.DELETED);//删除标记
                workListMatterFile.setUpdateTime(new Date());
                String username = JwtUtil.getUserNameByToken(req);
                workListMatterFile.setUpdateBy(username);
                entityList.add(workListMatterFile);
            }
            boolean ok = workListMatterFileService.updateBatchById(entityList);//批量逻辑删除
            //workListMatterFileService.removeByIds(Arrays.asList(ids.split(",")));//批量物理删除

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
	@AutoLog(value = "工单任务附件-通过id查询")
	@ApiOperation(value="工单任务附件-通过id查询", notes="工单任务附件-通过id查询")
	@GetMapping(value = "/query")
	public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<WorkListMatterFile> resultData = new ResultData<WorkListMatterFile>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            WorkListMatterFile workListMatterFile = workListMatterFileService.getById(id);
            resultData.setData(workListMatterFile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
	}
}