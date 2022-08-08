package com.zcdy.dsc.modules.operation.work.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeam;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeamTreeEntity;
import com.zcdy.dsc.modules.operation.work.param.WorkTeamParam;
import com.zcdy.dsc.modules.operation.work.service.WorkService;
import com.zcdy.dsc.modules.operation.work.service.WorkTeamService;
import com.zcdy.dsc.modules.operation.work.vo.ShiftsDropdown;
import com.zcdy.dsc.modules.operation.work.vo.WorkTeamTree;
import com.zcdy.dsc.modules.operation.work.vo.WorkTeamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述: 班组信息表
 *
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-10 13:17:06
 * 版本号: V1.0
 */
@Api(tags = "班组信息表")
@RestController
@RequestMapping("/work/workTeam")
public class WorkTeamController extends BaseController<WorkTeam, WorkTeamService> {

    @Autowired
    private WorkService workService;

    /**
     * 分页列表查询
     *
     * @param teamName
     * @param teamStatus
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
            @ApiImplicitParam(name = "teamName", value = "班组名称", paramType = "query"),
            @ApiImplicitParam(name = "teamStatus", value = "班组状态", paramType = "query")
    })
    @AutoLog(value = "班组信息表-分页列表查询")
    @ApiOperation(value = "班组信息表-分页列表查询", notes = "班组信息表-分页列表查询")
    @GetMapping
    public Result<IPage<WorkTeamVo>> queryPageList(String teamName, String teamStatus,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<WorkTeamVo>> result = new Result<IPage<WorkTeamVo>>();
        Page<WorkTeamVo> page = new Page<>(pageNo, pageSize);
        IPage<WorkTeamVo> pageData = workService.queryTeamPageData(page, teamName, teamStatus);
        result.setResult(pageData);
        return result.success();
    }

    /**
     * 添加或修改
     *
     * @param workTeamVo
     * @return
     */
    @AutoLog(value = "班组信息表-添加或修改")
    @ApiOperation(value = "班组信息表-添加或修改", notes = "班组信息表-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody WorkTeamParam workTeamVo) {
        WorkTeam team = new WorkTeam();
        team.setOverTime(workTeamVo.getOverTime());
        team.setStartTime(workTeamVo.getStartTime());
        team.setTeamDescribe(workTeamVo.getTeamDescribe());
        team.setTeamName(workTeamVo.getTeamName());
        String userManageIds = workTeamVo.getUserManageIds();
        String userIds = workTeamVo.getUserIds();
        if(StringUtils.isBlank(userIds))
        {
            return Result.error("成员不能为空");
        }
        if(StringUtils.isBlank(userManageIds))
        {
            return Result.error("管理员不能为空");
        }
        String[] managers = userManageIds.split(",");
        String[] users = userIds.split(",");
        for(String user : users)
        {
            for(String manager : managers)
            {
                if(user.equals(manager))
                {
                    return Result.error("组员和管理员不能重复");
                }
            }
        }
        if (!StringUtils.isEmpty(workTeamVo.getId())) {
            team.setId(workTeamVo.getId());
            workService.updateTeam(team, workTeamVo);
        } else {
            team.setTeamStatus(WorkingStatus.WORKING);
            team.setDelFlag(DelFlagConstant.NORMAL);
            workService.saveTeam(team, workTeamVo);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 导出excel
     *
     * @param teamName
     * @param teamStatus
     * @return
     */
    @ApiOperation(value = "导出excel", notes = "导出excel")
    @GetMapping(value = "/export")
    public ModelAndView exportXls(String teamName, String teamStatus) {
        List<WorkTeamVo> pageList = workService.exportTeamData(teamName, teamStatus);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new Date());
        String secondTitle = "导出人:" + sysUser.getRealname() + "  导出时间：" + nowTime;
        mv.addObject(NormalExcelConstants.FILE_NAME, "班组信息管理");
        mv.addObject(NormalExcelConstants.CLASS, WorkTeamVo.class);
        mv.addObject(NormalExcelConstants.PARAMS,
                new ExportParams("班组信息管理" + "报表", secondTitle, "班组信息管理"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 描述: 班组状态-启停用
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月10日 下午9:06:42
     * 版本号: V1.0
     */
    @ApiOperation(value = "班组状态-启停用", notes = "班组状态-启停用")
    @GetMapping("/editStatus")
    public Result<Object> editTeamStatus(String id) {
        workService.editTeamStatus(id);
        return Result.ok();
    }

    /**
     * 描述: 班次下拉列表(id-value)
     *
     * @author： songguang.jiao
     * 创建时间：  2020年1月19日 下午4:07:31
     * 版本号: V1.0
     */
    @ApiOperation(value = " 班次下拉列表(id-value)", notes = " 班次下拉列表(id-value)")
    @GetMapping("/shiftsList")
    public Result<List<ShiftsDropdown>> queryShiftsName() {
        Result<List<ShiftsDropdown>> result = new Result<>();
        List<ShiftsDropdown> list = workService.queryShiftsName();
        result.setResult(list);
        return result.success();
    }

    /**
     * 班组人员树
     *
     * @return
     */
    @AutoLog("班组人员树")
    @ApiOperation("班组人员树")
    @GetMapping("/tree")
    public Result<List<WorkTeamTree>> treeList() {
        List<WorkTeamTree> teamTreeList = new ArrayList<>();
        List<WorkTeamTreeEntity> treeEntities = workService.treeList();
        Map<String, WorkTeamTreeEntity> treeEntityMap = new TreeMap<>();
        treeEntities.forEach(item -> {
            treeEntityMap.put(item.getTeamId(), item);
        });
        //生成班组菜单
        treeEntityMap.values().forEach(item -> {
            WorkTeamTree workTeamTree = new WorkTeamTree();
            workTeamTree.setId(item.getTeamId());
            workTeamTree.setLeaf(false);
            workTeamTree.setName(item.getTeamName());
            teamTreeList.add(workTeamTree);
        });
        //生成人员清单
        teamTreeList.forEach(item -> {
            treeEntities.forEach(itemChild -> {
                if (item.getId().equals(itemChild.getTeamId())) {
                    List<WorkTeamTree> data = item.getData();
                    if (null == data) {
                        data = new ArrayList<>();
                    }
                    WorkTeamTree workTeamTreeChild = new WorkTeamTree();
                    workTeamTreeChild.setName(itemChild.getUserName());
                    workTeamTreeChild.setLeaf(true);
                    workTeamTreeChild.setId(itemChild.getUserId());
                    workTeamTreeChild.setParentId(item.getId());
                    data.add(workTeamTreeChild);
                    item.setData(data);
                }
            });
        });
        return Result.ok(teamTreeList);
    }

}