package com.zcdy.dsc.modules.message.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.message.entity.SmsEvent;
import com.zcdy.dsc.modules.message.param.SmsEventPageParam;
import com.zcdy.dsc.modules.message.param.SmsEventParam;
import com.zcdy.dsc.modules.message.service.SmsEventService;
import com.zcdy.dsc.modules.message.vo.SmsEventVo;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 事件管理
 *
 * @author :songguang.jiao
 */
@Api(tags = "事件管理")
@RestController
@RequestMapping("/message/smsEvent")
public class SmsEventController extends BaseController<SmsEvent, SmsEventService> {
    @Resource
    private SmsEventService smsEventService;

    /**
     * 分页列表查询
     *
     * @param param 入参
     * @return 结构集
     */
    @AutoLog(value = "事件管理-分页列表查询")
    @ApiOperation(value = "事件管理-分页列表查询", notes = "事件管理-分页列表查询")
    @GetMapping
    public Result<IPage<SmsEventVo>> queryPageList(SmsEventPageParam param) {
        Result<IPage<SmsEventVo>> result = new Result<>();
        Page<SmsEventVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<SmsEventVo> pageData = smsEventService.queryPageData(page, param);
        result.setResult(pageData);
        return result.success("success");
    }

    /**
     * 添加或修改
     *
     * @param smsEventParam 事件入参
     * @return 结构集
     */
    @AutoLog(value = "事件管理-添加或修改")
    @ApiOperation(value = "事件管理-添加或修改", notes = "事件管理-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody SmsEventParam smsEventParam) {
        SmsEvent smsEvent = new SmsEvent();
        BeanUtil.copyProperties(smsEventParam, smsEvent);
        //检验code不能重复
        QueryWrapper<SmsEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SmsEvent::getEventCode, smsEvent.getEventCode());
        if (StringUtils.isNotEmpty(smsEvent.getId())) {
            queryWrapper.lambda().ne(SmsEvent::getId, smsEvent.getId());
        }
        if (smsEventService.count(queryWrapper) > 0) {
            return Result.error("code不能重复!");
        }
        if (!StringUtils.isEmpty(smsEvent.getId())) {
            smsEvent.setEventCode(null);
            smsEventService.updateById(smsEvent);
        } else {
            smsEvent.setEventStatus(WorkingStatus.WORKING);
            smsEvent.setDelFlag(DelFlagConstant.NORMAL);
            smsEventService.save(smsEvent);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 更改启停用状态
     *
     * @param id 主键id
     * @return 结果集
     */
    @ApiOperation(value = "更改启停用状态", notes = "更改启停用状态")
    @GetMapping("/editStatus")
    public Result<Object> editStatus(String id) {
        UpdateWrapper<SmsEvent> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SmsEvent::getId, id);
        //查询当前状态
        QueryWrapper<SmsEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(SmsEvent::getEventStatus).eq(SmsEvent::getId, id);
        SmsEvent one = smsEventService.getOne(queryWrapper);
        if (one.getEventStatus().equals(WorkingStatus.WORKING)) {
            one.setEventStatus(WorkingStatus.STOP);
        } else {
            one.setEventStatus(WorkingStatus.WORKING);
        }
        smsEventService.update(one, updateWrapper);

        List<SmsEvent> entityList = new ArrayList<>();
        smsEventService.saveBatch(entityList);
        return Result.ok("更新成功");
    }

    /**
     * 导出excel
     *
     * @param param 参数
     */
    @ApiOperation(value = "导出excel", notes = "导出excel")
    @GetMapping(value = "/export")
    public ModelAndView exportXls(SmsEventPageParam param) {
        List<SmsEventVo> data = smsEventService.selectExprotData(param);
        // 子标题
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String secondTitle = "导出人:" + sysUser.getRealname() + "  导出时间：" + nowTime;
        String sheetName = "事件管理";
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, sheetName);
        mv.addObject(NormalExcelConstants.CLASS, SmsEventVo.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(sheetName + "报表", secondTitle, sheetName));
        mv.addObject(NormalExcelConstants.DATA_LIST, data);
        return mv;
    }

}