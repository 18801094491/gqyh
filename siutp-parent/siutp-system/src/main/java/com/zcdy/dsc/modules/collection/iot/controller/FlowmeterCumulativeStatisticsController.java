package com.zcdy.dsc.modules.collection.iot.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgMapExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.excel.def.NormalExcelConstants;
import com.zcdy.dsc.common.excel.view.DynamicColumnExcelView;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.collection.iot.service.FlowmeterCumulativeStatisticsService;
import com.zcdy.dsc.modules.collection.iot.vo.FlowmeterCumulativeStatisticsParam;
import com.zcdy.dsc.modules.collection.iot.vo.FlowmeterCumulativeStatisticsVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * @author tangchao
 * 累计流量统计
 */
@Api(tags = "累计流量统计")
@RestController
@RequestMapping("iot/cumulativeStatistics")
public class FlowmeterCumulativeStatisticsController {

    @Autowired
    private FlowmeterCumulativeStatisticsService flowmeterCumulativeStatisticsService;

    /**
     * @param param beginDate 开始时间
     *              cycle 周期
     *              interval 间隔
     *              cycleUnit 周期单位: 周期单位, YEARS-年 MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分
     *              intervalUnit 间隔单位:  MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分
     * @return
     * @author tangchao
     * @since 2020年4月28日12:58:58
     */
    @GetMapping
    @ApiOperation(value = "累计流量统计列表", notes = "累计流量统计列表")
    public Result<List<Map<String, Object>>> index(FlowmeterCumulativeStatisticsParam param) {
        Result<List<Map<String, Object>>> result = new Result<>();
        try {
            param.validate();
        } catch (IllegalArgumentException e) {
            result.error500(e.getMessage());
            return result;
        }
        List<Map<String, Object>> lists =
                this.flowmeterCumulativeStatisticsService.ioDataByCycleAndIntervalDataFormat(param);
        result.setResult(lists);
        result.success("查询成功");
        return result;
    }

    @ApiOperation(value = "导出", notes = "导出")
    @GetMapping("/exprot")
    public ModelAndView exprot(FlowmeterCumulativeStatisticsParam param) {
        List<Map<String, Object>> result = this.index(param).getResult();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String secondTitle = "导出人:" + sysUser.getRealname() + "  导出时间：" + now;
        new JeecgMapExcelView();
        ModelAndView mv = new ModelAndView(new DynamicColumnExcelView());
        Map<String, Object> map = result.get(0);
        String title = (String) map.getOrDefault("title", "Title");
        Object dataObj = map.get("data");
        if(dataObj instanceof List){
            List<FlowmeterCumulativeStatisticsVo> data =  (List<FlowmeterCumulativeStatisticsVo>)dataObj;
            mv.addObject(NormalExcelConstants.DATA_LIST, data);
        }
        List<Map<String, String>> header = (List<Map<String, String>>)map.get("header");

        mv.addObject(NormalExcelConstants.FILE_NAME, "流量累计统计");
        mv.addObject(NormalExcelConstants.HEADER_LIST, header);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "流量累计统计报表", secondTitle, title));

        return mv;
    }

}
