package com.zcdy.dsc.modules.settle.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.constant.RedisKeyConstant;
import com.zcdy.dsc.modules.collection.gis.vo.IotDataVO;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IOTModelService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.settle.entity.SettleCustomer;
import com.zcdy.dsc.modules.settle.entity.SettleCustomerEquipment;
import com.zcdy.dsc.modules.settle.param.CustomerParam;
import com.zcdy.dsc.modules.settle.param.EquipmentMeterPageParam;
import com.zcdy.dsc.modules.settle.service.ISettleCustomerEquipmentService;
import com.zcdy.dsc.modules.settle.service.ISettleCustomerService;
import com.zcdy.dsc.modules.settle.service.ISettleService;
import com.zcdy.dsc.modules.settle.vo.CustomerVo;
import com.zcdy.dsc.modules.settle.vo.CustomerWaterInfoVo;
import com.zcdy.dsc.modules.settle.vo.EquipWatermeterVo;
import com.zcdy.dsc.modules.settle.vo.SettleCustomerEquipVo;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 描述: 客户信息管理 @author： 智能无人硬核心项目组 创建时间： 2020-01-02 版本号: V1.0
 */
@Api(tags = "客户信息管理")
@RestController
@RequestMapping("/settle/customer")
public class CustomerController extends BaseController<SettleCustomer, ISettleCustomerService> {

    @Autowired
    private ISettleService settleService;

    @Autowired
    private ISettleCustomerEquipmentService settleCustomerEquipService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IOTModelService iotModelService;

