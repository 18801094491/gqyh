package com.zcdy.dsc.modules.operation.equipment.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.collection.gis.entity.GisEquipment;
import com.zcdy.dsc.modules.collection.gis.param.GisEquipmentParam;
import com.zcdy.dsc.modules.collection.gis.service.IGisEquipmentService;
import com.zcdy.dsc.modules.collection.gis.vo.EquipmentAttrVO;
import com.zcdy.dsc.modules.collection.gis.vo.IotDataVO;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IOTModelService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.constant.EquipmentStatus;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentLabel;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipment;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipmentDownLoad;
import com.zcdy.dsc.modules.operation.equipment.param.OptEquipmentAddParam;
import com.zcdy.dsc.modules.operation.equipment.service.EquipmentLabelService;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeService;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentAttrService;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentService;
import com.zcdy.dsc.modules.operation.equipment.vo.*;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeEquipData;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述: 设备资产
 * @author： 智能无人硬核心项目组
 * 创建时间： 2019-12-17
 * 版本号: V1.0
 */
@Slf4j
@Api(tags = "设备资产")
@RestController
@RequestMapping("/equipment/optEquipment")
public class OptEquipmentController extends BaseController<OptEquipment, IOptEquipmentService> {

    @Autowired
    private IOptEquipmentService optEquipmentService;

    @Autowired
    private IOptEquipmentAttrService attrService;

    @Resource
    private IGisEquipmentService gisEquipmentService;

    @Resource
    private IOTModelService iotModelService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IKnowlegeService knowlegeService;

    @Resource
    private EquipmentLabelService equipmentLabelService;


    /**
     * 分页列表查询
     * @param pageNo
     * @param pageSize
     * @param param
     * @return
     */
    @AutoLog(value = "设备资产-分页列表查询")
    @ApiOperation(value = "设备资产-分页列表查询", notes = "设备资产-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OptEquipmentModel>> queryPageList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, EquipmentQueryVO param) {
        Result<IPage<OptEquipmentModel>> result = new Result<IPage<OptEquipmentModel>>();
        Page<OptEquipmentModel> page = new Page<OptEquipmentModel>(pageNo, pageSize);
        IPage<OptEquipmentModel> list = optEquipmentService.getPageListByParam(page, param);
        result.setResult(list);
        result.success("查询成功");
        return result;
    }

    @AutoLog(value = "设备资产-获取监控设备列表")
    @ApiOperation(value = "设备资产-获取监控设备列表", notes = "设备资产-获取监控设备列表")
    @GetMapping(value = "/monitor/list")
    public Result<IPage<OptEquipmentModel>> monitorList(EquipmentQueryVO param) {
        Result<IPage<OptEquipmentModel>> result = new Result<>();
        Page<OptEquipmentModel> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<OptEquipmentModel> list = optEquipmentService.getMonitorEquipList(page, param);
        result.setResult(list);
        result.success("查询成功");
        return result;
    }

