package com.zcdy.dsc.modules.operation.work.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkThirdTeam;
import com.zcdy.dsc.modules.operation.work.param.ThirdTeamParam;
import com.zcdy.dsc.modules.operation.work.service.WorkThirdTeamService;
import com.zcdy.dsc.modules.operation.work.service.WorkService;
import com.zcdy.dsc.modules.operation.work.vo.ThirdTeamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述: 第三方维修团队管理
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-15 17:44:36
 * 版本号: V1.0
 */
@Api(tags = "第三方维修团队管理")
@RestController
@RequestMapping("/work/thirdTeam")
public class WorkThirdTeamController extends BaseController<WorkThirdTeam, WorkThirdTeamService> {

    @Autowired
    private WorkThirdTeamService workThirdTeamService;

    @Autowired
    private WorkService workService;

    /**
     * 分页列表查询
     *
     * @param teamName
     * @param teamSn
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "第三方维修团队管理-分页列表查询")
    @ApiOperation(value = "第三方维修团队管理-分页列表查询", notes = "第三方维修团队管理-分页列表查询")
    @GetMapping
    public Result<IPage<ThirdTeamVo>> queryPageList(String teamName, String teamSn,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<ThirdTeamVo>> result = new Result<>();
        Page<ThirdTeamVo> page = new Page<>(pageNo, pageSize);
        IPage<ThirdTeamVo> pageDate = workService.queryThirdPageData(page, teamName, teamSn);
        List<ThirdTeamVo> records = pageDate.getRecords();
        for (ThirdTeamVo thirdTeamVo : records) {
            if (thirdTeamVo.getFileUrl() != null && !"".endsWith(thirdTeamVo.getFileUrl())) {
                thirdTeamVo.setFileUrl(baseStoragePath + thirdTeamVo.getFileUrl());
            }
        }
        result.setCode(CommonConstant.SC_OK_200);
        result.setResult(pageDate);
        return result;
    }

    /**
     * 添加或编辑
     *
     * @param teamParamVo
     * @return
     */
    @AutoLog(value = "第三方维修团队管理-添加或编辑")
    @ApiOperation(value = "第三方维修团队管理-添加或编辑", notes = "第三方维修团队管理-添加或编辑")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody ThirdTeamParam teamParamVo) {
        WorkThirdTeam workThirdTeam = new WorkThirdTeam();
        workThirdTeam.setTeamName(teamParamVo.getTeamName());
        workThirdTeam.setAgreeStart(teamParamVo.getAgreeStart());
        workThirdTeam.setAgreeEnd(teamParamVo.getAgreeEnd());
        workThirdTeam.setContactName(teamParamVo.getContactName());
        workThirdTeam.setContactPhone(teamParamVo.getContactPhone());
        workThirdTeam.setContactPosition(teamParamVo.getContactPosition());
        workThirdTeam.setTeamRating(teamParamVo.getTeamRating());
        workThirdTeam.setFileUrl(teamParamVo.getFileUrl());
        workThirdTeam.setFileName(teamParamVo.getFileName());
        if (!StringUtils.isEmpty(teamParamVo.getId()) && teamParamVo.getId() != null) {
            workThirdTeam.setId(teamParamVo.getId());
            workThirdTeamService.updateById(workThirdTeam);
        } else {
            workThirdTeam.setDelFlag(DelFlagConstant.NORMAL);
            workService.saveThirdTeam(workThirdTeam);
        }
        return Result.ok("保存成功！");
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "第三方维修团队管理-通过id查询")
    @ApiOperation(value = "第三方维修团队管理-通过id查询", notes = "第三方维修团队管理-通过id查询")
    @GetMapping(value = "/{id}")
    public Result<?> queryById(@PathVariable("id") String id) {
        WorkThirdTeam workThirdTeam = workThirdTeamService.getById(id);
        return Result.ok(workThirdTeam);
    }

}