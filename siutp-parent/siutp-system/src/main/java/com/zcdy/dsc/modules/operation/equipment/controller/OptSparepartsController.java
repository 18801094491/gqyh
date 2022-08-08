package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSpareparts;
import com.zcdy.dsc.modules.operation.equipment.param.OptSparepartsPageParam;
import com.zcdy.dsc.modules.operation.equipment.service.OptSparepartsService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptSparepartsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description: 备品备件基本信息
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-06-02 11:42:26
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "备品备件基本信息")
@RestController
@RequestMapping("/equipment/optSpareparts")
public class OptSparepartsController extends BaseController<OptSpareparts, OptSparepartsService> {
    @Resource
    private OptSparepartsService optSparepartsService;

    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "备品备件基本信息-分页列表查询")
    @ApiOperation(value = "备品备件基本信息-分页列表查询", notes = "备品备件基本信息-分页列表查询")
    @GetMapping
    public Result<IPage<OptSparepartsVo>> queryPageList(OptSparepartsPageParam param) {
        Result<IPage<OptSparepartsVo>> result = new Result<>();
        Page<OptSparepartsVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<OptSparepartsVo> data = optSparepartsService.queryPageData(page, param);
        result.setResult(data);
        return result.success();
    }

    /**
     * 添加或修改
     *
     * @param optSpareparts
     * @return
     */
    @AutoLog(value = "备品备件基本信息-添加或修改")
    @ApiOperation(value = "备品备件基本信息-添加或修改", notes = "备品备件基本信息-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Valid OptSpareparts optSpareparts) {
        //校验重复
        QueryWrapper<OptSpareparts> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.lambda().eq(OptSpareparts::getCategoryName, optSpareparts.getCategoryName());
        QueryWrapper<OptSpareparts> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OptSpareparts::getSparepartsModel, optSpareparts.getSparepartsModel())
                .eq(OptSpareparts::getSparepartsSpces, optSpareparts.getSparepartsSpces());
        //修改的话校验id
        if(StringUtils.isNotEmpty(optSpareparts.getId())){
			categoryWrapper.lambda().ne(OptSpareparts::getId, optSpareparts.getId());
			wrapper.lambda().ne(OptSpareparts::getId, optSpareparts.getId());
		}
        if (optSparepartsService.count(categoryWrapper) > 0) {
            return Result.error("名称不能重复");
        }
        if (optSparepartsService.count(wrapper) > 0) {
            return Result.error("规格跟型号重复");
        }
        if (!StringUtils.isEmpty(optSpareparts.getId())) {
            optSparepartsService.updateById(optSpareparts);
        } else {
            optSparepartsService.save(optSpareparts);
        }
        return Result.ok("保存成功！");
    }
}