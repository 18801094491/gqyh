package com.zcdy.dsc.modules.operation.equipment.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeOperation;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeOperationService;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeOpAddVo;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeOperationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述: 知识库条目操作章程
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Api(tags = "知识库操作规程")
@RestController
@RequestMapping("/equipment/knowlegeOperation")
public class KnowlegeOperationController extends BaseController<KnowlegeOperation, IKnowlegeOperationService> {
    @Autowired
    private IKnowlegeOperationService knowlegeOperationService;


    /**
     * 添加
     *
     * @param knowlegeOperationVo
     * @return
     */
    @AutoLog(value = "知识库条目操作章程-添加")
    @ApiOperation(value = "知识库条目操作章程-添加", notes = "知识库条目操作章程-添加")
    @PostMapping(value = "/add")
    public Result<KnowlegeOperationVo> add(@RequestBody KnowlegeOpAddVo knowlegeOperationVo) {
        KnowlegeOperation knowlegeOpBean = new KnowlegeOperation();
        BeanUtils.copyProperties(knowlegeOperationVo, knowlegeOpBean);
        knowlegeOperationService.save(knowlegeOpBean);
        KnowlegeOperationVo operationVo = new KnowlegeOperationVo();
        BeanUtils.copyProperties(knowlegeOpBean, operationVo);
        Result<KnowlegeOperationVo> result = new Result<>();
        result.setResult(operationVo);
        return result.success();
    }

    /**
     * 编辑
     *
     * @param knowlegeOperation
     * @return
     */
    @AutoLog(value = "知识库条目操作章程-编辑")
    @ApiOperation(value = "知识库条目操作章程-编辑", notes = "知识库条目操作章程-编辑")
    @PostMapping(value = "/edit")
    public Result<Object> edit(@RequestBody KnowlegeOperationVo knowlegeOperation) {
        KnowlegeOperation knowlegeOpBean = new KnowlegeOperation();
        BeanUtils.copyProperties(knowlegeOperation, knowlegeOpBean);
        knowlegeOperationService.updateById(knowlegeOpBean);
        return Result.ok("编辑成功!");
    }

    /**
     * 伪删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "知识库条目操作章程-删除(修改状态)")
    @ApiOperation(value = "知识库条目操作章程-删除(修改状态)")
    @GetMapping(value = "/modifyknowlegeop")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "自身id", paramType = "query")
    })
    public Result<Object> modifyKnowlegeCa(@RequestParam(name = "id") String id) {
        knowlegeOperationService.removeById(id);
        return Result.ok("删除成功!");
    }

}
