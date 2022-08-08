package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.entity.Knowlege;
import com.zcdy.dsc.modules.operation.equipment.param.KnowlegePageParam;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeService;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeDataVo;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 描述: 知识库管理
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Api(tags = "知识库管理")
@RestController
@RequestMapping("/equipment/knowlege")
public class KnowlegeController extends BaseController<Knowlege, IKnowlegeService> {
    @Autowired
    private IKnowlegeService knowlegeService;


    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "知识库管理-分页列表查询")
    @ApiOperation(value = "知识库管理-分页列表查询", notes = "知识库管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<KnowlegeDataVo>> queryPageList(KnowlegePageParam param) {
        Page<KnowlegeDataVo> page = new Page<KnowlegeDataVo>(param.getPageNo(), param.getPageSize());
        Result<IPage<KnowlegeDataVo>> result = new Result<IPage<KnowlegeDataVo>>();
        IPage<KnowlegeDataVo> pageList = knowlegeService.getListInfo(page, param);
        result.setResult(pageList);
        return result.success();
    }

    /**
     * 添加
     *
     * @param knowlegeVo
     * @return
     */
    @AutoLog(value = "知识库管理-添加")
    @ApiOperation(value = "知识库管理-添加", notes = "知识库管理-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("knowlege:add")
    public Result<Object> add(@RequestBody @Valid KnowlegeVo knowlegeVo) {
        //校验重复
        QueryWrapper<Knowlege> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Knowlege::getEquipmentType, knowlegeVo.getEquipmentType())
                .eq(Knowlege::getEquipmentModel, knowlegeVo.getEquipmentModel())
                .eq(Knowlege::getEquipmentSpecs, knowlegeVo.getEquipmentSpecs())
                .eq(Knowlege::getType, knowlegeVo.getType())
                .ne(Knowlege::getId, knowlegeVo.getId());
        if (knowlegeService.count(queryWrapper) > 0) {
            return Result.error("同一规格下,同一个供应商,同一个知识类型只能有一个");
        }
        knowlegeService.saveInfo(knowlegeVo);
        return Result.ok("保存成功");
    }

    /**
     * 编辑
     *
     * @param knowlegeVo
     * @return
     */
    @AutoLog(value = "知识库管理-编辑")
    @ApiOperation(value = "知识库管理-编辑", notes = "知识库管理-编辑")
    @PostMapping(value = "/edit")
    @RequiresPermissions("knowlege:edit")
    public Result<Object> edit(@RequestBody @Valid KnowlegeVo knowlegeVo) {
        //校验重复
        QueryWrapper<Knowlege> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Knowlege::getEquipmentType, knowlegeVo.getEquipmentType())
                .eq(Knowlege::getEquipmentModel, knowlegeVo.getEquipmentModel())
                .eq(Knowlege::getEquipmentSpecs, knowlegeVo.getEquipmentSpecs())
                .eq(Knowlege::getType, knowlegeVo.getType())
                .ne(Knowlege::getId, knowlegeVo.getId());
        if (knowlegeService.count(queryWrapper) > 0) {
            return Result.error("同一规格下,同一个供应商,同一个知识类型只能有一个");
        }
        //知识库对象
        Knowlege knowlege = new Knowlege();
        BeanUtils.copyProperties(knowlegeVo,knowlege);
        knowlege.setItemCount(knowlegeVo.getKnowlegeItemVoList().size());
        knowlegeService.updateById(knowlege);
        return Result.ok("编辑成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "知识库管理-通过id查询")
    @ApiOperation(value = "知识库管理-通过id查询", notes = "知识库管理-通过id查询")
    @GetMapping(value = "/getById")
    public Result<KnowlegeVo> queryById(@RequestParam(name = "id", required = true) @NotBlank(message = "id不允许为空") String id) {
        return Result.ok(knowlegeService.selectInfo(id));
    }
}
