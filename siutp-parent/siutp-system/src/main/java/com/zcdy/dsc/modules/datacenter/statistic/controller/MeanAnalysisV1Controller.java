package com.zcdy.dsc.modules.datacenter.statistic.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointGroupDataV1;
import com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentVariableVO;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.influxdb.dto.Query;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @author： Roberto
 * 创建时间：2020年3月19日 下午12:28:33
 * 描述: <p>指标数据表格展示接口</p>
 */
@Api(value = "指标表格数据展示接口集", tags = "指标数据展示接口集")
@RestController
@RequestMapping("iot/vardata/analysis/v1")
public class MeanAnalysisV1Controller {

    @Resource
    private IIotVariableInfoService iotVariableInfoService;

    // 统计模块业务逻辑
    @Resource
    private ModuleVarService moduleVarService;

    @Resource
    private InfluxService influxService;

    @ApiOperation(value = "统计项目接口", notes = "统计项目接口,目前id是<ce3992ca69c911ea9fded05099cd3eff>")
    @GetMapping("/item/{moduleId}")
    public Result<List<SysCateDropdown>> getAnalyseItem(
            @ApiParam(name = "moduleId", value = "统计项目的id") @PathVariable String moduleId) {
        List<SysCateDropdown> datas = this.moduleVarService.queryModuleItem(moduleId);
        return Result.success(datas, "success");
    }

    @ApiOperation(value = "根据统计项目出数据", notes = "根据统计项目出数据,目前id是<ce3992ca69c911ea9fded05099cd3eff>")
    @GetMapping("/{moduleId}")
    public Result<List<PointGroupDataV1>> getAnalyseData(@ApiParam(name = "moduleId", value = "统计项目的id") @PathVariable String moduleId,
                                                         @ApiParam(name = "typeCode", value = "统计变量类型编码") String typeCode, Long startTime, Long endTime) {

        //如果时间为空的话,默认查询当前1小时
        if (startTime == null || endTime == null) {
            endTime = System.currentTimeMillis() * 1000000L;
            LocalDateTime localDateTime = LocalDateTime.now();
            localDateTime = localDateTime.plusHours(-1);
            startTime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() * 1000000L;
        } else {
            startTime = startTime.longValue() * 1000000L;
            endTime = endTime.longValue() * 1000000L;
        }

        //根据统计项目id和统计变量查询变量信息和对应资产信息
        List<EquipmentVariableVO> datas = this.moduleVarService.queryEquipAndVarByModuleId(moduleId, typeCode);
        StringBuilder meanSqlBuilder = new StringBuilder();

        meanSqlBuilder.append("select MEAN(var_value) as avgValue from iot_point_data where ");

        int index = 0, size = datas.size();
        Map<String, EquipmentVariableVO> config = new HashMap<>(datas.size());
        if (size == 0) {
            Result.error("变量信息配置错误,请重新配置");
        }
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append("(");
        for (; index < size; index++) {
            if (index > 0) {
                whereClauseBuilder.append(" or ");
            }
            EquipmentVariableVO item = datas.get(index);
            whereClauseBuilder.append("var_name='");
            whereClauseBuilder.append(item.getVariableName());
            whereClauseBuilder.append("'");
            config.put(item.getVariableName(), item);
        }
        whereClauseBuilder.append(")");
        whereClauseBuilder.append(" and time>=")
                .append(startTime)
                .append(" and time<=")
                .append(endTime);

        meanSqlBuilder.append(whereClauseBuilder.toString());
        meanSqlBuilder.append(" group by var_name fill(none)");

        List<Map<String, Object>> varOrders = this.moduleVarService.getVarOrder(moduleId);
        Map<String, Object> varOrderConfig = new HashMap<>(varOrders.size());
        varOrders.forEach(item -> {
            varOrderConfig.put((String) item.get("var_name"), item.get("display_order"));
        });

        StringBuilder maxSqlBuilder = new StringBuilder();
        maxSqlBuilder.append("select max(var_value) as var_value from iot_point_data where ")
                .append(whereClauseBuilder.toString()).append(" group by var_name fill(none)");
        List<PointData> maxDatas = this.influxService.query(maxSqlBuilder.toString(), PointData.class);
        Map<String, PointData> maxMap = new HashMap<>(maxDatas.size());
        maxDatas.forEach(item -> {
            maxMap.put(item.getVarName(), item);
        });
        maxDatas = null;

        StringBuilder minSqlBuilder = new StringBuilder();
        minSqlBuilder.append("select min(var_value) as var_value from iot_point_data where ")
                .append(whereClauseBuilder.toString()).append(" group by var_name fill(none)");
        List<PointData> minDatas = this.influxService.query(minSqlBuilder.toString(), PointData.class);
        Map<String, PointData> minMap = new HashMap<>(minDatas.size());
        minDatas.forEach(item -> {
            minMap.put(item.getVarName(), item);
        });
        minDatas = null;

        Query query = new Query(meanSqlBuilder.toString());
        List<PointGroupDataV1> groupDatas = this.influxService.query(query, PointGroupDataV1.class);
        for (PointGroupDataV1 data : groupDatas) {
            EquipmentVariableVO vo = config.get(data.getVarName());
            data.setEquipmentLocation(vo.getEquipmentLocation());
            data.setEquipmentSection(vo.getSection());
            data.setEquipmentType(vo.getEquipmentType());
            data.setVariableTitle(vo.getVariableTitle() + "[" + vo.getVariableUnit() + "]");
            PointData max = maxMap.get(data.getVarName());
            if (null != max) {
                data.setMaxValue(max.getVarValue());
                data.setMaxTime(Date.from(max.getTime()));
            }
            PointData min = minMap.get(data.getVarName());
            if (null != min) {
                data.setMinValue(min.getVarValue());
                data.setMinTime(Date.from(min.getTime()));
            }

            int scale = vo.getScale();
            if (scale >= 0) {
                BigDecimal bgAvg = new BigDecimal(data.getAvgValue());
                data.setAvgValue(bgAvg.setScale(scale, RoundingMode.HALF_UP).toString());
                if (null != data.getMinValue()) {
                    BigDecimal bgMin = new BigDecimal(data.getMinValue());
                    data.setMinValue(bgMin.setScale(scale, RoundingMode.HALF_UP).toString());
                } else {
                    data.setMinValue("--");
                }
                if (null != data.getMaxValue()) {
                    BigDecimal bgMax = new BigDecimal(data.getMaxValue());
                    data.setMaxValue(bgMax.setScale(scale, RoundingMode.HALF_UP).toString());
                } else {
                    data.setMaxValue("--");
                }
            }
            Object ord = varOrderConfig.get(data.getVarName());
            if (null == ord) {
                ord = 0;
            }
            data.setDisplayOrder((int) ord);
        }
        Collections.sort(groupDatas);
        return Result.success(groupDatas, "success");
    }

