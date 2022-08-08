package com.zcdy.dsc.modules.operation.equipment.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeItem;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeItemService;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeItemEditVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述: 知识库条目管理
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Api(tags = "知识库条目管理")
@RestController
@RequestMapping("/equipment/knowlegeItem")
public class KnowlegeItemController extends BaseController<KnowlegeItem, IKnowlegeItemService> {
    @Autowired
    private IKnowlegeItemService knowlegeItemService;


    /**
     * 编辑
     *
     * @param knowlegeVo
     * @return
     */
    @AutoLog(value = "知识库条目管理-修改条目名称")
    @ApiOperation(value = "知识库条目管理-修改条目名称", notes = "知识库条目管理-修改条目名称")
    @PostMapping(value = "/editItemName")
    public Result<Object> edit(@RequestBody KnowlegeItemEditVo knowlegeVo) {
        KnowlegeItem knowlegeItem = new KnowlegeItem();
        knowlegeItem.setId(knowlegeVo.getId());
        knowlegeItem.setOperationName(knowlegeVo.getOperationName());
        knowlegeItemService.updateById(knowlegeItem);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "知识库条目管理-通过id删除")
    @ApiOperation(value = "知识库条目管理-通过id删除", notes = "知识库条目管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<Object> delete(@RequestParam(name = "id", required = true) String id) {
        knowlegeItemService.removeInfo(id);
        return Result.ok("删除成功!");
    }

}
