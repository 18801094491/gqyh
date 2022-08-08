package com.zcdy.dsc.modules.operation.equipment.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeAttach;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeAttachService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述: 附件信息
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Api(tags = "知识库附件信息")
@RestController
@RequestMapping("/equipment/knowlegeAttach")
public class KnowlegeAttachController extends BaseController<KnowlegeAttach, IKnowlegeAttachService> {
    @Autowired
    private IKnowlegeAttachService knowlegeAttachService;

    /**
     * 添加
     *
     * @param knowlegeAttach
     * @return
     */
    @AutoLog(value = "附件信息-添加")
    @ApiOperation(value = "附件信息-添加", notes = "附件信息-添加")
    @PostMapping(value = "/add")
    public Result<KnowlegeAttach> add(@RequestBody KnowlegeAttach knowlegeAttach) {
        knowlegeAttachService.save(knowlegeAttach);
        //封装返回值
        Result<KnowlegeAttach> result = new Result<>();
        result.setResult(knowlegeAttach);
        return result.success();
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "附件信息-通过id删除")
    @ApiOperation(value = "附件信息-通过id删除", notes = "附件信息-通过id删除")
    @GetMapping(value = "/delete")
    public Result<Object> delete(@RequestParam(name = "id", required = true) String id) {
        knowlegeAttachService.removeById(id);
        return Result.ok("删除成功!");
    }

}
