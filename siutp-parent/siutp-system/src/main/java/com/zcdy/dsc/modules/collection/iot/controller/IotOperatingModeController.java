package com.zcdy.dsc.modules.collection.iot.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.framework.redis.RedisOperation;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.gis.vo.IotDataVO;
import com.zcdy.dsc.modules.collection.iot.entity.GisIotEquipEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotOperateModeEntityDefault;
import com.zcdy.dsc.modules.collection.iot.entity.IotOperatingMode;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IOTModelService;
import com.zcdy.dsc.modules.collection.iot.service.IotOperatingModeService;
import com.zcdy.dsc.modules.collection.iot.vo.OperateDataInfo;
import com.zcdy.dsc.modules.collection.iot.vo.OperateEquipInfomation;
import com.zcdy.dsc.modules.collection.iot.vo.OperateEquipInfomation.OperateEquipInfoVo;
import com.zcdy.dsc.modules.collection.iot.vo.OperateEquipPageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Description: 工况监控关联设备信息
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-05-21 15:02:57
 * @Version: V1.0
 */
@Api(tags = "工况监控关联设备信息")
@RestController
@RequestMapping("/iot/operate")
public class IotOperatingModeController extends BaseController<IotOperatingMode, IotOperatingModeService> {
    @Resource
    private IotOperatingModeService iotOperatingModeService;

    @Resource
    private IOTModelService iotModelService;

    @Resource(name = "stringRedisOperation")
    private RedisOperation<String> stringRedisOperation;

    /**
     * 7标检测点id
     */
    private final String IOT_LOCATION_7_ID = "39014c10abb711eab47f0050568c260c";
    /**
     * 2标检测到id
     */
    private final String IOT_LOCATION_2_ID = "342a754fabb711eab47f0050568c260c";

    /**
     * 2标检测点图片路径
     */
    private final String section2 = "/res/file/equipImg/section2.jpg";
    /**
     * 7标检测点图片路径
     */
    private final String section7 = "/res/file/equipImg/section7.jpg";


    /**
     * 所有设备列表
     *
     * @param param
     * @return
     * @author songguang.jiao
     * @date 2020/05/21 16:27:41
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(value = "所有设备列表")
    @GetMapping("/equip")
    public Result<OperateEquipInfomation> equipList(OperateEquipPageParam param) {
        Result<OperateEquipInfomation> result = new Result<>();
        OperateEquipInfomation operateEquipInfomation = new OperateEquipInfomation();
        Page<OperateEquipInfoVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<OperateEquipInfoVo> data = iotOperatingModeService.getAllEquip(page, param);
        //单独返回列表id
        List<Object> equipIdList = iotOperatingModeService.listObjs(Wrappers.lambdaQuery(new IotOperatingMode()).select(IotOperatingMode::getEquimentId));
        operateEquipInfomation.setIds(equipIdList);
        operateEquipInfomation.setOperateEquipInfoVo(data);
        result.setResult(operateEquipInfomation);
        return result.success();
    }

    /**
     * 监控设备列表
     *
     * @return
     * @author songguang.jiao
     * @date 2020/05/21 16:40:59
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(value = "监控设备列表")
    @GetMapping("/mode")
    public Result<List<OperateDataInfo>> operateModeList() {
        Result<List<OperateDataInfo>> result = new Result<>();
        List<GisIotEquipEntity> iot2Gis = iotOperatingModeService.getOperateModeEquip();
        List<OperateDataInfo> dataList = new ArrayList<>(iot2Gis.size());
        // 监控的设备信息循环获取采集的数据信息
        for (GisIotEquipEntity iot2GisEntity : iot2Gis) {
            OperateDataInfo operateDataInfo = new OperateDataInfo();
            operateDataInfo.setEquipName(iot2GisEntity.getEquipName());
            if (!StringUtils.isEmpty(iot2GisEntity.getEquipImg())) {
                operateDataInfo.setEquipImg(baseStoragePath + iot2GisEntity.getEquipImg());
            }
            List<VariableInfo> vars = this.iotModelService.getVarsByModelId(iot2GisEntity.getGisId());
            List<IotDataVO> data = new ArrayList<>(vars.size());
            vars.forEach(item -> {
                String messageStr =
                        this.stringRedisOperation.get(String.format(RedisKeyConstantV2.REDISDATAKEY, item.getVarName()));
                if (!StringUtils.isEmpty(messageStr)) {
                    ValueEntity value = JSON.parseObject(messageStr, ValueEntity.class);
                    IotDataVO dataVO = new IotDataVO();
                    dataVO.setVariableName(item.getVarTitle());
                    if (null == value.getValue()) {
                        dataVO.setVaribleValue("");
                    } else {
                        BigDecimal bg = new BigDecimal(value.getValue());
                        // 使用小数位控制
                        int scale = null == item.getScale() ? 2 : item.getScale().intValue();
                        if (scale >= 0) {
                            bg = bg.setScale(scale, RoundingMode.HALF_UP);
                        }
                        if (!StringUtils.isEmpty(item.getUnited())) {
                            dataVO.setVaribleValue(bg.toEngineeringString() + "[" + item.getUnited() + "]");
                        } else {
                            dataVO.setVaribleValue(bg.toEngineeringString());
                        }
                    }
                    data.add(dataVO);
                }
            });
            operateDataInfo.setDatas(data);
            dataList.add(operateDataInfo);
        }
        result.setResult(dataList);
        return result.success();
    }

    /**
     * 默认加载的设备信息
     *
     * @return
     */
    @AutoLog("默认加载的设备信息")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(value = "默认加载的设备信息")
    @GetMapping("defaultMode")
    public Result<List<OperateDataInfo>> defaultMode() {
        Result<List<OperateDataInfo>> result = new Result<>();
        List<IotOperateModeEntityDefault> iotDefaults = iotOperatingModeService.getDefaultModeEquip();
        //同一个地区的放置到同一个对象中展示
        Map<String, OperateDataInfo> mapRerun = new TreeMap<>();
        for (IotOperateModeEntityDefault iotDefault : iotDefaults) {
            OperateDataInfo operateDataInfo = mapRerun.get(iotDefault.getOperateLocationId());
            //返回结果初始化基础数据
            if (null == operateDataInfo) {
                operateDataInfo = new OperateDataInfo();
                operateDataInfo.setEquipName(iotDefault.getOperateLocationName());
                //2标 7标不赋值图片
                if (!IOT_LOCATION_7_ID.equals(iotDefault.getOperateLocationId()) &&
                        !IOT_LOCATION_2_ID.equals(iotDefault.getOperateLocationId()) &&
                        !StringUtils.isEmpty(iotDefault.getEquipImg())) {
                    operateDataInfo.setEquipImg(baseStoragePath + iotDefault.getEquipImg());
                }
                //2标,7标图片赋值
                if (IOT_LOCATION_2_ID.equals(iotDefault.getOperateLocationId())) {
                    operateDataInfo.setEquipImg(baseStoragePath + section2);
                }
                if (IOT_LOCATION_7_ID.equals(iotDefault.getOperateLocationId())) {
                    operateDataInfo.setEquipImg(baseStoragePath + section7);
                }
                mapRerun.put(iotDefault.getOperateLocationId(), operateDataInfo);
            }

            //监控的设备信息循环获取采集的数据信息
            List<VariableInfo> vars = this.iotModelService.getVarsByModelId(iotDefault.getGisId());
            List<IotDataVO> data = operateDataInfo.getDatas();
            if (null == data || data.size() == 0) {
                data = new ArrayList<>(vars.size());
            }
            List<IotDataVO> finalData = data;
            vars.forEach(item -> {
                String messageStr =
                        this.stringRedisOperation.get(String.format(RedisKeyConstantV2.REDISDATAKEY, item.getVarName()));
                if (!StringUtils.isEmpty(messageStr)) {
                    ValueEntity value = JSON.parseObject(messageStr, ValueEntity.class);
                    IotDataVO dataVO = new IotDataVO();
                    dataVO.setVariableName(iotDefault.getEquimentLocation() + "[" + item.getVarTitle() + "]");
                    if (null == value.getValue()) {
                        dataVO.setVaribleValue("");
                    } else {
                        BigDecimal bg = new BigDecimal(value.getValue());
                        // 使用小数位控制
                        int scale = null == item.getScale() ? 2 : item.getScale().intValue();
                        if (scale >= 0) {
                            bg = bg.setScale(scale, RoundingMode.HALF_UP);
                        }
                        if (!StringUtils.isEmpty(item.getUnited())) {
                            dataVO.setVaribleValue(bg.toEngineeringString() + "[" + item.getUnited() + "]");
                        } else {
                            dataVO.setVaribleValue(bg.toEngineeringString());
                        }
                    }
                    finalData.add(dataVO);
                }
            });
            operateDataInfo.setDatas(data);
        }

        List<OperateDataInfo> dataList = new ArrayList<>(mapRerun.values());
        result.setResult(dataList);
        return result.success();
    }


