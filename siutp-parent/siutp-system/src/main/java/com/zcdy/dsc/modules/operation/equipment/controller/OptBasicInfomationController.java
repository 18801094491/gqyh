package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.entity.OptBasicInfomation;
import com.zcdy.dsc.modules.operation.equipment.param.OptBaseInfoPageParam;
import com.zcdy.dsc.modules.operation.equipment.service.OptBasicInfomationService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptBaseInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description: 设备台账基本信息
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-05-28 15:03:28
 * @Version: V1.0
 */
@Api(tags = "设备台账基本信息")
@RestController
@RequestMapping("/equipment/baseInfo")
public class OptBasicInfomationController extends BaseController<OptBasicInfomation, OptBasicInfomationService> {
    @Resource
    private OptBasicInfomationService optBasicInfomationService;

    /**
     * 分页列表查询
     * @param param
     * @return
     */
    @AutoLog(value = "设备台账基本信息-分页列表查询")
    @ApiOperation(value = "设备台账基本信息-分页列表查询", notes = "设备台账基本信息-分页列表查询")
    @GetMapping
    public Result<IPage<OptBaseInfoVo>> queryPageList(OptBaseInfoPageParam param) {
        Result<IPage<OptBaseInfoVo>> result = new Result<>();
        Page<OptBaseInfoVo> page=new Page<>(param.getPageNo(), param.getPageSize());
        IPage<OptBaseInfoVo> data = optBasicInfomationService.queryPageData(page,param);
        result.setResult(data);
        return result.success();
    }

    /**
     * 添加或修改
     * 
     * @param optBasicInfomation
     * @return
     */
    @AutoLog(value = "设备台账基本信息-添加或修改")
    @ApiOperation(value = "设备台账基本信息-添加或修改", notes = "设备台账基本信息-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Valid OptBasicInfomation optBasicInfomation) {
        //校验名称是否重复
        LambdaQueryWrapper<OptBasicInfomation> nameWrapper =new LambdaQueryWrapper<OptBasicInfomation>()
                            .eq(OptBasicInfomation::getBaseName, optBasicInfomation.getBaseName());
        //四项组成唯一性数据不能重复
        LambdaQueryWrapper<OptBasicInfomation> uniqueWrapper =new LambdaQueryWrapper<OptBasicInfomation>().
            eq(OptBasicInfomation::getEquipmentModel,optBasicInfomation.getEquipmentModel())
            .eq(OptBasicInfomation::getEquipmentSpecs, optBasicInfomation.getEquipmentSpecs())
            .eq(OptBasicInfomation::getEquipmentType, optBasicInfomation.getEquipmentType())
            .eq(OptBasicInfomation::getEquipmentSupplier, optBasicInfomation.getEquipmentSupplier());
        if(!StringUtils.isEmpty(optBasicInfomation.getId())){
            nameWrapper= nameWrapper.ne(OptBasicInfomation::getId, optBasicInfomation.getId());
            uniqueWrapper=uniqueWrapper.ne(OptBasicInfomation::getId, optBasicInfomation.getId());
        }
        int nameCount = optBasicInfomationService.count(nameWrapper);
        if(nameCount>0){
            return Result.error("基础信息名称不能重复");
        }
        int count = optBasicInfomationService.count(uniqueWrapper);
        if(count>0){
            return Result.error("类型组合不能重复");
        }
        if (!StringUtils.isEmpty(optBasicInfomation.getId())) {
            optBasicInfomationService.updateEquipInfo(optBasicInfomation);
        } else {
            optBasicInfomationService.save(optBasicInfomation);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 通过id删除
     * 
     * @param id
     * @return
     */
    @AutoLog(value = "设备台账基本信息-通过id删除")
    @ApiOperation(value = "设备台账基本信息-通过id删除", notes = "设备台账基本信息-通过id删除")
    @DeleteMapping(value = "{id}")
    public Result<Object> delete(@PathVariable("id") String id) {
        optBasicInfomationService.removeBaseInfo(id);
        return Result.ok("删除成功!");
    }

}