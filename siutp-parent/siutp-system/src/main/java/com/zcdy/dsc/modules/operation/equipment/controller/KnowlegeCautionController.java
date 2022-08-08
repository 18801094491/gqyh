package com.zcdy.dsc.modules.operation.equipment.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeCaution;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeCautionService;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeCaAddVo;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeCautionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述: 知识库条目操作注意事项
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Api(tags = "知识库安全事项")
@RestController
@RequestMapping("/equipment/knowlegeCaution")
public class KnowlegeCautionController extends BaseController<KnowlegeCaution, IKnowlegeCautionService> {
    @Autowired
    private IKnowlegeCautionService knowlegeCautionService;


    /**
     * 添加
     *
     * @param knowlegeCaAddVo
     * @return
     */
    @AutoLog(value = "知识库条目操作注意事项-添加")
    @ApiOperation(value = "知识库条目操作注意事项-添加", notes = "知识库条目操作注意事项-添加")
    @PostMapping(value = "/add")
    public Result<KnowlegeCaAddVo> add(@RequestBody KnowlegeCaAddVo knowlegeCaAddVo) {
        KnowlegeCaution knowlegeCaBean = new KnowlegeCaution();
        BeanUtils.copyProperties(knowlegeCaAddVo, knowlegeCaBean);
        knowlegeCautionService.save(knowlegeCaBean);
        knowlegeCaAddVo.setId(knowlegeCaBean.getId());
        Result<KnowlegeCaAddVo> result = new Result<KnowlegeCaAddVo>();
        result.setResult(knowlegeCaAddVo);
        return result.success();
    }

    /**
     * 编辑
     *
     * @param knowlegeCaution
     * @return
     */
    @AutoLog(value = "知识库条目操作注意事项-编辑")
    @ApiOperation(value = "知识库条目操作注意事项-编辑", notes = "知识库条目操作注意事项-编辑")
    @PostMapping(value = "/edit")
    public Result<Object> edit(@RequestBody KnowlegeCautionVo knowlegeCaution) {
        KnowlegeCaution knowlegeCaBean = new KnowlegeCaution();
        BeanUtils.copyProperties(knowlegeCaution, knowlegeCaBean);
        knowlegeCautionService.updateById(knowlegeCaBean);
        return Result.ok("编辑成功!");
    }

    /**
     * 伪删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "知识库条目操作注意事项-删除(修改状态)")
    @ApiOperation(value = "知识库条目操作注意事项-删除(修改状态)")
    @GetMapping(value = "/modifyknowlegeca")
    public Result<Object> modifyKnowlegeCa(@RequestParam(name = "id") String id) {
        knowlegeCautionService.removeById(id);
        return Result.ok("删除成功!");
    }
}
