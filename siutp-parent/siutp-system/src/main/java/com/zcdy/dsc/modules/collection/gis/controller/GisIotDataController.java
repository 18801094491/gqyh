package com.zcdy.dsc.modules.collection.gis.controller;

import com.alibaba.fastjson.JSON;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.framework.redis.RedisOperation;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.base.controller.AbstractBaseController;
import com.zcdy.dsc.common.util.StringUtil;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.constant.VariableTypeConstant;
import com.zcdy.dsc.modules.collection.gis.entity.IotShowData;
import com.zcdy.dsc.modules.collection.gis.service.IGisModelService;
import com.zcdy.dsc.modules.collection.gis.vo.*;
import com.zcdy.dsc.modules.collection.gis.vo.GISModelMessage.MessageBody;
import com.zcdy.dsc.modules.collection.iot.constant.ModelStatusConstant;
import com.zcdy.dsc.modules.collection.iot.entity.Iot2GisEntity;
import com.zcdy.dsc.modules.collection.iot.entity.ModelStatusEntity;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IOTModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import javax.annotation.Resource;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取gis模型数据接口
 *
 * @author Roberto
 * @date 2020/05/12
 */
@Api(tags = "gis模型展示iot数据", value = "GisIotData")
@RestController
@RequestMapping("/gis/iot")
public class GisIotDataController extends AbstractBaseController {

    @Resource
    private IGisModelService iGisModelService;

    @Resource
    private IOTModelService iotModelService;

    @Resource(name = "stringRedisOperation")
    private RedisOperation<String> stringRedisOperation;

    @ApiOperation(value = "获取状态变化的设备状态信息")
    @ApiOperationSort(value = 2)
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @GetMapping("/iots/state")
    public Result<List<MessageBody>> getAllChangedStatusData() {
        // 通过资产找到采集模型
        List<Iot2GisEntity> iot2Gis = this.iotModelService.getIot2Gis();

        List<MessageBody> messages = new ArrayList<>(16);
        // 遍历所有的模型
        for (Iot2GisEntity model : iot2Gis) {
            // 根据模型查询所有设备的状况(正常、异常、开、关)
            String statusConstant = getLastestStatus(model.getIotId());
            if (null == statusConstant) {
                continue;
            }

            ModelStatusEntity statusEntity = JSON.parseObject(statusConstant, ModelStatusEntity.class);

            // 设备上次的状态
            String prevStatusConstant = getPrevstatus(model.getIotId());
            if (null != prevStatusConstant) {
                // 上次的状态
                ModelStatusEntity prevStatusEntity = JSON.parseObject(prevStatusConstant, ModelStatusEntity.class);
                // 设备状态没有发生改变
                if (statusEntity.getStatus().equals(ModelStatusConstant.STATUS_NORMAL)
                        && statusEntity.getDetailStatus().equals(ModelStatusConstant.STATUS_NORMAL_OPEN)
                        && prevStatusEntity.getStatus().equals(statusEntity.getStatus())
                        && prevStatusEntity.getDetailStatus().equals(statusEntity.getDetailStatus())) {
                    changePrevStatus(model.getIotId(), statusConstant);
                    continue;
                }
            }
            changePrevStatus(model.getIotId(), statusConstant);
            OperationSatus perationStatus = fillOperation(statusEntity);
            MessageBody body = new MessageBody();
            body.setId(model.getGisId());
            body.setOpration(perationStatus);
            messages.add(body);
        }
        return Result.success(messages, "Operation successfully!");
    }

    /**
     * 设备类型查询列表
     *
     * @param typeCode
     * @return
     * @author songguang.jiao
     * @date 2020/05/20 12:00:08
     */
    @ApiOperation(value = "通过设备类型编码查询对应设备信息列表",notes = "通过设备类型编码查询对应设备信息列表")
    @GetMapping("/list/{typeCode}")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<List<IotEquipInfoVo>> getEquipInfoByModelType(@PathVariable("typeCode") String typeCode) {
        Result<List<IotEquipInfoVo>> result = new Result<>();
        List<IotEquipInfoVo> data = iotModelService.getEquipInfoByModelType(typeCode);
        List<IotEquipInfoVo> ret = new ArrayList<>();
        if(data != null)
        {
            for (IotEquipInfoVo iotEquipInfoVo : data) {
                if(StringUtils.isNotEmpty(iotEquipInfoVo.getEquipmentName()))
                {
                    IotShowData iotShowData = this.getIotDataById(iotEquipInfoVo.getGisId());
                    iotEquipInfoVo.setShowData(StringUtil.isNotNull(iotShowData.getShowData()) ? iotShowData.getShowData() : "暂无");
                    ret.add(iotEquipInfoVo);
                }
            }
        }

        result.setResult(ret);
        return result.success();
    }

