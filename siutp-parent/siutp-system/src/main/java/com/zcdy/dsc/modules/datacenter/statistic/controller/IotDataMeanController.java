package com.zcdy.dsc.modules.datacenter.statistic.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IotDataService;
import com.zcdy.dsc.modules.collection.iot.vo.IotHisDataVO;
import com.zcdy.dsc.modules.collection.iot.vo.IotHistoryDataMeanParam;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.influxdb.dto.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author： Roberto
 * 创建时间：2020年3月10日 下午3:30:28
 * 描述: <p>采集数据颗粒度查询处理</p>
 */
@Api(tags="采集数据历史数据颗粒度查询查询接口集(开始日期限制在2020-03-01之后,之前没有数据)")
@RestController
@RequestMapping("iot/vardata/mean")
public class IotDataMeanController {

	@Resource
	private IotDataService iotDataService;
	
	@Resource
	private InfluxService influxService;
	
	/**
	 * @author：Roberto
	 * @create:2020年3月10日 下午3:36:25
	 * 描述:<p>根据设备id查询变量信息</p>
	 */
	@ApiOperation(value="根据设备id查询变量信息", notes="根据设备id查询变量信息")
	@RequestMapping(value = "", method = { RequestMethod.POST })
	public Result<List<IotHisDataVO>> queryHisData(@RequestBody @ApiParam(value="请求体")IotHistoryDataMeanParam param) {
		String[] ids = param.getIds();
		// 如果参数无效
		if (ArrayUtils.isEmpty(ids)){
			return Result.fail("参数无效");
		}
		// 根据变量查询变量数据
		long leftTimestamp = 0;
		if(null==param.getLeft()){
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime plusHours = now.plusHours(-1);
			leftTimestamp = plusHours.toInstant(ZoneOffset.of("+8")).toEpochMilli();
		}else{
			leftTimestamp = param.getLeft().getTime();
		}
		
		long rightTimestamp = 0;
		if(null==param.getRight()){
			rightTimestamp = System.currentTimeMillis();
		}else{
			rightTimestamp = param.getRight().getTime();
		}
		
		int particle = param.getParticle().intValue(); 
		String particleUnit = param.getParticleUnit();
		
		List<IotHisDataVO> hisDatas = new ArrayList<>(ids.length);
		List<String> msgList = new ArrayList<>();
		for(String id : ids){
			// 根据设备获取设备的变量
			List<VariableInfo> varInfos = this.iotDataService.queryVariableInfoByEquipmentId(id);
			EquipmentInfoVO evo = this.iotDataService.queryEquipmentById(id);
			if(varInfos.size()==0){
				msgList.add(evo.getEquipmentName()+"未绑定变量信息");
				continue;
			}
			Map<String, VariableInfo> varMap = new HashMap<String, VariableInfo>(varInfos.size());
			List<String> varNamesSqlParam = new ArrayList<>();
			//记录变量单位
			Map<String, String> unitedMap = new LinkedHashMap<>();
			Set<String> headerTitle = new HashSet<>();
			varInfos.forEach(item -> {
				varMap.put(item.getVarName(), item);
				varNamesSqlParam.add(item.getVarName());
				headerTitle.add(item.getVarTitle());
				unitedMap.put(item.getVarTitle(), item.getUnited());
			});
			
			List<Map<String, Object>> headers = new ArrayList<>();
			Map<String, Object> timestampCofnig = new HashMap<>(2);
			timestampCofnig.put("title", "时间");
			timestampCofnig.put("data", "timestamp");
			headers.add(timestampCofnig);
			//记录表格头及对应字段
			Map<String, String> nameMap = new LinkedHashMap<>();
			int code = 0;
			for(String title : headerTitle){
				nameMap.put(title, "field_" + code);
				Map<String, Object> one = new HashMap<>(2);
				String united = unitedMap.get(title);
				united = StringUtils.isEmpty(united)?"":("["+united+"]");
				one.put("title", title+united);
				one.put("data", "field_" + code);
				headers.add(one);
				code++;
			}
			
			//判断周期
			List<PointData> pointDatas = executeQuery(leftTimestamp, rightTimestamp, varNamesSqlParam, particle, particleUnit);
			
			IotHisDataVO iotHisData = new IotHisDataVO();
			iotHisData.setHeader(headers);
			iotHisData.setTitle(evo.getEquipmentName());
			
			Map<String, Map<String, String>> resultMap = new HashMap<>(pointDatas.size());
			//执行查询
			if(pointDatas.size()>0){
				String nanoStr = "";
		 		for(PointData one : pointDatas){
		 			int nano =  one.getTime().getNano();
		 			nanoStr = String.valueOf(nano);
		 			if(nano<=0 || nanoStr.length()<9){
		 				nanoStr = StringUtils.rightPad(nanoStr, 9, '0');
		 			}
		 			String timeStamp = one.getTime().getEpochSecond() + nanoStr;
		 			Map<String, String> item = resultMap.get(timeStamp);
		 			if(null==item){
		 				item = new HashMap<>(pointDatas.size());
		 				resultMap.put(timeStamp, item);
		 			}
		 			VariableInfo variableInfo = varMap.get(one.getVarName());
		 			if(variableInfo==null){
		 				continue;
		 			}
		 			Integer scale = variableInfo.getScale();
		 			if(scale==null){
		 				scale=0;
		 			}
		 			if(StringUtils.isEmpty(one.getVarValue())){
		 				one.setVarValue("0.00");
		 			}
		 			BigDecimal bigDecimal = new BigDecimal(one.getVarValue());
		 			String key = nameMap.get(variableInfo.getVarTitle());
		 			item.put(key, bigDecimal.setScale(scale, RoundingMode.HALF_UP).toString());
		 			item.put("timestamp", LocalDateTime.ofInstant(one.getTime(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		 		}
		 	}
			TreeMap<String, Map<String, String>> resultTreeMap = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o2.compareTo(o1);
				}
			});
			resultTreeMap.putAll(resultMap);
		 	List<Map<String, String>> listResult = new ArrayList<Map<String, String>>(resultTreeMap.values());
		 	//第一条无数据填充0
		 	iotHisData.setData(listResult);
		 	iotHisData.setPageNo(1L);
		 	iotHisData.setTotal(0L);
		 	iotHisData.setPageSize(1L);
		 	
		 	hisDatas.add(iotHisData);
		}
		String msg = "";
		if(msgList.size()>0){
			msg = StringUtils.join(msgList, ",");
		}
		return Result.success(hisDatas, msg);
	}
	