    /**
     * 添加
     *
     * @param param
     * @return
     */
    @AutoLog(value = "设备资产-添加")
    @ApiOperation(value = "设备资产-添加", notes = "设备资产-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("opt:add")
    public Result<Object> add(@RequestBody OptEquipmentAddParam param) {
        Boolean snExist = optEquipmentService.checkEquipSnExist(null, param.getEquipmentSn());
        if (snExist) {
            return Result.error("设备编号重复");
        }
        if(StringUtils.isNotEmpty(param.getHkMonitorCode()))
        {
            Boolean monitorCodeFlag = optEquipmentService.checkHkMonitorCode(param.getId(), param.getHkMonitorCode());
            if (monitorCodeFlag) {
                return Result.error("海康监控设备编码重复");
            }
        }
        optEquipmentService.saveOptEquipment(param);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param param
     * @return
     */
    @AutoLog(value = "设备资产-编辑")
    @ApiOperation(value = "设备资产-编辑", notes = "设备资产-编辑")
    @PostMapping(value = "/edit")
    @RequiresPermissions("opt:edit")
    public Result<Object> edit(@Valid @RequestBody OptEquipmentAddParam param) {
        Boolean snExist = optEquipmentService.checkEquipSnExist(param.getId(), param.getEquipmentSn());
        if (snExist) {
            return Result.error("设备编号重复");
        }
        if(StringUtils.isNotEmpty(param.getHkMonitorCode()))
        {
            Boolean monitorCodeFlag = optEquipmentService.checkHkMonitorCode(param.getId(), param.getHkMonitorCode());
            if (monitorCodeFlag) {
                return Result.error("海康监控设备编码重复");
            }
        }
        optEquipmentService.saveOptEquipment(param);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备资产-通过id查询")
    @ApiOperation(value = "设备资产-通过id查询", notes = "设备资产-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<OptEquipment> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<OptEquipment> result = new Result<OptEquipment>();
        OptEquipment optEquipment = optEquipmentService.getById(id);
        result.success("查询成功");
        result.setResult(optEquipment);
        return result;
    }

    /**
     * 修改启停用状态
     *
     * @param id
     * @return
     */
    @AutoLog(value = "修改启停用状态-通过id修改")
    @ApiOperation(value = "修改启停用状态-通过id修改", notes = "修改启停用状态-通过id修改")
    @GetMapping(value = "/queryRevstopById")
    public Result<Object> queryRevstopById(@RequestParam(name = "id", required = true) String id,
                                           @RequestParam(name = "state", required = true) String state) {
        OptEquipment equipment = new OptEquipment();
        equipment.setEquipmentRevstop(state);
        UpdateWrapper<OptEquipment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(OptEquipment::getId, id);
        optEquipmentService.update(equipment, updateWrapper);
        return Result.ok();
    }

    /**
     * 获取设备资产详情
     *
     * @param id
     * @return
     */
    @AutoLog(value = "获取设备资产详情")
    @ApiOperation(value = "获取设备资产详情", notes = "获取设备资产详情")
    @GetMapping(value = "/getDetail")
    public Result<EquipmentDetailModel> getDetail(@RequestParam(name = "id", required = true) String id) {
        Result<EquipmentDetailModel> result = new Result<>();
        EquipmentDetailModel eDetailModel = new EquipmentDetailModel();
        //资产信息
        OptEquipmentModel optEquipment = optEquipmentService.getDetail(id);
        if (!StringUtils.isEmpty(optEquipment.getEquipmentImg())) {
            optEquipment.setEquipmentImg(baseStoragePath + optEquipment.getEquipmentImg());
        }
        // 纵向的,展示不重要属性的
        List<EquipmentAttrVO> queryAttrList = attrService.queryAttrList(id);

        QueryWrapper<GisEquipment> qw = new QueryWrapper<>();
        qw.lambda().eq(GisEquipment::getEquipmentId, id);
        //获取gis模型关联的中间表
        GisEquipment gisEquipment = this.gisEquipmentService.getOne(qw);

        //判断是否绑定gis模型
        if (gisEquipment != null) {
            //根据变量查询所有的采集信息
            List<VariableInfo> vars = this.iotModelService.getVarsByModelId(gisEquipment.getModelId());
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
                        dataVO.setVaribleValue(bg.toEngineeringString());
                    }
                    data.add(dataVO);
                }
            });
            eDetailModel.setEquipData(data);
        }
        //根据设备类型,规格型号带出维保信息
        List<KnowlegeEquipData> knowlegeEquipData = optEquipmentService.queryKnowlegeData(optEquipment.getEquipmentTypeCode(), optEquipment.getEquipmentMode(), optEquipment.getEquipmentSpecs());
        eDetailModel.setKnowlegeEquipData(knowlegeEquipData);
        eDetailModel.setList(queryAttrList);
        eDetailModel.setOptEquipmentModel(optEquipment);
        result.success("获取设备资产详情成功");
        result.setResult(eDetailModel);
        return result;
    }

    /**
     * 查询gis模型 关联 设备下拉列表
     *
     * @param pageNo
     * @param pageSize
     * @param name
     * @return 版本号: V2.0   由资产设备页面做绑定改为在gis页面做绑定
     */
    @AutoLog(value = "查询设备关联gis模型下拉列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "gis模型名称", paramType = "query")})
    @ApiOperation(value = "查询gis模型 关联 设备下拉列表", notes = "查询gis模型 关联 设备下拉列表")
    @GetMapping("/getGisVoList")
    public Result<IPage<GisVo>> getGisVoList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String name) {
        Result<IPage<GisVo>> result = new Result<>();
        Page<GisVo> page = new Page<>(pageNo, pageSize);
        IPage<GisVo> gisVoList = optEquipmentService.getGisVoList(page, name);
        result.success("查询成功");
        result.setResult(gisVoList);
        return result;
    }

    /**
     * gis模型绑定
     *
     * @param gisEquipmentParam
     * @return
     */
    @AutoLog(value = "gis模型绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipmentId", value = "设备ID", paramType = "query"),
            @ApiImplicitParam(name = "modelId", value = "Gid模型Id", paramType = "query")
    })
    @ApiOperation(value = "gis模型绑定", notes = "gis模型绑定")
    @PostMapping("/bindGisData")
    public Result<Object> bindGisData(@RequestBody @Valid GisEquipmentParam gisEquipmentParam) {
        GisEquipment gisEquipment = gisEquipmentService.getGisEquipmentByModelId(gisEquipmentParam.getModelId());
        //不为空时修改中间表，为空时新增数据
        if (gisEquipment != null) {
         gisEquipmentService.removeById(gisEquipment.getId());
        }
            gisEquipment = new GisEquipment();
            gisEquipment.setEquipmentId(gisEquipmentParam.getEquipmentId());
            gisEquipment.setModelId(gisEquipmentParam.getModelId());
            gisEquipmentService.save(gisEquipment);

        return Result.ok("绑定成功");
    }


    /**
     * 解除gis模型绑定
     *
     * @return
     */
    @AutoLog(value = "解除gis模型绑定")
    @ApiOperation(value = "解除gis模型绑定", notes = "解除gis模型绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipmentId", value = "设备ID", paramType = "query")
    })
    @GetMapping("/unbindGisData")
    public Result<Object> unbindGisData(@RequestParam(required = true) String modelId) {
        gisEquipmentService.unbindGisData(modelId);
        return Result.ok("解除绑定成功");
    }

    /**
     * 描述: 获取知识库详情
     * @author：  songguang.jiao
     * 创建时间： 2020年2月27日 下午4:45:12
     * 版本号: V1.0
     */
    @ApiOperation(value = "获取知识库详情", notes = "获取知识库详情")
    @GetMapping(value = "/getKnowlege")
    public Result<KnowlegeVo> getKnowlege(@RequestParam(name = "id", required = true) String id) {
        Result<KnowlegeVo> result = new Result<KnowlegeVo>();
        KnowlegeVo knowlegeReturnVo = knowlegeService.selectInfo(id);
        result.setResult(knowlegeReturnVo);
        result.success("查询成功");
        return result;
    }


    /**
     * 描述: 导出
     * @author：  songguang.jiao
     * 创建时间： 2020年2月13日 下午5:33:28
     * 版本号: V1.0
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出", notes = "导出")
    @RequiresPermissions("opt:export")
    public ModelAndView export(EquipmentQueryVO param) {
        List<OptEquipmentModel> list = optEquipmentService.selectPageListByParam(param);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new Date());
        String secondTitle = "导出人:" + sysUser.getRealname() + "  导出时间：" + nowTime;
        mv.addObject(NormalExcelConstants.FILE_NAME, "设备信息");
        mv.addObject(NormalExcelConstants.CLASS, OptEquipmentModel.class);
        mv.addObject(NormalExcelConstants.PARAMS,
                new ExportParams("设备信息" + "报表", secondTitle, "设备信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }


    /**
     * 通过excel导入数据
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "导入", notes = "导入")
    @RequiresPermissions("opt:import")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result<Object> importExcel(HttpServletRequest request) {
        Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
        	// 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(false);
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!Lists.newArrayList("xls", "xlsx", "XLS", "XLSX").contains(extension)) {
                return Result.error("只能上传excel");
            }
            try {
                //加载供应商数据,加载数据字典,分类字典
                Map<String, String> supplierData = optEquipmentService.getSupplierData();
                Map<String, String> equipmentSectionData = optEquipmentService.getCategoryData("A04");
                Map<String, String> equipmentModeData = optEquipmentService.getCategoryData("A03");
                Map<String, String> equipmentCateData = optEquipmentService.getCategoryData("A01");
                List<OptEquipment> list = ExcelImportUtil.importExcel(file.getInputStream(), OptEquipment.class, params);
                for (OptEquipment optEquipment : list) {
                    String equipmentType = optEquipment.getEquipmentType();
                    if (equipmentModeData.containsKey(equipmentType)) {
                        optEquipment.setEquipmentType(equipmentModeData.get(equipmentType));
                    } else {
                        return Result.error("设备分类(" + equipmentType + ")填写错误,请先去分类管理页面添加设备分类");
                    }
                    String equipmentSection = optEquipment.getEquipmentSection();
                    if (equipmentSectionData.containsKey(equipmentSection)) {
                        optEquipment.setEquipmentSection(equipmentSectionData.get(equipmentSection));
                    } else {
                        return Result.error("所属标段(" + equipmentSection + ")填写错误,请先去分类管理页面添加所属标段");
                    }
                    String equipmentSupplier = optEquipment.getEquipmentSupplier();
                    if (!StringUtils.isEmpty(equipmentSupplier)) {
                        if (supplierData.containsKey(equipmentSupplier)) {
                            optEquipment.setEquipmentSupplier(supplierData.get(equipmentSupplier));
                        } else {
                            return Result.error("供应商(" + equipmentSupplier + ")填写错误,请先去供应商页面维护");
                        }
                    }
                    String equipmentCate = optEquipment.getEquipmentCategory();
                    if (StringUtils.isEmpty(equipmentCate)) {
                        return Result.error("设备类别不允许为空");
                    }
                    if (equipmentCateData.containsKey(equipmentCate)) {
                        optEquipment.setEquipmentCategory(equipmentCateData.get(equipmentCate));
                    } else {
                        return Result.error("设备类别(" + equipmentCate + ")填写错误,请先去分类管理页面添加设备类别");
                    }

                    //设置设备默认状态
                    optEquipment.setEquipmentStatus(EquipmentStatus.USED);
                    optEquipment.setEquipmentRevstop(WorkingStatus.WORKING);
                    optEquipment.setDelFlag(DelFlagConstant.NORMAL);
                }
                optEquipmentService.saveBatch(list);
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 描述: 下载导入模板
     * @author：  songguang.jiao
     * 创建时间： 2020年2月13日 下午5:33:28
     * 版本号: V1.0
     */
    @GetMapping("/downloadModel")
    @ApiOperation(value = "下载导入模板", notes = "下载导入模板")
    @RequiresPermissions("opt:downloadModel")
    public ModelAndView loadModel() {
        List<OptEquipmentDownLoad> list = new ArrayList<>();
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "设备台账导入模板");
        mv.addObject(NormalExcelConstants.CLASS, OptEquipmentDownLoad.class);
        mv.addObject(NormalExcelConstants.PARAMS,
                new ExportParams("设备台账导入模板", "备注：投入运营时间跟购置时间格式(yyyy-MM-dd);所属标段跟设备类型(见分类字典);供应商见供应商管理模块", "设备台账导入模板"));
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }


    /**
     * 添加
     *
     * @param param       上传设备类型图片
     * @return 1.图片上传, 获取图片 filepath
     * 2.根据设备类型,更新设备图片 equipment_img 字段 为filepath
     * String equipmentType = param.get("equipmentType");
     * if(StringUtils.isEmpty(equipmentType)){
     * return Result.error("设备类型不能为空.");
     * }
     * String fileImg = param.get("fileImg");
     */
    @AutoLog(value = "设备资产-类型图片上传修改")
    @ApiOperation(value = "设备资产-类型图片上传修改", notes = "设备资产-类型图片上传修改")
    @PostMapping(value = "/uploadEqTypeImg")
    @RequiresPermissions("opt:uploadImg")
    public Result<Object> uploadEquimentTypeImg(@RequestBody Map<String, String> param) {
        String equipmentType = param.get("equipmentType");
        if (StringUtils.isEmpty(equipmentType)) {
            return Result.error("设备类型不能为空.");
        }
        String fileImg = param.get("fileImg");
        UpdateWrapper<OptEquipment> update = new UpdateWrapper<>();

        SFunction<OptEquipment, String> getEquipmentCategory = OptEquipment::getEquipmentType;

        update.lambda().eq(getEquipmentCategory, equipmentType);

        OptEquipment optEquipment = new OptEquipment();
        String equipmentImg = "".equals(fileImg) ? "" : fileImg.replace(baseStoragePath, "");
        optEquipment.setEquipmentImg(equipmentImg);
        optEquipmentService.update(optEquipment, update);
        return Result.ok();
    }

    /**
     * tangchao
     * 设备绑定标签
     * @param equipmentId 设备id
     * @param labelId 标签id
     * @return 请求结果
     */
    @ApiImplicitParams({@ApiImplicitParam(name = "equipmentId", value = "设备id", paramType = "query"),
            @ApiImplicitParam(name = "labelId", value = "标签id", paramType = "query")})
    @ApiOperation(value = "设备绑定标签", notes = "设备绑定标签")
    @GetMapping("/bindLabel")
    public Result<Object> bindDistrict(String equipmentId, String labelId) {
        if (StringUtils.isEmpty(labelId) || StringUtils.isEmpty(equipmentId)) {
            return Result.error("水表跟标签不能为空");
        }
        equipmentLabelService.bindLabel(equipmentId, labelId);
        return Result.ok("绑定成功");
    }


    /**
     * tangchao
     * 设备绑定标签
     * @param equipmentId 设备id
     * @param labelId 标签id
     * @return 请求结果
     */
    @ApiImplicitParams({@ApiImplicitParam(name = "equipmentId", value = "设备id", paramType = "query"),
            @ApiImplicitParam(name = "districtId", value = "标签id", paramType = "query")})
    @ApiOperation(value = "水表解除绑定标签", notes = "水表解除绑定标签")
    @GetMapping("/unbindLabel")
    public Result<Object> unBindDistrict(String equipmentId, String labelId) {
        if (StringUtils.isEmpty(labelId) || StringUtils.isEmpty(equipmentId)) {
            return Result.error("水表跟标签不能为空");
        }
        EquipmentLabel equipmentLabel = new EquipmentLabel();
        equipmentLabel.setValidStatus(StatusConstant.INVALID);
        UpdateWrapper<EquipmentLabel> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(EquipmentLabel::getLabelId, labelId).eq(EquipmentLabel::getEquipmentId,
                equipmentId);
        equipmentLabelService.update(equipmentLabel, updateWrapper);
        return Result.ok("解除绑定成功");
    }

    /**
     * 所有设备下拉选
     * @param equipmentSn 设备编号
     * @return
     */
    @AutoLog(value = "设备下拉选")
    @ApiOperation(value = "所有设备下拉列表")
    @GetMapping("/dropdown")
    public Result<List<EquipmentDropdown>> dropdown(String equipmentSn){
        return Result.ok(optEquipmentService.equipDropdown(equipmentSn));

    }


}