    /**
     * 通过GisId获取采集数据
     *
     * @param gisId
     * @return
     * @author songguang.jiao
     * @date 2020/05/20 13:20:18
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation("通过GisId获取采集数据")
    @GetMapping("/getIotData/{gisId}")
    public Result<MessageBody> getIotData(@PathVariable("gisId") String gisId) {
        Result<MessageBody> result = new Result<>();
        MessageBody body = new MessageBody();
        IotShowData iotData = this.getIotDataById(gisId);
        body.setData(iotData.getIotDataVos());
        body.setShowData(iotData.getShowData());
        result.setResult(body);
        return result.success();
    }

    /**
     * 通过gisId获取采集数据
     *
     * @param gisId
     * @return
     * @author songguang.jiao
     * @date 2020/05/22 14:33:27
     */
    private IotShowData getIotDataById(String gisId) {
        IotShowData iotShowData = new IotShowData();
        List<VariableInfo> vars = this.iotModelService.getVarsByModelId(gisId);
        List<IotDataVO> data = new ArrayList<>(vars.size());
        if(vars == null)
        {
            return iotShowData;
        }
        //vars.forEach(item -> {
        for(VariableInfo item : vars){
            String messageStr = this.stringRedisOperation.get(String.format(RedisKeyConstantV2.REDISDATAKEY, item.getVarName()));
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
                    // 采集值
                    String iotValue = bg.toEngineeringString();
                    if (!StringUtils.isEmpty(item.getUnited())) {
                        iotValue = iotValue + "[" + item.getUnited() + "]";
                    }
                    dataVO.setVaribleValue(iotValue);
                }
                if(item.getVariableType() != null) {
                    // 通过变量类型获取APP列表需要展示的值
                    switch (item.getVariableType()) {
                        case VariableTypeConstant.WH_SLUDGET: //水文监测-雷达测速仪数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WH_RAINGAUGE: //水文监测-雨量计数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WH_LEVEL: //水文监测-液位计数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WH_FLOWV: //水文监测-雷达测速仪数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WQ_TP: //水质监测-总磷数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WQ_TN: //水质监测-总氮数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WQ_TEMP: //水质监测-水温数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WQ_SS: //水质监测-浊度数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WQ_PH: //水质监测-PH数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WQ_NH4_N: //水质监测-氨氮数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WQ_EC: //水质监测-电导率数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WQ_DO: //水质监测-溶解氧数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        case VariableTypeConstant.WQ_COD: //水质监测-COD数据
                            iotShowData.setShowData(dataVO.getVaribleValue());
                            break;
                        default:
                            break;
                    }
                }
                data.add(dataVO);
            }
        }
        iotShowData.setIotDataVos(data);
        return iotShowData;
    }

    /**
     * @param iotId
     * @return 状态字符串
     */
    private String getLastestStatus(String iotId) {
        return stringRedisOperation.get(String.format(RedisKeyConstantV2.MODEL_LAST_STATUS_KEY, iotId));
    }

    /**
     * @param iotId
     * @return 状态字符串
     */
    private String getPrevstatus(String iotId) {
        return stringRedisOperation.get(String.format(RedisKeyConstantV2.MODEL_PREV_STATUS_KEY, iotId));
    }

    /**
     * 填充模型状态信息
     *
     * @param statusEntity
     * @return
     */
    private OperationSatus fillOperation(ModelStatusEntity statusEntity) {
        OperationSatus operationSatus = OperationSatus.OPERATION_SATUS.clone();
        operationSatus.setMessage(statusEntity.getMessage());
        operationSatus.setWorkStatus(statusEntity.getStatus());
        operationSatus.setSwitchSatus(statusEntity.getDetailStatus());
        return operationSatus;
    }

    /**
     * @author：Roberto
     * @create:2020年3月13日 上午9:35:10 描述:
     * <p>
     * 变更设备的上次状态
     * </p>
     */
    private synchronized void changePrevStatus(String iotId, String statusConstant) {
        // 将最新的状态记入上次状态
        stringRedisOperation.set(String.format(RedisKeyConstantV2.MODEL_PREV_STATUS_KEY, iotId), statusConstant);
    }

    /**
     * 微信小程序
     * @param typeCode
     * @return
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation("设备类型查询列表2")
    @GetMapping("/listNew/{typeCode}")
    public Result<List<IotEquipInfoVoNew>> getEquipInfoByModelTypeNew(@PathVariable("typeCode") String typeCode) {
        Result<List<IotEquipInfoVoNew>> result = new Result<>();
        List<IotEquipInfoVo> dataList = iotModelService.getEquipInfoByModelType(typeCode);
        List<IotEquipInfoVoNew> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            IotEquipInfoVoNew newEq = new IotEquipInfoVoNew();
            newEq.setGisId(dataList.get(i).getGisId());
            newEq.setEquipmentName(dataList.get(i).getEquipmentName());
            newEq.setShowData(dataList.get(i).getShowData());
            IotShowData iotShowData = this.getIotDataById(dataList.get(i).getGisId());
            List<IotDataVO> iotDataVOList = iotShowData.getIotDataVos();
            String context = "";
            if (iotDataVOList.size() > 0) {
                for (int j = 0; j < iotDataVOList.size(); j++) {
                    IotDataVO iotDataVos = new IotDataVO();
                    iotDataVos = iotDataVOList.get(j);
                    if(iotDataVos !=null && iotDataVos.getVariableName() !=null && iotDataVos.getVariableName().length()>0){
                        context = context + (iotDataVos.getVariableName().concat(":").concat(iotDataVos.getVaribleValue().toString()));
                    }
                }
            }
            newEq.setConText(context);
            list.add(newEq);
        }
        result.setResult(list);
        return result.success();
    }

    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation("获取全量标注信息")
    @GetMapping(value = "/dataNew", produces = "application/json;charset=utf-8")
    public Result<List<GisDataVo>> initAllGisDataNew() {
        Result<List<GisDataVo>> result = new Result<List<GisDataVo>>();
        List<GisModelImgVo> modelsList = iGisModelService.queryListByIn();
        List<GisDataVo> returnGisDataVo = new ArrayList<>();
        // 地图拼接全路径,为图片赋值宽跟高
        for (GisModelImgVo gisModelImgVo : modelsList) {
            GisDataVo gisDataVo =new GisDataVo();
            gisDataVo.setTitle(gisModelImgVo.getEquipLocation());
            gisDataVo.setGisId(gisModelImgVo.getId());
            String s=this.map_bd2tx(Double.valueOf(gisModelImgVo.getLatitude()),Double.valueOf(gisModelImgVo.getLongitude()));
            String[] strs = s.split(",");
            for(int i=0;i<strs.length;i++){
                gisDataVo.setLatitude(strs[0].toString());
                gisDataVo.setLongitude(strs[1].toString());
            }
            /**
             * 获取gis模型参数对应的数据
             */
            IotShowData iotShowData = this.getIotDataById(gisModelImgVo.getId());
            List<IotDataVO> iotDataVOList = iotShowData.getIotDataVos();
            String context = "";
            if (iotDataVOList.size() > 0) {
                for (int j = 0; j < iotDataVOList.size(); j++) {
                    IotDataVO iotDataVos = new IotDataVO();
                    iotDataVos = iotDataVOList.get(j);
                    context = context + (iotDataVos.getVariableName().concat(":").concat(iotDataVos.getVaribleValue().toString()));
                }
            }
            gisDataVo.setTitle(context);
            gisDataVo.setIotDataVos(iotDataVOList);
           /* gisDataVo.setImgUrl(gisModelImgVo.getModelOnImg().getImgUrl());*/
            /**
             * 获取设备状态并根据状态显示相应的gis图片
             */
            returnGisDataVo.add(gisDataVo);
        }

        result.setResult(returnGisDataVo);
        return result.success("ok");
    }
    /**
     * 坐标转换，百度地图坐标转换成腾讯地图坐标
     * @param lat  百度坐标纬度
     * @param lon  百度坐标经度
     * @return 返回结果：纬度,经度
     */
    private String map_bd2tx(double lat, double lon){
        double tx_lat;
        double tx_lon;
        double x_pi=3.14159265358979324;
        double x = lon - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        tx_lon = z * Math.cos(theta);
        tx_lat = z * Math.sin(theta);
        return tx_lat+","+tx_lon;
    }
}