	private final List<PointData> executeQuery(long leftTimestamp, long rightTimestamp, List<String> varNamesSqlParam, int particle, String unit){
		
		StringBuilder innerBuilder = new StringBuilder();
		StringBuilder whereBuilder = new StringBuilder();
		whereBuilder.append(" where (");
		
		int index=0,size = varNamesSqlParam.size();
		for(;index < size; index++){
			if(index>0){
				whereBuilder.append(" or ");
			}
			String item = varNamesSqlParam.get(index);
			whereBuilder.append("var_name='");
			whereBuilder.append(item);
			whereBuilder.append("'");
		}
		whereBuilder.append(") and")
    		.append(" time>")
            .append(leftTimestamp)
            .append("ms and time<")
            .append(rightTimestamp)
            .append("ms ");
		innerBuilder.append(whereBuilder.toString());
		
		StringBuilder sqlBuilder = new StringBuilder();
		
		List<PointData> pointDatas = null;
		switch(unit){
			case "s" : {
				sqlBuilder.append("select mean(var_value) as var_value from iot_point_data");
				sqlBuilder.append(innerBuilder.toString());
				sqlBuilder.append(" group by var_name, time("+particle+"s) fill(none)").append(" order by time desc");
				Query query = new Query(sqlBuilder.toString());
				pointDatas = this.influxService.query(query, PointData.class, "iot_point_data");
				break;
			}
			case "m" : {
				sqlBuilder.append("select mean(var_value) as var_value from iot_point_data");
				sqlBuilder.append(innerBuilder.toString());
				sqlBuilder.append(" group by var_name, time("+particle+"m) fill(none)").append(" order by time desc");
				Query query = new Query(sqlBuilder.toString());
				pointDatas = this.influxService.query(query, PointData.class, "iot_point_data");
				break;
			}
			case "h" : {
				sqlBuilder.append("select mean(avg_value) as var_value from iot_point_data_half_hour");
				sqlBuilder.append(innerBuilder.toString());
				sqlBuilder.append(" group by var_name, time("+particle+"h) fill(none)").append(" order by time desc");
				Query query = new Query(sqlBuilder.toString());
				pointDatas = this.influxService.query(query, PointData.class, "iot_point_data_half_hour");
				break;
			}
			case "d" : {
				sqlBuilder.append("select mean(avg_value) as var_value from iot_point_data_hour");
				sqlBuilder.append(innerBuilder.toString());
				sqlBuilder.append(" group by var_name, time("+particle+"d) fill(none)").append(" order by time desc");
				Query query = new Query(sqlBuilder.toString());
				pointDatas = this.influxService.query(query, PointData.class, "iot_point_data_hour");
				break;
			}
			case "M" : {
				sqlBuilder.append("select time, mean(avg_value) as var_value from iot_point_data_day");
				sqlBuilder.append(innerBuilder.toString());
				sqlBuilder.append(" group by var_name fill(none)").append(" order by time desc");
				Query query = new Query(sqlBuilder.toString());
				pointDatas = this.influxService.query(query, PointData.class, "iot_point_data_day");
				break;
			}
			case "y" : {
				sqlBuilder.append("select time, mean(avg_value) as var_value from iot_point_data_month");
				sqlBuilder.append(innerBuilder.toString());
				sqlBuilder.append(" group by var_name fill(none)").append(" order by time desc");
				Query query = new Query(sqlBuilder.toString());
				pointDatas = this.influxService.query(query, PointData.class, "iot_point_data_month");
				break;
			}
			default:{
				break;
			}
		}
		return pointDatas;
	}
}
