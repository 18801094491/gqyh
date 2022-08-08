package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.operation.equipment.entity.OptLabel;
import com.zcdy.dsc.modules.operation.equipment.service.OptLabelService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptLabelTreeVo;
import com.zcdy.dsc.modules.system.entity.SysDict;
import com.zcdy.dsc.modules.system.service.ISysCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 设备标签配置
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-04-20 10:45:47
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "设备台账")
@RestController
@RequestMapping("/equipment/optLabel")
public class OptLabelController extends BaseController<OptLabel, OptLabelService> {
    @Autowired
    private OptLabelService optLabelService;

    /**
     * 根节点列表
     *
     * @return
     */
    @AutoLog(value = "设备标签配置-根节点查询")
    @ApiOperation(value = "设备标签配置-根节点查询", notes = "设备标签配置-根节点查询")
    @GetMapping(value = "/rootList")
    public Result<IPage<OptLabel>> rootList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<OptLabel> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(OptLabel::getParentId, ISysCategoryService.ROOT_PID_VALUE)
                .eq(OptLabel::getDelFlag, 0)
                .orderByAsc(OptLabel::getCreateTime);

        IPage<OptLabel> pageList = optLabelService.page(new Page<OptLabel>(pageNo, pageSize), wrapper);
        Result<IPage<OptLabel>> result = new Result<IPage<OptLabel>>();
        result.setSuccess(true);
        result.setCode(200);
        result.setResult(pageList);
        return result;
    }

    @GetMapping(value = "/childList")
    @AutoLog(value = "设备标签配置-子节点查询")
    @ApiOperation(value = "设备标签配置-子节点查询", notes = "设备标签配置-子节点查询")
    public Result<List<OptLabel>> queryPageList(@RequestParam(value = "parentId") String parentId) {
        Result<List<OptLabel>> result = new Result<List<OptLabel>>();
        QueryWrapper<OptLabel> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(OptLabel::getParentId, parentId)
                .eq(OptLabel::getDelFlag, 0)
                .orderByAsc(OptLabel::getCreateTime);
        List<OptLabel> list = optLabelService.list(wrapper);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    /**
     * 添加
     *
     * @param optLabel
     * @return
     */
    @AutoLog(value = "设备标签配置-添加")
    @ApiOperation(value = "设备标签配置-添加", notes = "设备标签配置-添加")
    @PostMapping(value = "add")
    public Result<?> add(@RequestBody OptLabel optLabel) {
        Result<OptLabel> result = new Result<>();
        try {
            optLabelService.addOptLabel(optLabel);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 修改
     *
     * @param optLabel
     * @return
     */
    @AutoLog(value = "设备标签配置-修改")
    @ApiOperation(value = "设备标签配置-修改", notes = "设备标签配置-修改")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody OptLabel optLabel) {
        Result<OptLabel> result = new Result<>();
        try {
            optLabelService.updateOptLabel(optLabel);
            result.success("修改成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备标签配置-通过id删除")
    @ApiOperation(value = "设备标签配置-通过id删除", notes = "设备标签配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam("id") String id) {
        Result<OptLabel> result = new Result<>();
        optLabelService.removeOptLabel(id);
        return result;
    }

    /**
     * 通过ids 批量删除删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "设备标签配置-批量删除删除")
    @ApiOperation(value = "设备标签配置-批量删除删除", notes = "设备标签配置-批量删除删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam("ids") String ids) {
        Result<SysDict> result = new Result<SysDict>();
        if (ConvertUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            optLabelService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }
    /**
     * 查询数据 查出所有设备标签,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    @AutoLog(value = "设备标签配置-树形结构查询")
    @ApiOperation(value = "设备标签配置-树形结构查询", notes = "设备标签配置-树形结构查询")
    public Result<List<OptLabelTreeVo>> queryTreeList() {
        Result<List<OptLabelTreeVo>> result = new Result<>();
        try {
        	//获取根节点和下面所有子节点
            List<OptLabelTreeVo> list = optLabelService.getTreeById(ISysCategoryService.ROOT_PID_VALUE);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }

    /**
     * <p>
     * 通过设备标签名称关键字,模糊查询功能
     * </p>
     *
     * @param keyWord
     * @return
     */
    @RequestMapping(value = "/searchBy", method = RequestMethod.GET)
    public Result<List<OptLabelTreeVo>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord) {
        Result<List<OptLabelTreeVo>> result = new Result<List<OptLabelTreeVo>>();
        try {
            List<OptLabelTreeVo> treeList = this.optLabelService.searchBy(keyWord);
            if (treeList == null || treeList.size() == 0) {
                treeList = new ArrayList<>(1);
                OptLabelTreeVo root = new OptLabelTreeVo();
                OptLabel rootLabel = this.optLabelService.getById("0");
                root.setIsLeaf(false);
                root.setId("0");
                root.setPid("");
                root.setKey("0");
                root.setTitle(rootLabel.getLabelName());
                root.setExpanded(false);
                treeList.add(root);
            }
            result.setSuccess(true);
            result.setResult(treeList);
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("没有符合的条件!");
            return result;
        }
    }

}