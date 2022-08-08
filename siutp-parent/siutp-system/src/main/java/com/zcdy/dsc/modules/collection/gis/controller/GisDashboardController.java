package com.zcdy.dsc.modules.collection.gis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.base.controller.AbstractBaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.NumberFormatUtil;
import com.zcdy.dsc.constant.UnitConstant;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelNav;
import com.zcdy.dsc.modules.collection.gis.job.GisModelJob;
import com.zcdy.dsc.modules.collection.gis.service.GisModelNavService;
import com.zcdy.dsc.modules.collection.gis.service.IGisModelService;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelImgVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelResImgVo;
import com.zcdy.dsc.modules.collection.gis.vo.MeterAndFlowCountVo;
import com.zcdy.dsc.modules.collection.gis.websocket.GISUserWebsocketManager;
import com.zcdy.dsc.modules.monitor.quartz.bean.QuartzScheduleManager;
import com.zcdy.dsc.modules.settle.constant.FlowModuleConstant;
import com.zcdy.dsc.modules.settle.logic.FlowCountCurrentTimeLogic;
import com.zcdy.dsc.modules.settle.logic.FlowCountYesterDayLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songguang.jiao 新增日期：2019年12月20日 上午10:06:57 描述s: 监控中心页面控制器
 */
@Api(tags = "监控中心")
@Slf4j
@RestController
@RequestMapping("/gis/dashboard")
public class GisDashboardController extends AbstractBaseController{

    @Resource
    private IGisModelService iGisModelService;

    /**
     * 调度器
     */
    @Resource
    private QuartzScheduleManager quartzScheduleManager;

    /**
     * 发送Websocket消息的频率
     */
    @Value("${com.zcdy.dsc.gis.ws.cron}")
    private String cron;

    @Resource
    private GisModelNavService gisModelNavService;

    @Resource
    private FlowCountCurrentTimeLogic flowCountCurrentTimeLogic;

    @Resource
    private FlowCountYesterDayLogic yesterDayLogic;

    /**
     * 默认查询不到数据时显示为 --
     */
    private String initFlow = "--";

    /**
     * @author: Roberto
     * 
     * @create:2019年12月24日 下午2:55:42 描述:
     *                     <p>
     *                     通知后台开始访问GIS数据
     *                     </p>
     */
    @ApiOperation("通知后台开始启用后台数据推送")
    @GetMapping("init")
    public Result<String> init() {
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        if (!StringUtils.isEmpty(userId)) {
            GISUserWebsocketManager.setValue(userId, "on");
            // 开启新的定时任务，去redis中查询采集数据
            try {
                boolean isExists = quartzScheduleManager.checkTriggerExists("GIS-iot-" + userId);
                if (isExists) {
                    quartzScheduleManager.resumeTrigger("GIS-iot-" + userId);
                } else {
                    quartzScheduleManager.newJob2Scheduler(GisModelJob.class, "GIS-iot-" + userId, cron, userId);
                }
            } catch (Exception e) {
                log.error("启动用户监控中心任务失败……");
            }
        }
        return new Result<String>().success("ok");
    }

    /**
     * @auther：Roberto
     * @create:2019年12月24日 下午3:00:43 描述:
     *                     <p>
     *                     停止向Websocket通道发送数据
     *                     </p>
     */
    @ApiOperation("通知后台关闭后台数据推送")
    @GetMapping("destroy")
    public Result<String> destroy() {
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        if (!StringUtils.isEmpty(userId)) {
            GISUserWebsocketManager.remove(userId);
            try {
                quartzScheduleManager.pauseTrigger("GIS-iot-" + userId);
            } catch (SchedulerException e) {
                log.error("暂停用户监控中心任务失败……");
            }
        }
        return new Result<String>().success("ok");
    }

    /**
     * 通知后台开始启用后台数据推送
     * @return
     */
    @ApiOperation(value = "通知后台开始启用后台数据推送",notes = "通知后台关闭后台数据推送")
    @GetMapping("app/init")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<String> appInit() {
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        if (!StringUtils.isEmpty(userId)) {
            GISUserWebsocketManager.setValue(userId, "on");
            // 开启新的定时任务，去redis中查询采集数据
            try {
                boolean isExists = quartzScheduleManager.checkTriggerExists("GIS-iot-" + userId);
                if (isExists) {
                    quartzScheduleManager.resumeTrigger("GIS-iot-" + userId);
                } else {
                    quartzScheduleManager.newJob2Scheduler(GisModelJob.class, "GIS-iot-" + userId, cron, userId);
                }
            } catch (Exception e) {
                log.error("启动用户监控中心任务失败……");
            }
        }
        return new Result<String>().success("ok");
    }

