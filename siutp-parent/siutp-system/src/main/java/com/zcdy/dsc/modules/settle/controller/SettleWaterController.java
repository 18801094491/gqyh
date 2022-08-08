package com.zcdy.dsc.modules.settle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.constant.ScaleConstant;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.settle.constant.MonthBalanceConstant;
import com.zcdy.dsc.modules.settle.entity.MeterAttr;
import com.zcdy.dsc.modules.settle.entity.SettleBatch;
import com.zcdy.dsc.modules.settle.entity.WaterDistrict;
import com.zcdy.dsc.modules.settle.param.MeterMonthOnePageParam;
import com.zcdy.dsc.modules.settle.param.SettleWaterParam;
import com.zcdy.dsc.modules.settle.service.ISettleService;
import com.zcdy.dsc.modules.settle.service.MeterAttrService;
import com.zcdy.dsc.modules.settle.service.SettleBatchService;
import com.zcdy.dsc.modules.settle.service.WaterDistrictService;
import com.zcdy.dsc.modules.settle.vo.CustomerMeterInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 描述: 客户水表管理 @author： 智能无人硬核心项目组 创建时间： 2020-02-17 09:24:36
 */
@Api(tags = "客户水表管理")
@Validated
@RestController
@RequestMapping("/settle/waterPrice")
public class SettleWaterController {

    @Resource
    private ISettleService settleService;

    @Resource
    private WaterDistrictService waterDistrictService;

    @Resource
    private MeterAttrService meterAttrService;

    @Resource
    private SettleBatchService settleBatchService;
    
    /**
     * 项目访问基础路径
     */
    @Value("${com.zcdy.dsc.file.request.domain}")
    private String baseStoragePath;

    /**
     * 水表管理列表查询
     *
     * @return
     */
    @AutoLog(value = "水表管理列表查询-分页列表查询")
    @ApiOperation(value = "水表管理列表查询-分页列表查询", notes = "水表管理列表-分页列表查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
            @ApiImplicitParam(name = "waterSn", value = "编号", paramType = "query"),
            @ApiImplicitParam(name = "waterName", value = "名称", paramType = "query")})
    @GetMapping
    public Result<IPage<CustomerMeterInfoVo>> queryPageList(SettleWaterParam param) {
        Result<IPage<CustomerMeterInfoVo>> result = new Result<>();
        Page<CustomerMeterInfoVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<CustomerMeterInfoVo> data = settleService.queryWaterPageData(page, param);
        List<CustomerMeterInfoVo> list = data.getRecords();
        for (CustomerMeterInfoVo customerMeterInfoVo : list) {
            if(StringUtils.isNotEmpty(customerMeterInfoVo.getModelImg())){
                customerMeterInfoVo.setModelImg(baseStoragePath+customerMeterInfoVo.getModelImg());
            }
        }
        result.setResult(data);
        result.success("查询成功");
        return result;
    }

    /**
     * 描述: 抄表详情列表 @author: songguang.jiao 创建时间: 2020年4月20日 下午2:06:08 版本: V1.0
     */
    @ApiOperation(value = "抄表详情列表", notes = "抄表详情列表")
    @GetMapping("/queryMeterMonth")
    public Result<IPage<SettleBatch>> queryMeterMonth(@Valid MeterMonthOnePageParam param) {
        Result<IPage<SettleBatch>> result = new Result<>();
        Page<SettleBatch> page = new Page<>(param.getPageNo(), param.getPageSize());
        QueryWrapper<SettleBatch> query = new QueryWrapper<>();
        query.lambda().eq(SettleBatch::getEquipmentId, param.getEquipmentId())
                .orderByDesc(SettleBatch::getCurrentFlowTime);
        IPage<SettleBatch> data = settleBatchService.page(page, query);
        if(data != null && data.getSize() > 0){
            for (SettleBatch sb : data.getRecords()){
                if(StringUtils.isNotBlank(sb.getCurrentFlow())){
                    sb.setCurrentFlow(new BigDecimal(sb.getCurrentFlow()).setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP).toEngineeringString());
                }
            }
        }
        result.setResult(data);
        return result.success("success");
    }

    /**
     * 描述: 水表绑定地区 @author: songguang.jiao 创建时间: 2020年4月21日 下午5:19:53 版本: V1.0
     */
    @ApiImplicitParams({@ApiImplicitParam(name = "equipmentId", value = "设备id", paramType = "query"),
            @ApiImplicitParam(name = "districtId", value = "地区id", paramType = "query")})
    @ApiOperation(value = "水表绑定地区", notes = "水表绑定地区")
    @GetMapping("/bindDistrict")
    public Result<Object> bindDistrict(String equipmentId, String districtId) {
        if (StringUtils.isEmpty(districtId) || StringUtils.isEmpty(equipmentId)) {
            return Result.error("水表跟地区不能为空");
        }
        waterDistrictService.bindDistrict(equipmentId, districtId);
        return Result.ok("绑定成功");
    }

    /**
     * 描述: 水表解除绑定地区 @author: songguang.jiao 创建时间: 2020年4月21日 下午5:40:38 版本: V1.0
     */
    @ApiImplicitParams({@ApiImplicitParam(name = "equipmentId", value = "设备id", paramType = "query"),
            @ApiImplicitParam(name = "districtId", value = "地区id", paramType = "query")})
    @ApiOperation(value = "水表解除绑定地区", notes = "水表解除绑定地区")
    @GetMapping("/unBindDistrict")
    public Result<Object> unBindDistrict(String equipmentId, String districtId) {
        if (StringUtils.isEmpty(districtId) || StringUtils.isEmpty(equipmentId)) {
            return Result.error("水表跟地区不能为空");
        }
        WaterDistrict waterDistrict = new WaterDistrict();
        waterDistrict.setValidStatus(StatusConstant.INVALID);
        UpdateWrapper<WaterDistrict> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(WaterDistrict::getDistrictId, districtId).eq(WaterDistrict::getEquipmentId,
                equipmentId);
        waterDistrictService.update(waterDistrict, updateWrapper);
        return Result.ok("解除绑定");
    }

    /**
     * 描述: 更改是否月结 @author: songguang.jiao 创建时间: 2020年4月26日 上午10:34:19 版本: V1.0
     */

    @ApiOperation(value = "更改是否月结", notes = "更改是否月结")
    @GetMapping("/remoteStatus")
    public Result<Object>
    monthBalance(@RequestParam(value = "equipmentId",required = false) @NotBlank(message = "水表id不能为空") String equipmentId) {
        MeterAttr meterAttr = new MeterAttr();
        LambdaQueryWrapper<MeterAttr> queryWrapper =
                Wrappers.lambdaQuery(meterAttr).eq(MeterAttr::getEquipmentId, equipmentId);
        MeterAttr updateWrapper = meterAttrService.getOne(queryWrapper);
        // 如果存在就修改,不存在就新增
        if (updateWrapper != null) {
            if (MonthBalanceConstant.MONTH_BALANCE.equals(updateWrapper.getMonthBalance())) {
                updateWrapper.setMonthBalance(MonthBalanceConstant.NOT_MONTH_BALANCE);
            } else {
                updateWrapper.setMonthBalance(MonthBalanceConstant.MONTH_BALANCE);
            }
            meterAttrService.update(updateWrapper, queryWrapper);
        } else {
            meterAttr.setEquipmentId(equipmentId);
            // 默认新增为月结
            meterAttr.setMonthBalance(MonthBalanceConstant.MONTH_BALANCE);
            meterAttrService.save(meterAttr);
        }
        return Result.ok("更改成功");
    }

}