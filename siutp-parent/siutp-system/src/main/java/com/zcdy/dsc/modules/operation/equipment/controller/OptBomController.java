package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.entity.OptBom;
import com.zcdy.dsc.modules.operation.equipment.param.OptBomPageParam;
import com.zcdy.dsc.modules.operation.equipment.service.OptBomService;
import com.zcdy.dsc.modules.operation.equipment.service.OptSparepartsService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptBomVo;
import com.zcdy.dsc.modules.operation.equipment.vo.SparepartsDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Description: bom清单
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-06-02 11:43:08
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "bom清单")
@RestController
@RequestMapping("/equipment/optBom")
public class OptBomController extends BaseController<OptBom, OptBomService> {
    @Resource
    private OptBomService optBomService;

    @Resource
    private OptSparepartsService optSparepartsService;

    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "bom清单-分页列表查询")
    @ApiOperation(value = "bom清单-分页列表查询", notes = "bom清单-分页列表查询")
    @GetMapping
    public Result<IPage<OptBomVo>> queryPageList(@Valid OptBomPageParam param) {
        Result<IPage<OptBomVo>> result = new Result<IPage<OptBomVo>>();
        Page<OptBomVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<OptBomVo> data = optBomService.queryPageData(page, param);
        result.setResult(data);
        return result.success();
    }

    /**
     * 添加或修改
     *
     * @param optBom
     * @return
     */
    @AutoLog(value = "bom清单-添加或修改")
    @ApiOperation(value = "bom清单-添加或修改", notes = "bom清单-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Valid OptBom optBom) {
        //检验重复
        LambdaQueryWrapper<OptBom> queryWrapper = new LambdaQueryWrapper<OptBom>().eq(OptBom::getBasicId, optBom.getBasicId()).eq(OptBom::getBomType, optBom.getBomType()).eq(OptBom::getSparepartsId, optBom.getSparepartsId());
        if (!StringUtils.isEmpty(optBom.getId())) {
            queryWrapper = queryWrapper.ne(OptBom::getId, optBom.getId());
        }
        if (optBomService.count(queryWrapper) > 0) {
            return Result.error("类型组合不能重复");
        }
        if (!StringUtils.isEmpty(optBom.getId())) {

            optBomService.updateById(optBom);
        } else {
            optBomService.save(optBom);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 备品备件下拉选
     *
     * @param name
     * @return
     */
    @AutoLog(value = "备品备件下拉选")
    @ApiOperation(value = "备品备件下拉选")
    @GetMapping("/spareparts")
    public Result<List<SparepartsDropdown>> querySparepartsList(String name) {
        return Result.ok(optSparepartsService.querySparepartsList(name));
    }

}