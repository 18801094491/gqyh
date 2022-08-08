package com.zcdy.dsc.modules.operation.equipment.logic;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.CaseFormat;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.constant.ScaleConstant;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentVariableVO;
import com.zcdy.dsc.modules.operation.equipment.constant.EquipIdConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.MeterFlow;
import com.zcdy.dsc.modules.operation.equipment.service.MeterFlowService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author： Roberto
 * 创建时间：2020年3月21日 下午5:17:53
 * 描述: <p>水表统计对service封装，在service之上</p>
 */
@Service("meterFlowLogic")
public class MeterFlowLogic {

	@Resource
	private MeterFlowService meterFlowService;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private ModuleVarService moduleVarService;
	
	
	@Resource
	private SystemParamService systemParamService;
	
	/**
	 * @author：Roberto
	 * @create:2020年3月21日 下午5:20:48
	 * 描述:<p>执行流量计当天数据的统计，并计入数据库表</p>
	 */
	public void executeStatisticToday(String moduleId) {
		// 查询所有水表和水表变量信息
		// 根据统计项目id和统计变量查询变量信息和对应资产信息
		List<EquipmentVariableVO> datas = this.moduleVarService.queryAllEquipAndVarByModuleId(moduleId);
		Map<String, EquipmentVariableVO> config = new HashMap<>(datas.size());
		Map<String, MeterFlow> meterMap = new HashMap<>(datas.size());
		Class<MeterFlow> clazz = MeterFlow.class;
		for(EquipmentVariableVO vo : datas){
			config.put(vo.getVariableName(), vo);
			MeterFlow  one = meterMap.get(vo.getEquipmentId());
			if(null==one){
				one = new MeterFlow();
				meterMap.put(vo.getEquipmentId(), one);
			}
			one.setEquipmentId(vo.getEquipmentId());
			String column = vo.getTableColumn();
			String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
			Field field = null;
			String valueEntityStr = this.stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, vo.getVariableName()));
			if(StringUtils.isEmpty(valueEntityStr)){
				continue;
			}
			ValueEntity entity = JSON.parseObject(valueEntityStr, ValueEntity.class);
			String value = entity.getValue(); 
			BigDecimal valueBg = new BigDecimal(value);
			int scale = vo.getScale()>0?vo.getScale():ScaleConstant.LL_SCALE_STORE;
			try {
				field = clazz.getDeclaredField(fieldName);
				if(null==field){
					continue;
				}
				field.setAccessible(true);
				field.set(one, valueBg.setScale(scale,RoundingMode.HALF_UP).toString());
				meterMap.put(vo.getEquipmentId(), one);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		Collection<MeterFlow> meters = meterMap.values();
		List<MeterFlow> needSaves = new ArrayList<MeterFlow>();
		Date now = new Date();
		
		for(MeterFlow one : meters){
			String equipmentId = one.getEquipmentId();
			//如果采集的正负累计流量为空的话,就默认采集值为0
			if(StringUtils.isEmpty(one.getPositiveFlow())){
				one.setPositiveFlow("0");
			}
			if(StringUtils.isEmpty(one.getNavigateFlow())){
				one.setNavigateFlow("0");
			}
			BigDecimal posBg = new BigDecimal(one.getPositiveFlow());
			BigDecimal nvgBg = new BigDecimal(one.getNavigateFlow());
			BigDecimal netBg = posBg.add(nvgBg);
			
			// 先删除当天数据防止重复数据出现
			meterFlowService.deleteMeterFlowByDateAndEquipmentId(equipmentId);
			// 查询数据库前一天的用户用水数据
			MeterFlow todayMeterFlow = new MeterFlow();
			todayMeterFlow.setNavigateFlow(nvgBg.setScale(ScaleConstant.LL_SCALE_STORE,RoundingMode.HALF_UP).toString());
			todayMeterFlow.setPositiveFlow(posBg.setScale(ScaleConstant.LL_SCALE_STORE,RoundingMode.HALF_UP).toString());
			todayMeterFlow.setEquipmentId(equipmentId);
			todayMeterFlow.setStaticsDate(now);
			todayMeterFlow.setNetFlow(netBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
			todayMeterFlow.setPositiveFlowDay("0");
			todayMeterFlow.setNavigateFlowDay("0");
			todayMeterFlow.setNetFlowDay("0");
			todayMeterFlow.setStaticsDate(now);
			todayMeterFlow.setStaticsTime(now);
			
			BigDecimal positiveFlowDay = BigDecimal.ZERO;
			BigDecimal navigateFlowDay = BigDecimal.ZERO;
			BigDecimal netFlowDay = BigDecimal.ZERO;
			
			//获取昨天的数据
			MeterFlow meter = meterFlowService.getMeterFlowByDateAndEquipmentId(equipmentId);
			//如果能取到值就说明该水表有历史记录值
			if (null!=meter) {
				// 昨日正向累计流量
				String forValueOld = meter.getPositiveFlow();
				BigDecimal yestodayPosBg = new BigDecimal(forValueOld);
				// 昨日正向累计流量
				String revValueOld = meter.getNavigateFlow();
				BigDecimal yestodayNvgBg = new BigDecimal(revValueOld);
				//正向累计量和0做比较，如果不大于0则认为没取到数据，则正向累计量需要用前一天的数据
				//负向累计量和0做比较，如果不大于0则认为没取到数据，则负向累计量需要用前一天的数据
				int aa=posBg.compareTo(BigDecimal.ZERO);
				int bb=nvgBg.compareTo(BigDecimal.ZERO);
				if(aa==0&&bb==0){
					//累计量用前一天的数据
					posBg=yestodayPosBg;
					//累计量用前一天的数据
					nvgBg=yestodayNvgBg;
				}
				netBg = posBg.add(nvgBg);
				positiveFlowDay = posBg.subtract(yestodayPosBg);
				navigateFlowDay = nvgBg.subtract(yestodayNvgBg);
				netFlowDay = positiveFlowDay.add(navigateFlowDay);
				//负向累计
				todayMeterFlow.setNavigateFlow(nvgBg.setScale(ScaleConstant.LL_SCALE_STORE,RoundingMode.HALF_UP).toString());
				//正向累计
				todayMeterFlow.setPositiveFlow(posBg.setScale(ScaleConstant.LL_SCALE_STORE,RoundingMode.HALF_UP).toString());
				//净用累计
				todayMeterFlow.setNetFlow(netBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
				//正向当天
				todayMeterFlow.setPositiveFlowDay(positiveFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
				//负向当天
				todayMeterFlow.setNavigateFlowDay(navigateFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
				//净用当天
				todayMeterFlow.setNetFlowDay(netFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
				// 如果取不到数据证明该水表是第一次录入
			}
			needSaves.add(todayMeterFlow);
		}
		meterFlowService.saveBatch(needSaves);
	}
	
	/**
	 * @author：Roberto
	 * @create:2020年3月21日 下午5:20:48
	 * 描述:<p>执行水表昨日数据的统计，并计入数据库表</p>
	 */
	public void executeStatisticYestoday(String moduleId) {
		// 查询所有水表和水表变量信息
		// 根据统计项目id和统计变量查询变量信息和对应资产信息
		List<EquipmentVariableVO> datas = this.moduleVarService.queryAllEquipAndVarByModuleId(moduleId);
		Map<String, EquipmentVariableVO> config = new HashMap<>(datas.size());
		Map<String, MeterFlow> meterMap = new HashMap<>(datas.size());
		Class<MeterFlow> clazz = MeterFlow.class;
		for(EquipmentVariableVO vo : datas){
			config.put(vo.getVariableName(), vo);
			MeterFlow  one = meterMap.get(vo.getEquipmentId());
			if(null==one){
				one = new MeterFlow();
				meterMap.put(vo.getEquipmentId(), one);
			}
			one.setEquipmentId(vo.getEquipmentId());
			String column = vo.getTableColumn();
			String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
			Field field = null;
			String valueEntityStr = this.stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, vo.getVariableName()));
			if(StringUtils.isEmpty(valueEntityStr)){
				continue;
			}
			ValueEntity entity = JSON.parseObject(valueEntityStr, ValueEntity.class);
			String value = entity.getValue(); 
			BigDecimal valueBg = new BigDecimal(value);
			int scale = vo.getScale()>0?vo.getScale():ScaleConstant.LL_SCALE_STORE;
			try {
				field = clazz.getDeclaredField(fieldName);
				if(null==field){
					continue;
				}
				field.setAccessible(true);
				field.set(one, valueBg.setScale(scale,RoundingMode.HALF_UP).toString());
				meterMap.put(vo.getEquipmentId(), one);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		Collection<MeterFlow> meters = meterMap.values();
		List<MeterFlow> needSaves = new ArrayList<MeterFlow>();
		Date now = new Date();
		
		LocalDate today = LocalDate.now();
		LocalDate yestoday = today.plusDays(-1);
		LocalDate prevYestoday = today.plusDays(-2);
		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		for(MeterFlow one : meters){
			String equipmentId = one.getEquipmentId();
			//如果采集的正负累计流量为空的话,就默认采集值为0
			if(StringUtils.isEmpty(one.getPositiveFlow())){
				one.setPositiveFlow("0");
			}
			if(StringUtils.isEmpty(one.getNavigateFlow())){
				one.setNavigateFlow("0");
			}
			BigDecimal posBg = new BigDecimal(one.getPositiveFlow());
			BigDecimal nvgBg = new BigDecimal(one.getNavigateFlow());
			BigDecimal netBg = posBg.add(nvgBg);
			
			// 先删除昨天数据防止重复数据出现
			String yestodyStr = yestoday.format(ofPattern);
			meterFlowService.deleteMeterByDateAndEquipmentId(equipmentId, yestodyStr);
			
			// 查询数据库昨天的用户用水数据
			MeterFlow yestodayMeterFlow = new MeterFlow();
			yestodayMeterFlow.setNavigateFlow(nvgBg.setScale(ScaleConstant.LL_SCALE_STORE,RoundingMode.HALF_UP).toString());
			yestodayMeterFlow.setPositiveFlow(posBg.setScale(ScaleConstant.LL_SCALE_STORE,RoundingMode.HALF_UP).toString());
			yestodayMeterFlow.setEquipmentId(equipmentId);
			yestodayMeterFlow.setNetFlow(netBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
			yestodayMeterFlow.setPositiveFlowDay("0");
			yestodayMeterFlow.setNavigateFlowDay("0");
			yestodayMeterFlow.setNetFlowDay("0");
			yestodayMeterFlow.setStaticsDate(Date.from(yestoday.atStartOfDay().toInstant(ZoneOffset.of("+8"))));
			yestodayMeterFlow.setStaticsTime(now);
			
			BigDecimal positiveFlowDay = BigDecimal.ZERO;
			BigDecimal navigateFlowDay = BigDecimal.ZERO;
			BigDecimal netFlowDay = BigDecimal.ZERO;
			
			//获取前天的数据
			MeterFlow meter = meterFlowService.getMeterByDateAndEquipmentId(equipmentId, prevYestoday.format(ofPattern));
			//如果能取到值就说明该水表有历史记录值
			if (null!=meter) {
				// 昨日正向累计流量
				String forValueOld = meter.getPositiveFlow();
				BigDecimal yestodayPosBg = new BigDecimal(forValueOld);
				// 昨日正向累计流量
				String revValueOld = meter.getNavigateFlow();
				BigDecimal yestodayNvgBg = new BigDecimal(revValueOld);
				//正向累计量和0做比较，如果不大于0则认为没取到数据，则正向累计量需要用前一天的数据
				//负向累计量和0做比较，如果不大于0则认为没取到数据，则负向累计量需要用前一天的数据
				int aa=posBg.compareTo(BigDecimal.ZERO);
				int bb=nvgBg.compareTo(BigDecimal.ZERO);
				if(aa==0&&bb==0){
					posBg=yestodayPosBg;
					nvgBg=yestodayNvgBg;
				}
				netBg = posBg.add(nvgBg);
				positiveFlowDay = posBg.subtract(yestodayPosBg);
				navigateFlowDay = nvgBg.subtract(yestodayNvgBg);
				netFlowDay = positiveFlowDay.add(navigateFlowDay);
				//负向累计
				yestodayMeterFlow.setNavigateFlow(nvgBg.setScale(ScaleConstant.LL_SCALE_STORE,RoundingMode.HALF_UP).toString());
				//正向累计
				yestodayMeterFlow.setPositiveFlow(posBg.setScale(ScaleConstant.LL_SCALE_STORE,RoundingMode.HALF_UP).toString());
				//净用累计
				yestodayMeterFlow.setNetFlow(netBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
				//正向当天
				yestodayMeterFlow.setPositiveFlowDay(positiveFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
				//负向当天
				yestodayMeterFlow.setNavigateFlowDay(navigateFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
				//净用当天
				yestodayMeterFlow.setNetFlowDay(netFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
				// 如果取不到数据证明该水表是第一次录入
			}
			needSaves.add(yestodayMeterFlow);
		}
		meterFlowService.saveBatch(needSaves);
	}
	
	
	/**
	 * 描述: 只产生河东水厂数据
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月14日 下午5:08:35
	 * 版本: V1.0
	 */
	public void executeHdFlow(String moduleId) {
		// 查询所有水表和水表变量信息
		// 根据统计项目id和统计变量查询变量信息和对应资产信息
		List<EquipmentVariableVO> datas = this.moduleVarService.queryAllEquipAndVarByModuleId(moduleId);
		Map<String, EquipmentVariableVO> config = new HashMap<>(datas.size());
		Map<String, MeterFlow> meterMap = new HashMap<>(datas.size());
		Class<MeterFlow> clazz = MeterFlow.class;
		for(EquipmentVariableVO vo : datas){
			//不是河东水厂的暂时不统计
			if(EquipIdConstant.HDSC.equals(vo.getEquipmentId())){
				config.put(vo.getVariableName(), vo);
				MeterFlow  one = meterMap.get(vo.getEquipmentId());
				if(null==one){
					one = new MeterFlow();
					meterMap.put(vo.getEquipmentId(), one);
				}
				one.setEquipmentId(vo.getEquipmentId());
				String column = vo.getTableColumn();
				String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
				Field field = null;
				String valueEntityStr = this.stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, vo.getVariableName()));
				if(StringUtils.isEmpty(valueEntityStr)){
					continue;
				}
				ValueEntity entity = JSON.parseObject(valueEntityStr, ValueEntity.class);
				String value = entity.getValue(); 
				BigDecimal valueBg = new BigDecimal(value);
				int scale = vo.getScale()>0?vo.getScale():ScaleConstant.LL_SCALE_STORE;
				try {
					field = clazz.getDeclaredField(fieldName);
					if(null==field){
						continue;
					}
					field.setAccessible(true);
					field.set(one, valueBg.setScale(scale,RoundingMode.HALF_UP).toString());
					meterMap.put(vo.getEquipmentId(), one);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}else{
				continue;
			}
		}
		
		Collection<MeterFlow> meters = meterMap.values();
		List<MeterFlow> needSaves = new ArrayList<MeterFlow>();
		Date now = new Date();
		for(MeterFlow one : meters){
			String equipmentId = one.getEquipmentId();
			//如果没有河东再生水累计流量就设置为空
			if(StringUtils.isEmpty(one.getNetFlow())){
				one.setNetFlow("0");
			}
			BigDecimal netBg = new BigDecimal(one.getNetFlow());
			// 先删除当天数据防止重复数据出现
			meterFlowService.deleteMeterFlowByDateAndEquipmentId(equipmentId);
			// 查询数据库前一天的用户用水数据
			MeterFlow todayMeterFlow = new MeterFlow();
			todayMeterFlow.setNavigateFlow("0");
			todayMeterFlow.setPositiveFlow("0");
			todayMeterFlow.setEquipmentId(equipmentId);
			todayMeterFlow.setStaticsDate(now);
			todayMeterFlow.setNetFlow(netBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
			todayMeterFlow.setPositiveFlowDay("0");
			todayMeterFlow.setNavigateFlowDay("0");
			todayMeterFlow.setNetFlowDay("0");
			todayMeterFlow.setStaticsDate(now);
			todayMeterFlow.setStaticsTime(now);
			
			BigDecimal netFlowDay = BigDecimal.ZERO;
			//获取昨天的数据
			String preDate=LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			QueryWrapper<MeterFlow> queryWrapper=new QueryWrapper<>();
			queryWrapper.lambda().eq(MeterFlow::getEquipmentId, equipmentId).eq(MeterFlow::getStaticsDate, preDate);
			MeterFlow meter = meterFlowService.getOne(queryWrapper);
			//如果能取到值就说明该水表有历史记录值
			if (null!=meter) {
				netFlowDay = new BigDecimal(todayMeterFlow.getNetFlow()).subtract(new BigDecimal(meter.getNetFlow()));
				//净用当天
				todayMeterFlow.setNetFlowDay(netFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
				// 如果取不到数据证明该水表是第一次录入
			}
			needSaves.add(todayMeterFlow);
		}
		meterFlowService.saveBatch(needSaves);
	}
	
}