    /**
     * 分页列表查询
     * 
     * @return
     */
    @AutoLog(value = "客户信息管理-分页列表查询")
    @ApiOperation(value = "客户信息管理-分页列表查询", notes = "客户信息管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<CustomerVo>> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String customerSn, String customerName,
        String startTime, String endTime) {
        Page<CustomerVo> page = new Page<CustomerVo>(pageNo, pageSize);
        Result<IPage<CustomerVo>> result = new Result<IPage<CustomerVo>>();
        IPage<CustomerVo> list = settleService.getList(page, customerSn, customerName, startTime, endTime);
        result.setResult(list);
        return result.success();
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @AutoLog(value = "客户信息管理-添加")
    @ApiOperation(value = "客户信息管理-添加", notes = "客户信息管理-添加")
    @PostMapping(value = "/add")
    public Result<Object> add(@RequestBody CustomerParam customerParam) {
        SettleCustomer customer = new SettleCustomer();
        BeanUtil.copyProperties(customerParam, customer);
        customer.setDelFlag(DelFlagConstant.NORMAL);
        this.settleService.addCustomerInfo(customer);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param
     * @return
     */
    @AutoLog(value = "客户信息管理-编辑")
    @ApiOperation(value = "客户信息管理-编辑", notes = "客户信息管理-编辑")
    @PostMapping(value = "/edit")
    public Result<Object> edit(@RequestBody CustomerParam customerParam) {
        SettleCustomer customer = new SettleCustomer();
        BeanUtil.copyProperties(customerParam, customer);
        this.settleService.update(customer);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客户信息管理-通过id查询")
    @ApiOperation(value = "客户信息管理-通过id查询", notes = "客户信息管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<CustomerVo> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<CustomerVo> result = new Result<>();
        CustomerVo detail = this.settleService.getDetail(id);
        result.setResult(detail);
        return result.success();
    }

    /**
     * 导出excel
     *
     * @param
     * @param
     */
    @RequestMapping("/exportXls")
    public ModelAndView exportXls( String customerSn, String customerName, String startTime, String endTime) {
        IPage<CustomerVo> list = settleService.getList(new Page<>(), customerSn, customerName, startTime, endTime);

        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new Date());
        String secondTitle = "导出人:" + sysUser.getRealname() + "  导出时间：" + nowTime;
        mv.addObject(NormalExcelConstants.FILE_NAME, "客户信息管理");
        mv.addObject(NormalExcelConstants.CLASS, CustomerVo.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客户信息管理" + "报表", secondTitle, "客户信息管理"));
        mv.addObject(NormalExcelConstants.DATA_LIST, list.getRecords());
        return mv;
    }

    /**
     * 描述: 查询出所有水表信息 @auther： songguang.jiao 创建时间： 2020年1月6日 下午2:20:51 版本号: V1.0
     */
    @AutoLog
    @ApiOperation(value = "查询出所有水表信息", notes = "查询出所有水表信息")
    @GetMapping("/getWatermeterList")
    public Result<IPage<EquipWatermeterVo>> getWatermeterList(EquipmentMeterPageParam param) {
        Result<IPage<EquipWatermeterVo>> result = new Result<>();
        Page<EquipWatermeterVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<EquipWatermeterVo> watermeterList = settleService.getWatermeterList(page, param);
        result.setCode(CommonConstant.SC_OK_200);
        result.setSuccess(true);
        result.setResult(watermeterList);
        return result;
    }

    /**
     * 描述: 查询已绑定的动态水表信息 @auther： songguang.jiao 创建时间： 2020年1月6日 下午2:20:51 版本号: V1.0
     */
    @AutoLog
    @ApiOperation(value = "查询已绑定的动态水表信息", notes = "查询已绑定的动态水表信息")
    @GetMapping("/getBindWatermeterList")
    public Result<List<CustomerWaterInfoVo>>
        getBindWatermeterList(@RequestParam(name = "customerId", required = true) String customerId) {
        Result<List<CustomerWaterInfoVo>> result = new Result<>();
        List<CustomerWaterInfoVo> wateInfoList = settleService.getBindWateInfoList(customerId);
        // 遍历赋值 动态获取出口压力 使用状态 设备状态
        for (CustomerWaterInfoVo customerWaterInfoVo : wateInfoList) {
            if (customerWaterInfoVo.getModelId() != null) {
                List<VariableInfo> vnList = iotModelService.getVarsByModelId(customerWaterInfoVo.getModelId());
                Map<String, VariableInfo> vnMap = new HashMap<String, VariableInfo>(10);
                vnList.forEach(item -> {
                    vnMap.put(item.getVarName(), item);
                });
                List<IotDataVO> idos = new ArrayList<>();
                Set<String> keyset = stringRedisTemplate
                    .keys(String.format(RedisKeyConstant.PARENT_REDISDATAKEY, customerWaterInfoVo.getModelId()));
                for (String key : keyset) {
                    String messageStr = this.stringRedisTemplate.opsForValue().get(key);
                    if (StringUtils.isEmpty(messageStr)) {
                        continue;
                    }
                    ValueEntity value = JSON.parseObject(messageStr, ValueEntity.class);
                    VariableInfo varName = vnMap.get(value.getVariableName());
                    if (!StringUtils.isEmpty(varName.getVarName())) {
                        IotDataVO ido = new IotDataVO();
                        // 关联模型库查询
                        ido.setVariableName(varName.getVarName());
                        if (!StringUtils.isEmpty(varName.getUnited())) {
                            ido.setVaribleValue(value.getValue() + "[" + varName.getUnited() + "]");
                        } else {
                            ido.setVaribleValue(value.getValue());
                        }
                        idos.add(ido);
                    }
                }
                customerWaterInfoVo.setEquipData(idos);
            }
        }
        result.setCode(CommonConstant.SC_OK_200);
        result.setSuccess(true);
        result.setResult(wateInfoList);
        return result;
    }

    /**
     * 描述: 客户绑定水表 @auther： songguang.jiao 创建时间： 2020年1月6日 下午6:19:02 版本号: V1.0
     */
    @AutoLog
    @ApiOperation(value = "客户绑定水表", notes = "客户绑定水表")
    @PostMapping("/bindCustomerEquip")
    public Result<Object> bindCustomerEquip(@RequestBody @Valid SettleCustomerEquipVo customerEquipVo) {
        Result<Object> result = new Result<>();
        // 查询是否绑定
        Integer bindEquipStatus = settleService.getBindEquipStatus(customerEquipVo.getEquipmentId());
        if (bindEquipStatus > 0) {
            result.setCode(CommonConstant.SC_OK_210);
            result.setMessage("该水表已经被绑定");
            return result;
        }
        SettleCustomerEquipment eCustomerEquipment = new SettleCustomerEquipment();
        eCustomerEquipment.setCustomerId(customerEquipVo.getCustomerId());
        eCustomerEquipment.setEquipmentId(customerEquipVo.getEquipmentId());
        eCustomerEquipment.setEquipmentSn(customerEquipVo.getEquipmentSn());
        eCustomerEquipment.setBindTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        eCustomerEquipment.setBindStatus(ISettleCustomerEquipmentService.BIND_STATUS_BINDED);
        settleCustomerEquipService.save(eCustomerEquipment);
        return result.success("客户绑定水表信息成功");
    }

    /**
     * 描述: 解绑客户水表 @auther： songguang.jiao 创建时间： 2020年1月6日 下午6:19:02 版本号: V1.0
     */
    @AutoLog
    @ApiOperation(value = "解绑客户水表", notes = "解绑客户水表")
    @GetMapping("/unBindCustomerEquip")
    public Result<Object> unBindCustomerEquip(@RequestParam(name = "id", required = true) String id) {
        Result<Object> result = new Result<>();
        // 如果绑定合同,不能解绑,首先需要合同解绑水表
        Integer count= settleService.checkMeterBindContract(id);
        if(count>0){
            return result.error500("水表已经绑定合同,请先去合同管理解绑水表");
        }
        settleService.unBindCustomerEquip(id,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return result.success("解绑客户水表成功");
    }

}
