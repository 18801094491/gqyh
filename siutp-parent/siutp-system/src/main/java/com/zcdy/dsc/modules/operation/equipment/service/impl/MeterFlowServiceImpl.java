package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.datacenter.statistic.vo.MeterFlowMonthPriceVo;
import com.zcdy.dsc.modules.operation.equipment.entity.MeterFlow;
import com.zcdy.dsc.modules.operation.equipment.mapper.MeterFlowMapper;
import com.zcdy.dsc.modules.operation.equipment.service.MeterFlowService;
import com.zcdy.dsc.modules.operation.equipment.vo.MeterFlowVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 描述: 水表日累计量信
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-19
 * 版本号: V1.0
 */
@Service("meterFlowService")
public class MeterFlowServiceImpl extends ServiceImpl<MeterFlowMapper, MeterFlow> implements MeterFlowService {
   
	@Resource
    MeterFlowMapper meterFlowMapper;
	
    @Override
    public MeterFlow getMeterFlowByDateAndEquipmentId(String equipmentId) {
    	LocalDate today = LocalDate.now();
    	today = today.plusDays(-1);
    	String prevDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return meterFlowMapper.getMeterFlowByDateAndEquipmentId(equipmentId, prevDate);
    }

    @Override
    public void deleteMeterFlowByDateAndEquipmentId(String equipmentId) {
        meterFlowMapper.deleteMeterFlowByDateAndEquipmentId(equipmentId);
    }

    @Override
    public List<MeterFlow> querynetFlowDay(String daycount,String staticsDateEnd) {
        return meterFlowMapper.querynetFlowDay(daycount,staticsDateEnd);
    }

    @Override
    public List<MeterFlowVo> queryAllNetFlowDay() {
        //获取当前年月
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM");
        String nowMonth=sdf.format(new Date());
        //获取上月年月
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String lastMonth=sdf.format(m);
        //获取当前年份
        SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy");
        String nowYear=sdf1.format(new Date());
        return meterFlowMapper.queryAllNetFlowDay(nowMonth,lastMonth,nowYear);
    }


	@Override
	public IPage<MeterFlowMonthPriceVo> queryAllWaterMonth(Page<MeterFlowMonthPriceVo> page, String customerId) {
		return this.meterFlowMapper.queryAllWaterMonth(page,customerId);
	}

	@Override
	public List<Map<String, Number>> queryAllDateWaterFlow(String customerId, String startDate, String endDate) {
    	Map<String, String> param = new HashMap<>(4);
    	param.put("customerId",customerId);
    	param.put("startDate",startDate);
    	param.put("endDate",endDate);
    	param.put("now", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());

		return this.meterFlowMapper.queryAllDateWaterFlow(param);
	}

	@Override
	public String queryMeterRsLh(List<String> list, LocalDate localDate) {
		return meterFlowMapper.queryRsLs(list,localDate);
	}

	/*
	 * @see com.zcdy.dsc.modules.operation.equipment.service.MeterFlowService#deleteMeterByDateAndEquipmentId(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteMeterByDateAndEquipmentId(String equipmentId, String yestodyStr) {
		meterFlowMapper.deleteMeterByDateAndEquipmentId(equipmentId, yestodyStr);
	}

	/*
	 * @see com.zcdy.dsc.modules.operation.equipment.service.MeterFlowService#getMeterByDateAndEquipmentId(java.lang.String, java.time.LocalDate)
	 */
	@Override
	public MeterFlow getMeterByDateAndEquipmentId(String equipmentId, String prevYestoday) {
        return meterFlowMapper.getMeterFlowByDateAndEquipmentId(equipmentId, prevYestoday);
	}

	@Override
	public Map<String, MeterFlow> queryByDate(String date) {
		LambdaQueryWrapper<MeterFlow> queryWrapper = Wrappers.lambdaQuery(new MeterFlow()).eq(MeterFlow::getStaticsDate, date);
		List<MeterFlow> list = meterFlowMapper.selectList(queryWrapper);
		Map<String, MeterFlow> map=new HashMap<String, MeterFlow>(list.size());
		for (MeterFlow meterFlow : list) {
			map.put(meterFlow.getEquipmentId(), meterFlow);
		}
		return map;
	}

}
