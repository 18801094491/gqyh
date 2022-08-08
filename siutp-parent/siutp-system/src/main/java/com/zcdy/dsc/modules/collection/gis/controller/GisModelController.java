package com.zcdy.dsc.modules.collection.gis.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.gis.entity.GisEquipment;
import com.zcdy.dsc.modules.collection.gis.entity.GisModel;
import com.zcdy.dsc.modules.collection.gis.entity.ModelVariable;
import com.zcdy.dsc.modules.collection.gis.param.GisModelParam;
import com.zcdy.dsc.modules.collection.gis.service.*;
import com.zcdy.dsc.modules.collection.gis.vo.*;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IOTModelService;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessService;
import com.zcdy.dsc.modules.operation.alarm.vo.BusinessWarnVo;
import com.zcdy.dsc.modules.operation.alarm.vo.GisStatusWarnVo;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentAttrService;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentService;
import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentDetailModel;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 描述: GIS模型
 *
 * @author： 智能无人硬核心项目组
 * 创建时间： 2019-12-23
 * 版本号: V1.0
 */
@Api(tags = "GIS模型")
@RestController
@RequestMapping("/equipment/gisModel")
public class GisModelController extends BaseController<GisModel, IGisModelService> {
    @Autowired
    private IGisModelService gisModelService;

    @Autowired
    private IOptEquipmentService optEquipmentService;

    @Autowired
    private IOptEquipmentAttrService attrService;

    @Autowired
    private IGisEquipmentService gisEquipmentService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IOTModelService iotModelService;

    @Autowired
    private IModelVariableService iModelVariableService;

    @Autowired
    private IGisIotService igIsIotService;
    @Autowired
    private BusinessService businessService;