    /**
     * 通知后台关闭后台数据推送
     * @return
     */
    @ApiOperation(value = "通知后台关闭后台数据推送",notes = "通知后台关闭后台数据推送")
    @GetMapping("/app/destroy")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<String> appDestroy() {
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        if (!StringUtils.isEmpty(userId)) {
            GISUserWebsocketManager.remove(userId);
            try {
                quartzScheduleManager.pauseTrigger("GIS-iot-" + userId);
            } catch (SchedulerException e) {
                log.error("暂停用户监控中心任务失败……");
            }
        }
        return new Result<String>().success("ok");
    }

    /**
     * 获取所有GIS设备的模型数据
     * @auther：Roberto
     * @create:2019年12月24日 下午5:19:38 描述:
     *                     <p>
     *                     获取所有GIS设备的模型数据
     *                     </p>
     */
    @ApiOperation(value = "获取所有GIS设备的模型数据", notes = "获取所有GIS设备的模型数据")
    @GetMapping(value = "/data", produces = "application/json;charset=utf-8")
    public Result<List<GisModelImgVo>> initAllGisData() {
        Result<List<GisModelImgVo>> result = new Result<List<GisModelImgVo>>();
        List<GisModelImgVo> models = iGisModelService.queryList();

        List<GisModelImgVo> returnModel = new ArrayList<>();
        // 地图拼接全路径,为图片赋值宽跟高
        for (GisModelImgVo gisModelImgVo : models) {
            // 如果Gis正常图片跟打开状态都为空,就不返回给全量地图数据
            if (StringUtils.isEmpty(gisModelImgVo.getImg()) && StringUtils.isEmpty(gisModelImgVo.getOnImg())) {
                continue;
            }

            if (!StringUtils.isEmpty(gisModelImgVo.getImg())) {
                GisModelResImgVo modelImg = new GisModelResImgVo();
                modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getImg());
                modelImg.setWidth(gisModelImgVo.getWidth());
                modelImg.setHeight(gisModelImgVo.getHeight());
                gisModelImgVo.setModelImg(modelImg);
            }
            if (!StringUtils.isEmpty(gisModelImgVo.getWaringImg())) {
                GisModelResImgVo modelImg = new GisModelResImgVo();
                modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getWaringImg());
                modelImg.setWidth(gisModelImgVo.getWidth());
                modelImg.setHeight(gisModelImgVo.getHeight());
                gisModelImgVo.setModelWaringImg(modelImg);
            }
            if (!StringUtils.isEmpty(gisModelImgVo.getOnImg())) {
                GisModelResImgVo modelImg = new GisModelResImgVo();
                modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getOnImg());
                modelImg.setWidth(gisModelImgVo.getWidth());
                modelImg.setHeight(gisModelImgVo.getHeight());
                gisModelImgVo.setModelOnImg(modelImg);
            }
            if (!StringUtils.isEmpty(gisModelImgVo.getOffImg())) {
                GisModelResImgVo modelImg = new GisModelResImgVo();
                modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getOffImg());
                modelImg.setWidth(gisModelImgVo.getWidth());
                modelImg.setHeight(gisModelImgVo.getHeight());
                gisModelImgVo.setModelOffImg(modelImg);
            }
            returnModel.add(gisModelImgVo);
        }
        result.setResult(returnModel);
        return result.success("ok");
    }

    /**
     * 获取所有GIS设备的模型数据
     * @return
     */
    @ApiOperation(value = "获取所有GIS设备的模型数据", notes = "获取所有GIS设备的模型数据")
    @GetMapping("/app/data")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<List<GisModelImgVo>> initAppAllGisData() {
        ResultData<List<GisModelImgVo>> resultData = null;
        try {
            resultData = new ResultData<>();
            List<GisModelImgVo> models = iGisModelService.queryList();
            List<GisModelImgVo> returnModel = new ArrayList<>();
            // 地图拼接全路径,为图片赋值宽跟高
            for (GisModelImgVo gisModelImgVo : models) {
                // 如果Gis正常图片跟打开状态都为空,就不返回给全量地图数据
                if (StringUtils.isEmpty(gisModelImgVo.getImg()) && StringUtils.isEmpty(gisModelImgVo.getOnImg())) {
                    continue;
                }
                if (!StringUtils.isEmpty(gisModelImgVo.getImg())) {
                    GisModelResImgVo modelImg = new GisModelResImgVo();
                    modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getImg());
                    modelImg.setWidth(gisModelImgVo.getWidth());
                    modelImg.setHeight(gisModelImgVo.getHeight());
                    gisModelImgVo.setModelImg(modelImg);
                }
                if (!StringUtils.isEmpty(gisModelImgVo.getWaringImg())) {
                    GisModelResImgVo modelImg = new GisModelResImgVo();
                    modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getWaringImg());
                    modelImg.setWidth(gisModelImgVo.getWidth());
                    modelImg.setHeight(gisModelImgVo.getHeight());
                    gisModelImgVo.setModelWaringImg(modelImg);
                }
                if (!StringUtils.isEmpty(gisModelImgVo.getOnImg())) {
                    GisModelResImgVo modelImg = new GisModelResImgVo();
                    modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getOnImg());
                    modelImg.setWidth(gisModelImgVo.getWidth());
                    modelImg.setHeight(gisModelImgVo.getHeight());
                    gisModelImgVo.setModelOnImg(modelImg);
                }
                if (!StringUtils.isEmpty(gisModelImgVo.getOffImg())) {
                    GisModelResImgVo modelImg = new GisModelResImgVo();
                    modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getOffImg());
                    modelImg.setWidth(gisModelImgVo.getWidth());
                    modelImg.setHeight(gisModelImgVo.getHeight());
                    gisModelImgVo.setModelOffImg(modelImg);
                }
                returnModel.add(gisModelImgVo);
            }
            resultData.setData(returnModel);
        } catch (Exception e) {
            e.printStackTrace();
            resultData.error500("获取失败");
        }
        return resultData;
    }

    /**
     * 描述: gis地图导航筛选栏 @auther： songguang.jiao 创建时间： 2020年3月27日 上午10:00:16 版本号: V1.0
     */
    @ApiOperation(value = "gis地图导航栏", notes = "gis地图导航栏")
    @GetMapping("/gisModelNav")
    public Result<List<GisModelNav>> gisModelNav() {
        Result<List<GisModelNav>> result = new Result<>();
        QueryWrapper<GisModelNav> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GisModelNav::getNavStatus, WorkingStatus.WORKING);
        List<GisModelNav> list = gisModelNavService.list(queryWrapper);
        for (GisModelNav gisModelNav : list) {
            gisModelNav.setModelThumb(baseStoragePath + gisModelNav.getModelThumb());
        }
        result.setResult(list);
        return result.success("success");
    }

    /**
     * gis地图导航栏列表
     * @return
     */
    @ApiOperation(value = "gis地图导航栏列表", notes = "gis地图导航栏列表")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @GetMapping("/app/gisModelNav")
    public ResultData<List<GisModelNav>> gisAppModelNav() {
        ResultData<List<GisModelNav>> resultData = new ResultData<>();
        QueryWrapper<GisModelNav> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GisModelNav::getNavStatus, WorkingStatus.WORKING);
        List<GisModelNav> list = gisModelNavService.list(queryWrapper);
        for (GisModelNav gisModelNav : list) {
            gisModelNav.setModelThumb(baseStoragePath + gisModelNav.getModelThumb());
        }
        resultData.setData(list);
        return resultData;
    }

    /**
     * 描述: 今日流量统计 @author: songguang.jiao 创建时间: 2020年5月7日 下午4:32:01 版本: V1.0
     */
    @ApiOperation(value = "今日流量统计")
    @GetMapping("/flowToday")
    public Result<List<MeterAndFlowCountVo>> flowCountToday() {
        Result<List<MeterAndFlowCountVo>> result = new Result<>();
        result.setResult(flowCountCurrentTimeLogic.getTodayData());
        return result.success("ok");
    }

    /**
     * 描述: 昨日流量统计 @author: songguang.jiao 创建时间: 2020年5月7日 下午4:32:01 版本: V1.0
     */
    @ApiOperation(value = "昨日流量统计")
    @GetMapping("/flowYestoday")
    public Result<List<MeterAndFlowCountVo>> flowCountYestoday() {
        Result<List<MeterAndFlowCountVo>> result = new Result<>();
        List<MeterAndFlowCountVo> allCurrentData = new ArrayList<MeterAndFlowCountVo>();
        // 河东(供)
        MeterAndFlowCountVo meterHeDong = new MeterAndFlowCountVo();
        meterHeDong.setFlowName(FlowModuleConstant.FLOW_HD_NAME);
        String flowHd = yesterDayLogic.getFlowHd();
        String formatFlowHd = flowHd;
        if (!flowHd.endsWith(initFlow)) {
            formatFlowHd = NumberFormatUtil.formatData(new BigDecimal(flowHd));
        }
        meterHeDong.setFlowCount(flowHd + UnitConstant.FLOWUNIT);
        meterHeDong.setFlowCountFormat(formatFlowHd + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterHeDong);

        // 行政区 7标+2标流量
        MeterAndFlowCountVo meterXzq = new MeterAndFlowCountVo();
        meterXzq.setFlowName(FlowModuleConstant.FLOW_XZQ_NAME);
        String flowXzq = yesterDayLogic.getFlowXzq();
        String formatFlowXzq = flowXzq;
        if (!flowXzq.equals(initFlow)) {
            formatFlowXzq = NumberFormatUtil.formatData(new BigDecimal(flowXzq));
        }
        meterXzq.setFlowCount(flowXzq + UnitConstant.FLOWUNIT);
        meterXzq.setFlowCountFormat(formatFlowXzq + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterXzq);

        // 行政区入楼
        MeterAndFlowCountVo meterRuLou = new MeterAndFlowCountVo();
        meterRuLou.setFlowName(FlowModuleConstant.FLOW_XZRL_NAME);
        String flowXzrl = yesterDayLogic.getFlowXzrl();
        String formatFlowXzrl = flowXzrl;
        if (!flowXzrl.endsWith(initFlow)) {
            formatFlowXzrl = NumberFormatUtil.formatData(new BigDecimal(flowXzrl));
        }
        meterRuLou.setFlowCount(flowXzrl + UnitConstant.FLOWUNIT);
        meterRuLou.setFlowCountFormat(formatFlowXzrl + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterRuLou);

        // 行政区绿化 =行政区-行政区入楼
        MeterAndFlowCountVo meterLvHua = new MeterAndFlowCountVo();
        meterLvHua.setFlowName(FlowModuleConstant.FLOW_XZLH_NAME);
        String flowXzlh = yesterDayLogic.getFlowXzlh();
        String formatFlowXzlh = flowXzlh;
        if (!flowXzlh.equals(flowXzlh)) {
            formatFlowXzlh = NumberFormatUtil.formatData(new BigDecimal(flowXzlh));
        }
        meterLvHua.setFlowCount(flowXzlh + UnitConstant.FLOWUNIT);
        meterLvHua.setFlowCountFormat(formatFlowXzlh + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterLvHua);

        // 镜河补水
        MeterAndFlowCountVo meterFzg = new MeterAndFlowCountVo();
        meterFzg.setFlowName(FlowModuleConstant.FLOW_FZG_NAME);
        String flowFzg = yesterDayLogic.getFlowFzg();
        String formatFlowFzg = flowFzg;
        if (!flowFzg.equals(initFlow)) {
            formatFlowFzg = NumberFormatUtil.formatData(new BigDecimal(flowFzg));
        }
        meterFzg.setFlowCount(flowFzg + UnitConstant.FLOWUNIT);
        meterFzg.setFlowCountFormat(formatFlowFzg + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterFzg);

        // 华电北燃
        MeterAndFlowCountVo meterHdbr = new MeterAndFlowCountVo();
        meterHdbr.setFlowName(FlowModuleConstant.FLOW_HDBR_NAME);
        String flowHdbr = yesterDayLogic.getFlowHdbr();
        String formatFlowHdbr = flowHdbr;
        if (!flowHdbr.equals(initFlow)) {
            formatFlowHdbr = NumberFormatUtil.formatData(new BigDecimal(flowHdbr));
        }
        meterHdbr.setFlowCount(flowHdbr + UnitConstant.FLOWUNIT);
        meterHdbr.setFlowCountFormat(formatFlowHdbr + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterHdbr);

        result.setResult(allCurrentData);
        return result.success("ok");
    }

}
