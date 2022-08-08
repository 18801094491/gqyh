package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentUpkeep;
import com.zcdy.dsc.modules.operation.equipment.service.EquipmentUpkeepService;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentService;
import com.zcdy.dsc.modules.operation.equipment.vo.*;
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

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 描述: 维保记录
 *
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-17 10:58:28
 * 版本号: V1.0
 */
@Api(tags = "维保记录")
@RestController
@RequestMapping("/equipment/equipmentUpkeep")
public class EquipmentUpkeepController extends BaseController<EquipmentUpkeep, EquipmentUpkeepService> {
    @Autowired
    private EquipmentUpkeepService equipmentUpkeepService;

    @Resource
    private IOptEquipmentService equipmentService;

    /**
     * 分页列表查询
     *
     * @param equipmentName
     * @param type
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "维保记录-分页列表查询")
    @ApiOperation(value = "维保记录-分页列表查询", notes = "维保记录-分页列表查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
            @ApiImplicitParam(name = "equipmentName", value = "资产名称", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "维保时间开始", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "维保时间结束", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "维保类型", paramType = "query")})
    @GetMapping
    public Result<IPage<EquipmentUpkeepVo>> queryPageList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String equipmentName,
            String startTime, String endTime, String type) {
        Page<EquipmentUpkeepVo> page = new Page<EquipmentUpkeepVo>(pageNo, pageSize);
        Result<IPage<EquipmentUpkeepVo>> result = new Result<IPage<EquipmentUpkeepVo>>();
        IPage<EquipmentUpkeepVo> pageList = equipmentUpkeepService.getListInfo(page, equipmentName, startTime, endTime,
                type);
        List<EquipmentUpkeepVo> records = pageList.getRecords();
        if (records.size() > 0 && !records.isEmpty()) {
            for (EquipmentUpkeepVo equipmentUpkeepVo : records) {
                if (equipmentUpkeepVo != null) {
                    List<AttachVo> list = equipmentUpkeepVo.getList();
                    if (list.size() > 0 && !list.isEmpty()) {
                        for (AttachVo attachVo : list) {
                            if (attachVo != null) {
                                attachVo.setUpkeepImage(baseStoragePath + attachVo.getUpkeepImage());
                                attachVo.setUpkeepThumb(baseStoragePath + attachVo.getUpkeepThumb());
                            }
                        }
                    }
                }
            }
        }
        result.setResult(pageList);
        result.success("查询成功");
        return result;
    }

    /**
     * 通过维保id查询 详情
     *
     * @param upkeepId
     * @return
     */
    @AutoLog(value = "详情-通过id查询")
    @ApiOperation(value = "详情-通过id查询", notes = "详情-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<EquipmentUpkeepdetVo> queryById(@RequestParam(name = "upkeepId", required = true) String upkeepId) {
        Result<EquipmentUpkeepdetVo> result = new Result<EquipmentUpkeepdetVo>();
        EquipmentUpkeepdetVo vo = equipmentUpkeepService.queryById(upkeepId);
        result.setResult(vo);
        return result.success();
    }

    /**
     * 通过资产id查询-具体的维保信息
     *
     * @param equipmentId
     * @return
     */
    @AutoLog(value = "通过资产id查询-具体的维保信息")
    @ApiOperation(value = "通过资产id查询-具体的维保信息", notes = "通过资产id查询-具体的维保信息")
    @GetMapping(value = "/queryByequId")
    public Result<List<EquipmentUpkeepVo>> queryByequId(
            @RequestParam(name = "equipmentId", required = true) String equipmentId) {
        Result<List<EquipmentUpkeepVo>> result = new Result<List<EquipmentUpkeepVo>>();
        List<EquipmentUpkeepVo> vo = equipmentUpkeepService.getUpkeepInfo(equipmentId);
        result.setResult(vo);
        return result.success();
    }


    /**
     * 导出excel
     */
    @RequestMapping(value = "/export")
    public ModelAndView exportXls() {
        List<EquipmentUpkeepVo> pageList = equipmentUpkeepService.getExportXls();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new Date());
        String secondTitle = "导出人:" + sysUser.getRealname() + "  导出时间：" + nowTime;
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "维保记录");
        mv.addObject(NormalExcelConstants.CLASS, EquipmentUpkeepVo.class);
        mv.addObject(NormalExcelConstants.PARAMS,
                new ExportParams("维保记录" + "报表", secondTitle, "维保记录"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 添加或修改
     *
     * @return
     */
    @AutoLog(value = "维保信息-添加或修改")
    @ApiOperation(value = "维保信息-添加或修改", notes = "维保信息-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Valid EquipmentUpkeepParamVo equipmentUpkeepParamVo) {
        if (!StringUtils.isEmpty(equipmentUpkeepParamVo.getId())) {
            equipmentUpkeepService.editUpkeep(equipmentUpkeepParamVo);
        } else {
            equipmentUpkeepService.saveUpkeep(equipmentUpkeepParamVo);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 描述: 设备资产下拉列表
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月24日 下午4:41:16
     * 版本号: V1.0
     */
    @ApiOperation(value = "设备资产下拉列表", notes = "设备资产下拉列表")
    @GetMapping(value = "equipList")
    @ApiImplicitParams({@ApiImplicitParam(name = "equipmentSn", value = "设备编号", paramType = "query")})
    public Result<List<EquipmentDropdown>> equipList(String equipmentSn) {
        return Result.ok(equipmentService.equipDropdown(equipmentSn));
    }

}