    /**
     * 保存信息
     *
     * @return
     * @author songguang.jiao
     * @date 2020/05/21 17:03:50
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(value = "保存信息")
    @GetMapping("/save")
    public Result<Object> save(String ids) {
        if (!StringUtils.isEmpty(ids)) {
            // 入参id数组
            String[] split = ids.split(",");
            List<String> paramIds = Arrays.asList(split);

            // 查询出数据库存在的设备信息
            List<IotOperatingMode> existData = iotOperatingModeService
                    .list(Wrappers.lambdaQuery(new IotOperatingMode()).select(IotOperatingMode::getEquimentId));
            List<String> existIds = new ArrayList<>();
            for (IotOperatingMode iotOperatingMode : existData) {
                existIds.add(iotOperatingMode.getEquimentId());
            }

            // 删除
            List<String> delete = this.getDiff(paramIds, existIds);
            for (String equipmentId : delete) {
                iotOperatingModeService.remove(
                        Wrappers.lambdaQuery(new IotOperatingMode()).eq(IotOperatingMode::getEquimentId, equipmentId));
            }

            // 保存
            List<String> saveIds = this.getDiff(existIds, paramIds);
            List<IotOperatingMode> list = new ArrayList<>();
            for (String equipmentId : saveIds) {
                IotOperatingMode iotOperatingMode = new IotOperatingMode();
                iotOperatingMode.setEquimentId(equipmentId);
                list.add(iotOperatingMode);
            }
            iotOperatingModeService.saveBatch(list);
        } else {
            iotOperatingModeService.remove(new LambdaQueryWrapper<IotOperatingMode>());
        }
        return Result.ok("保存成功");
    }

    /**
     * 比较不同,从differ中找出不在main的元素
     *
     * @param main
     * @param diff
     * @return
     * @author songguang.jiao
     * @date 2020/05/21 17:46:03
     */
    private List<String> getDiff(List<String> main, List<String> diff) {
        Map<String, Integer> map = new HashMap<>(main.size());
        for (String string : main) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diff) {
            if (ConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }
}