    /**
     * 描述: 平均值分析数据导出
     * 创建人:Roberto
     * 创建时间:2020年5月6日 下午2:30:25
     */
    @ApiOperation(value = "导出", notes = "导出")
    @GetMapping("/exprot/{moduleId}")
    public ModelAndView exprot(@ApiParam(name = "moduleId", value = "统计项目的id") @PathVariable String moduleId,
                               @ApiParam(name = "typeCode", value = "统计变量类型编码") String typeCode, Long startTime, Long endTime) {
        //如果时间为空的话,默认查询当前1小时
        if (startTime == null || endTime == null) {
            endTime = System.currentTimeMillis() * 1000000L;
            LocalDateTime localDateTime = LocalDateTime.now();
            localDateTime = localDateTime.plusHours(-1);
            startTime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() * 1000000L;
        } else {
            startTime = startTime.longValue() * 1000000L;
            endTime = endTime.longValue() * 1000000L;
        }

        //根据统计项目id和统计变量查询变量信息和对应资产信息
        List<EquipmentVariableVO> datas = this.moduleVarService.queryEquipAndVarByModuleId(moduleId, typeCode);
        StringBuilder meanSqlBuilder = new StringBuilder();

        meanSqlBuilder.append("select MEAN(var_value) as avgValue from iot_point_data where ");

        int index = 0, size = datas.size();
        Map<String, EquipmentVariableVO> config = new HashMap<>(size);
        if (size == 0) {
            Result.error("变量信息配置错误,请重新配置");
        }
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append("(");
        for (; index < size; index++) {
            if (index > 0) {
                whereClauseBuilder.append(" or ");
            }
            EquipmentVariableVO item = datas.get(index);
            whereClauseBuilder.append("var_name='");
            whereClauseBuilder.append(item.getVariableName());
            whereClauseBuilder.append("'");
            config.put(item.getVariableName(), item);
        }
        whereClauseBuilder.append(")");
        whereClauseBuilder.append(" and time>=")
                .append(startTime)
                .append(" and time<=")
                .append(endTime);

        meanSqlBuilder.append(whereClauseBuilder.toString());
        meanSqlBuilder.append(" group by var_name fill(none)");

        List<Map<String, Object>> varOrders = this.moduleVarService.getVarOrder(moduleId);
        Map<String, Object> varOrderConfig = new HashMap<>(varOrders.size());
        varOrders.forEach(item -> {
            varOrderConfig.put((String) item.get("var_name"), item.get("display_order"));
        });

        StringBuilder maxSqlBuilder = new StringBuilder();
        maxSqlBuilder.append("select max(var_value) as var_value from iot_point_data where ")
                .append(whereClauseBuilder.toString()).append(" group by var_name fill(none)");
        List<PointData> maxDatas = this.influxService.query(maxSqlBuilder.toString(), PointData.class);
        Map<String, PointData> maxMap = new HashMap<>(maxDatas.size());
        maxDatas.forEach(item -> {
            maxMap.put(item.getVarName(), item);
        });
        maxDatas = null;

        StringBuilder minSqlBuilder = new StringBuilder();
        minSqlBuilder.append("select min(var_value) as var_value from iot_point_data where ")
                .append(whereClauseBuilder.toString()).append(" group by var_name fill(none)");
        List<PointData> minDatas = this.influxService.query(minSqlBuilder.toString(), PointData.class);
        Map<String, PointData> minMap = new HashMap<>(minDatas.size());
        minDatas.forEach(item -> {
            minMap.put(item.getVarName(), item);
        });
        minDatas = null;

        Query query = new Query(meanSqlBuilder.toString());
        List<PointGroupDataV1> groupDatas = this.influxService.query(query, PointGroupDataV1.class);
        for (PointGroupDataV1 data : groupDatas) {
            EquipmentVariableVO vo = config.get(data.getVarName());
            data.setEquipmentLocation(vo.getEquipmentLocation());
            data.setEquipmentSection(vo.getSection());
            data.setEquipmentType(vo.getEquipmentType());
            data.setVariableTitle(vo.getVariableTitle() + "[" + vo.getVariableUnit() + "]");
            PointData max = maxMap.get(data.getVarName());
            if (null != max) {
                data.setMaxValue(max.getVarValue());
                data.setMaxTime(Date.from(max.getTime()));
            }
            PointData min = minMap.get(data.getVarName());
            if (null != min) {
                data.setMinValue(min.getVarValue());
                data.setMinTime(Date.from(min.getTime()));
            }

            int scale = vo.getScale();
            if (scale >= 0) {
                BigDecimal bgAvg = new BigDecimal(data.getAvgValue());
                data.setAvgValue(bgAvg.setScale(scale, RoundingMode.HALF_UP).toString());
                if (null != data.getMinValue()) {
                    BigDecimal bgMin = new BigDecimal(data.getMinValue());
                    data.setMinValue(bgMin.setScale(scale, RoundingMode.HALF_UP).toString());
                } else {
                    data.setMinValue("--");
                }
                if (null != data.getMaxValue()) {
                    BigDecimal bgMax = new BigDecimal(data.getMaxValue());
                    data.setMaxValue(bgMax.setScale(scale, RoundingMode.HALF_UP).toString());
                } else {
                    data.setMaxValue("--");
                }
            }
            Object ord = varOrderConfig.get(data.getVarName());
            if (null == ord) {
                ord = 0;
            }
            data.setDisplayOrder((int) ord);
        }
        Collections.sort(groupDatas);

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new Date());
        String secondTitle = "导出人:" + sysUser.getRealname() + "  导出时间：" + nowTime;
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "峰谷值分析报表");
        mv.addObject(NormalExcelConstants.CLASS, PointGroupDataV1.class);
        mv.addObject(NormalExcelConstants.PARAMS,
                new ExportParams("峰谷值分析报表", secondTitle, "峰谷值分析"));
        mv.addObject(NormalExcelConstants.DATA_LIST, groupDatas);
        return mv;
    }
}