    @Autowired
    private GisService gisService;

    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "GIS模型-分页列表查询")
    @ApiOperation(value = "GIS模型-分页列表查询", notes = "GIS模型-分页列表查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "modelName", value = "模型名称", paramType = "query"),
            @ApiImplicitParam(name = "modelTypeCode", value = "模型类型", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query")})
    @GetMapping(value = "/list")
    public Result<IPage<GisModelVO>> queryPageList(String modelName, String modelType,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<GisModelVO> page = new Page<GisModelVO>(pageNo, pageSize);
        Result<IPage<GisModelVO>> result = new Result<IPage<GisModelVO>>();
        IPage<GisModelVO> pageList = gisModelService.queryPageList(page, modelName, modelType);
        List<GisModelVO> records = pageList.getRecords();
        for (GisModelVO gisModelVO : records) {
            if (gisModelVO != null) {
                gisModelVO.setModelImg(baseStoragePath + gisModelVO.getModelImg());
                gisModelVO.setModelWaringImg(baseStoragePath + gisModelVO.getModelWaringImg());
                gisModelVO.setModelOnImg(baseStoragePath + gisModelVO.getModelOnImg());
                gisModelVO.setModelOffImg(baseStoragePath + gisModelVO.getModelOffImg());
                //拼接类型跟编号
                if (gisModelVO.getModelName() != null && gisModelVO.getModelType() != null) {
                    gisModelVO.setTypeSn(gisModelVO.getModelType() + "[" + gisModelVO.getModelName() + "]");
                }
            }
        }
        result.setResult(pageList);
        return result.success("查询成功");
    }

    /**
     * 添加
     *
     * @return
     */
    @AutoLog(value = "GIS模型-添加")
    @ApiOperation(value = "GIS模型-添加", notes = "GIS模型-添加")
    @PostMapping(value = "/add")
    public Result<Object> add(@RequestBody GisModelParam gisModelParam) {


        Boolean exist = gisModelService.checkNameExist(null, gisModelParam.getModelName());
        if (exist) {
            return Result.error("GIS模型名称不能重复");
        }

        GisModel gisModel = new GisModel();
        //BeanUtil.copyProperties(gisModelParam, gisModel);
        gisModel.setModelName(gisModelParam.getModelName());
        gisModel.setModelLevel(gisModelParam.getModelLevel());
        gisModel.setModelTypeCode(gisModelParam.getModelType());
        gisModel.setModelOnImg(gisModelParam.getModelOnImg());
        gisModel.setModelOffImg(gisModelParam.getModelOffImg());
        gisModel.setModelWaringImg(gisModelParam.getModelWaringImg());
        gisModel.setModelOffset(gisModelParam.getModelOffset());
        gisModel.setModelPosition(gisModelParam.getModelPosition());
        gisModel.setLongitude(gisModelParam.getLongitude());
        gisModel.setLatitude(gisModelParam.getLatitude());
        gisModel.setDelFlag(DelFlagConstant.NORMAL);
        try {
            gisModelService.save(gisModel);
        } catch (Exception e) {
            e.getMessage();
        }
        return Result.ok("添加成功！");
    }

    /**
     * 删除
     *
     * @return
     */
    @AutoLog(value = "GIS模型-删除")
    @ApiOperation(value = "GIS模型-删除", notes = "GIS模型-删除")
    @PostMapping(value = "/delete")
    public Result<Object> delete(@RequestBody GisModelParam gisModelParam) {

        try {
            //判断是否存在绑定的数据
            Boolean existDetail = gisModelService.checkModelDetailExist(gisModelParam.getId());
            if(existDetail){
                return  Result.error("请先解绑变量数据！");
            }
            //判断是否存在绑定的数据
            GisEquipment gisEquipment=gisEquipmentService.getGisEquipmentByModelId(gisModelParam.getId());
            if(gisEquipment!=null && gisEquipment.getEquipmentId()!=null && gisEquipment.getEquipmentId().length()>0){
                return  Result.error("请先解绑设备数据！");
            }
            //处理GisEquipment中的多余数据
            if(gisEquipment!=null && (gisEquipment.getEquipmentId()==null||gisEquipment.getEquipmentId().length()==0)){
                gisEquipmentService.removeById(gisEquipment.getId());
            }
            gisModelService.removeById(gisModelParam.getId());
        } catch (Exception e) {
            e.getMessage();
        }
        return Result.ok("删除成功！");
    }

    /**
     * 编辑
     *
     * @return
     */
    @AutoLog(value = "GIS模型-编辑")
    @ApiOperation(value = "GIS模型-编辑", notes = "GIS模型-编辑")
    @PostMapping(value = "/edit")
    public Result<Object> edit(@RequestBody GisModelParam gisModelParam) {
        Boolean exist = gisModelService.checkNameExist(gisModelParam.getId(), gisModelParam.getModelName());
        if (exist) {
            return Result.error("GIS模型名称不能重复");
        }
        GisModel gisModel = new GisModel();
        //BeanUtil.copyProperties(gisModelParam, gisModel);
        gisModel.setId(gisModelParam.getId());
        gisModel.setModelLevel(gisModelParam.getModelLevel());
        gisModel.setModelName(gisModelParam.getModelName());
        gisModel.setModelTypeCode(gisModelParam.getModelType());
        gisModel.setModelOnImg(gisModelParam.getModelOnImg());
        gisModel.setModelOffImg(gisModelParam.getModelOffImg());
        gisModel.setModelWaringImg(gisModelParam.getModelWaringImg());
        gisModel.setModelOffset(gisModelParam.getModelOffset());
        gisModel.setModelPosition(gisModelParam.getModelPosition());
        gisModel.setLongitude(gisModelParam.getLongitude());
        gisModel.setLatitude(gisModelParam.getLatitude());
        gisModel.setDelFlag(DelFlagConstant.NORMAL);
        try {
            gisModelService.updateById(gisModel);
        } catch (Exception e) {
            e.getMessage();
        }
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "GIS模型-通过id查询")
    @ApiOperation(value = "GIS模型-通过id查询", notes = "GIS模型-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GisModelVO> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<GisModelVO> result = new Result<>();
        GisModelVO gisModelVO = gisModelService.getDetail(id);
        result.setResult(gisModelVO);
        return result.success("查询成功");
    }


    /**
     * GIS设备的模型数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "GIS设备的模型数据", notes = "GIS设备的模型数据 返回的status状态0为正常，1为报警，2为设备状态为报警但是没有取到报警事件，210为")
    @GetMapping(value = "/getDetail")
    public Result<GisStatusWarnVo> getDetail(@RequestParam(name = "id", required = true) String id) {
        EquipmentDetailModel eDetailModel = new EquipmentDetailModel();
        GisStatusWarnVo gisStatusWarnVo = new GisStatusWarnVo();
        // 中间表
        // 根据模型管理中的设备id，关联gis模型
        GisEquipment gisEquipment = gisEquipmentService.getGisEquipmentByModelId(id);
        if (gisEquipment == null) {
            gisStatusWarnVo.setStatus(CommonConstant.SC_OK_210 + "");
            return Result.success(gisStatusWarnVo, "GIS设备不存在");
        }

        OptEquipmentModel optEquipment = optEquipmentService.getDetail(gisEquipment.getEquipmentId());
        List<EquipmentAttrVO> queryAttrList = attrService.queryAttrList(gisEquipment.getEquipmentId());

        //根据变量查询所有的采集信息
        List<VariableInfo> vars = this.iotModelService.getVarsByModelId(id);
        List<IotDataVO> data = new ArrayList<>(vars.size());
        vars.forEach(item -> {
            String messageStr = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, item.getVarName()));
            if (!StringUtils.isEmpty(messageStr)) {

                ValueEntity value = JSON.parseObject(messageStr, ValueEntity.class);
                IotDataVO dataVO = new IotDataVO();
                dataVO.setVariableName(item.getVarTitle());

                BigDecimal bg = new BigDecimal(value.getValue());
                //使用小数位控制,为空的话,默认为2位
                int scale = item.getScale() == null ? 2 : item.getScale();
                if (scale >= 0) {
                    bg = bg.setScale(scale, RoundingMode.HALF_UP);
                }
                if (!StringUtils.isEmpty(item.getUnited())) {
                    dataVO.setVaribleValue(bg.toString() + "[" + item.getUnited() + "]");
                } else {
                    dataVO.setVaribleValue(bg.toString());
                }
                data.add(dataVO);
            }
        });
        eDetailModel.setEquipData(data);

        eDetailModel.setList(queryAttrList);
        eDetailModel.setOptEquipmentModel(optEquipment);
        String status = "";

        List<BusinessWarnVo> businessWarnVoList = businessService.getBusinessWarnVoById(id);
        if (businessWarnVoList != null && businessWarnVoList.size() > 0) {
            BusinessWarnVo businessWarnVo = businessWarnVoList.get(0);
            if(businessWarnVo != null)
            {
                gisStatusWarnVo.setBusinessWarnVo(businessWarnVo);
                status = gisModelService.getStatus(businessWarnVo.getIotId());
                if (businessWarnVo.getId() == null && "1".equals(status)) {
                    status = "2";
                }
            }
        }
        gisStatusWarnVo.setEquipmentDetailModel(eDetailModel);
        gisStatusWarnVo.setStatus(status);
        return Result.success(gisStatusWarnVo, "GIS设备的模型数据成功");
    }

    /**
     * 通过gisID查询实际地理位置
     *
     * @author songguang.jiao 2020年1月2日 下午3:09:29
     */
    @AutoLog
    @ApiOperation(value = "通过gisID查询实际地理位置", notes = "通过gisID查询实际地理位置")
    @GetMapping("/getLocation")
    public Result<GisLocationVo> getLocation(@RequestParam(value = "modelId", required = true) String modelId) {
        Result<GisLocationVo> result = new Result<>();
        // 查询中间表
        GisEquipment gisEquipment = gisEquipmentService.getGisEquipmentByModelId(modelId);
        if (gisEquipment == null) {
            result.setSuccess(true);
            result.setCode(CommonConstant.SC_OK_210);
            result.setMessage("未查询到相关地理位置");
            return result;
        }
        GisLocationVo location = optEquipmentService.getLocation(gisEquipment.getEquipmentId());
        result.setResult(location);
        return result.success("查询成功");
    }

    /**
     * 通过gisID查询实际地理位置
     * @param modelId gis模型id
     * @return
     */
    @AutoLog(value = "通过gisID查询实际地理位置")
    @ApiOperation(value = "通过gisID查询实际地理位置", notes = "通过gisID查询实际地理位置")
    @GetMapping("/app/getLocation")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<GisLocationVo> getAppLocation(@ApiParam(value = "gis模型id") @RequestParam(value = "modelId", required = true) String modelId) {
        ResultData<GisLocationVo> resultData = new ResultData<>();
        // 查询中间表
        GisEquipment gisEquipment = gisEquipmentService.getGisEquipmentByModelId(modelId);
        if (gisEquipment == null) {
            resultData.error500("未查询到相关地理位置");
            return resultData;
        }
        GisLocationVo location = optEquipmentService.getLocation(gisEquipment.getEquipmentId());
        resultData.setData(location);
        return resultData;
    }

    /**
     * 绑定gis跟变量关系
     * <p>
     * 描述:
     *
     * @author： songguang.jiao
     * 创建时间： 2020年1月6日 上午10:23:13
     * 版本号: V1.0
     */
    @AutoLog
    @ApiOperation(value = "绑定Gis模型跟变量", notes = "绑定Gis模型跟变量")
    @PostMapping("/bindGisIot")
    public Result<Object> bindGisIot(@RequestBody @Valid GisIotVo gIsIotVo) {
        Result<Object> result = new Result<>();
        // 查询变量是否绑定
        Integer bindStatus = igIsIotService.bindStatus(gIsIotVo.getVariableId());
        if (bindStatus > 0) {
            result.setCode(CommonConstant.SC_OK_210);
            result.setMessage("该变量已经被绑定");
            return result;
        }
        ModelVariable modelVariable = new ModelVariable();
        modelVariable.setModelId(gIsIotVo.getModelId());
        modelVariable.setVariableId(gIsIotVo.getVariableId());
        iModelVariableService.save(modelVariable);
        return Result.ok("Gis模型跟变量绑定成功");
    }

    /**
     * 查询Gis模型对应的已经绑定的变量
     * <p>
     * 描述:
     *
     * @author： songguang.jiao
     * 创建时间： 2020年1月6日 上午11:01:43
     * 版本号: V1.0
     */
    @AutoLog
    @ApiOperation(value = "查询Gis模型对应的已经绑定的变量", notes = "查询Gis模型对应的已经绑定的变量")
    @GetMapping("/getBindGisIotList")
    public Result<IPage<IotVariableInfoVo>> getBindGisIotList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String modelId) {
        Result<IPage<IotVariableInfoVo>> result = new Result<IPage<IotVariableInfoVo>>();
        Page<IotVariableInfoVo> page = new Page<IotVariableInfoVo>(pageNo, pageSize);
        IPage<IotVariableInfoVo> bindList = igIsIotService.getModelBindList(page, modelId);
        result.setResult(bindList);
        return result.success();
    }

    /**
     * 描述: 解除Gis模型跟变量绑定
     *
     * @author： songguang.jiao
     * 创建时间： 2020年1月6日 上午11:40:47
     * 版本号: V1.0
     */
    @AutoLog
    @ApiOperation(value = "解除Gis模型跟变量绑定", notes = "解除Gis模型跟变量绑定")
    @GetMapping("/unbindGisIot")
    public Result<?> unbindGisIot(String variableId) {
        igIsIotService.unBindIotVar(variableId);
        return Result.ok("Gis模型解绑变量成功");
    }

    /**
     * 描述: 查询所有变量列表
     *
     * @author： songguang.jiao
     * 创建时间： 2020年1月6日 上午11:53:00
     * 版本号: V1.0
     */
    @AutoLog
    @ApiOperation(value = "查询所有变量列表", notes = "查询所有变量列表")
    @GetMapping("/getIotInfoList")
    public Result<IPage<IotVariableInfoVo>> getIotInfoList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize, String variableName,
            String variableTitle) {
        Result<IPage<IotVariableInfoVo>> result = new Result<IPage<IotVariableInfoVo>>();
        Page<IotVariableInfoVo> page = new Page<IotVariableInfoVo>(pageNo, pageSize);
        IPage<IotVariableInfoVo> iotInfoList = igIsIotService.getIotInfoList(page, variableName, variableTitle);
        result.setResult(iotInfoList);
        return result.success();
    }


    /**
     * 描述: 查询模型库列表
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月3日 上午10:56:23
     * 版本号: V1.0
     */
    @AutoLog
    @ApiOperation(value = "查询模型库列表", notes = "查询模型库列表")
    @GetMapping("/getResSn")
    public Result<List<ResSnVo>> queryResSn(String modelType) {
        Result<List<ResSnVo>> result = new Result<List<ResSnVo>>();
        List<ResSnVo> data = gisService.queryResSn(modelType);
        result.setResult(data);
        return result.success();
    }

    @AutoLog
    @ApiOperation(value = "更新图片信息", notes = "更新图片信息")
    @PostMapping("/editImage")
    public Result<Object> editImage(@RequestBody @Valid GisImageVo gisImageVo) {
        if (gisImageVo.getType().endsWith("1")) {
            if (StringUtils.isEmpty(gisImageVo.getIds())) {
                return Result.error("GisId不能为空");
            }
        }
        gisService.editImage(gisImageVo);
        return Result.ok();
    }

    /**
     * 描述: 清除Redis緩存
     *
     * @author： songguang.jiao
     * 创建时间：  2020年3月21日 下午7:20:04
     * 版本号: V1.0
     */
    @ApiOperation(value = "清除Redis緩存", notes = "清除Redis緩存")
    @PostMapping("/clearRedis")
    public Result<Object> clearRedis() {
        Collection<String> list = Arrays.asList("com:zcdy:dsc:iot2gis::getIot2Gis", "com:zcdy:dsc:models::getAllGISModel"
                , "com:zcdy:dsc:vars::getAllVarinfoData");
        stringRedisTemplate.delete(list);
        String[] likeKey = new String[]{"com:zcdy:dsc:model:vars::*"};
        for (String keys : likeKey) {
            Set<String> keySet = this.stringRedisTemplate.keys(keys);
            this.stringRedisTemplate.delete(keySet);
        }
        return Result.ok("同步成功");
    }

    /**
     * 描述: 设置变量详情展示序号
     *
     * @author： songguang.jiao
     * 创建时间：  2020年3月23日 上午11:35:01
     * 版本号: V1.0
     */
    @ApiOperation(value = "设置变量详情展示序号", notes = "设置变量详情展示序号")
    @GetMapping("/setOrder")
    public Result<Object> setOrder(String id, String displayOrder) {
        if (StringUtils.isEmpty(displayOrder) || StringUtils.isEmpty(id)) {
            return Result.error("序号跟id不能为空");
        }
        UpdateWrapper<ModelVariable> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(ModelVariable::getVariableId, id);
        ModelVariable modelVariable = new ModelVariable();
        modelVariable.setDisplayOrder(displayOrder);
        iModelVariableService.update(modelVariable, updateWrapper);
        return Result.ok("设置成功");
    }

}
