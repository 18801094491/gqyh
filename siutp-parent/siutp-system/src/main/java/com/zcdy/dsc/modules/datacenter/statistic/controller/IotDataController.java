package com.zcdy.dsc.modules.datacenter.statistic.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IotDataService;
import com.zcdy.dsc.modules.collection.iot.vo.IotHisDataVO;
import com.zcdy.dsc.modules.collection.iot.vo.IotHistoryDataParam;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author: Roberto
 * 创建时间：2020年3月10日 下午3:30:28
 * 描述: <p>采集数据查询处理</p>
 * 	<p>历史数据查询要联合分表查询，目前一个月产生一张表；根据查询的时间区间自动联合要查询的表</p>
 */
@Api(tags="采集数据历史查询接口集(开始日期限制在2020-03-01之后,之前没有数据)")
@RestController
@RequestMapping("iot/vardata")
public class IotDataController {

	@Resource
	private IotDataService iotDataService;
	
	@Resource
	private InfluxService influxService;
	
	private final int defaultPageSize = 20;

	/**
	 * @auther：Roberto
	 * @create:2020年3月10日 下午3:36:25
	 * 描述:<p>根据设备id查询变量信息</p>
	 */
	@ApiOperation(value="根据设备id查询变量信息", notes="根据设备id查询变量信息")
	@RequestMapping(value = "", method = { RequestMethod.POST })
	public Result<IotHisDataVO> queryHisData(@RequestBody IotHistoryDataParam param) {

		// 如果参数无效
		if (StringUtils.isEmpty(param.getEquipmentId())){
			return Result.fail("参数无效");
		}

		// 根据设备获取设备的变量
		List<VariableInfo> varInfos = this.iotDataService.queryVariableInfoByEquipmentId(param.getEquipmentId());
		if(varInfos.size()==0){
			return Result.fail("采集设备未绑定变量信息");
		}
		Map<String, VariableInfo> varMap = new HashMap<String, VariableInfo>(varInfos.size());
		List<String> varNamesSqlParam = new ArrayList<>();
		//记录变量单位
		Map<String, String> unitedMap = new LinkedHashMap<>();
		Set<String> headerTitle = new TreeSet<>();
		varInfos.forEach(item -> {
			varMap.put(item.getVarName(), item);
			varNamesSqlParam.add(item.getVarName());
			//对阀门特殊处理
			if(item.getVarName().endsWith("#ZT")){
				headerTitle.add("AAA"+item.getVarTitle());
			}else{
				headerTitle.add(item.getVarTitle());
			}
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
			if(title.startsWith("AAA")){
				title = title.substring(3);
			}
			nameMap.put(title, "field_" + code);
			Map<String, Object> one = new HashMap<>(2);
			String united = unitedMap.get(title);
			united = StringUtils.isEmpty(united)?"":("["+united+"]");
			one.put("title", title+united);
			one.put("data", "field_" + code);
			headers.add(one);
			code++;
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
		
		StringBuilder countSqlBuilder = new StringBuilder();
		countSqlBuilder.append("select count(var_value) from iot_point_data");
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select var_value from iot_point_data");
		
		StringBuilder whereBuilder = new StringBuilder();
		whereBuilder.append(" where (");
		
		int index=0,size = varNamesSqlParam.size(),offset = 0;
		Integer pagesize = param.getPageSize();
		if(null==pagesize){
			pagesize = defaultPageSize;
		}
		int pageNo = param.getPageNo()==null?1:param.getPageNo().intValue(); 
		offset = pagesize*(pageNo-1);
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
		.append(" time>=")
        .append(leftTimestamp)
        .append("ms and time<=")
        .append(rightTimestamp)
        .append("ms");
		
		countSqlBuilder.append(whereBuilder.toString());
		sqlBuilder.append(whereBuilder.toString());
		sqlBuilder.append(" group by var_name").append(" order by time desc");
		sqlBuilder.append("  limit ");
		sqlBuilder.append(pagesize);
		sqlBuilder.append(" OFFSET ");
		sqlBuilder.append(offset);
		
		Long total = 0L;
		QueryResult sizeResult= this.influxService.query(countSqlBuilder.toString());
		List<QueryResult.Result> results = sizeResult.getResults();
		if(null!=results && results.get(0)!=null && results.get(0).getSeries()!=null){
			total =  new Double((double) results.get(0).getSeries().get(0).getValues().get(0).get(1)).longValue() ;
		}
		
		List<PointData> pointDatas = this.influxService.query(sqlBuilder.toString(), PointData.class);
		
		IotHisDataVO iotHisData = new IotHisDataVO();
		iotHisData.setHeader(headers);
		
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
	 			if(one.getVarName().endsWith("#ZT")){
	 				String realValue = bigDecimal.setScale(0, RoundingMode.HALF_UP).toString();
	 				switch (realValue) {
					case "0":
						item.put(key, "无状态");
						break;
					case "1":
						item.put(key, "全开");
						break;
					case "2":
						item.put(key, "全关");
						break;
					case "3":
						item.put(key, "本体故障");
						break;
					default:
						item.put(key, "无状态");
						break;
					}
	 			}else{
	 				String realValue = bigDecimal.setScale(scale, RoundingMode.HALF_UP).toString();
	 				item.put(key, realValue);
	 			}
	 			item.put("timestamp", LocalDateTime.ofInstant(one.getTime(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	 		}
	 	}
		//resultMap
		TreeMap<String, Map<String, String>> resultTreeMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		});
		resultTreeMap.putAll(resultMap);
	 	List<Map<String, String>> listResult = new ArrayList<Map<String, String>>(resultTreeMap.values());
	 	iotHisData.setData(listResult);
	 	iotHisData.setPageNo(param.getPageNo().intValue()*1L);
	 	iotHisData.setTotal(total/size);
	 	iotHisData.setPageSize(pagesize*1L);
		return Result.success(iotHisData, "success");
	}
	
	@ApiOperation(value="设备列表接口", notes="根据设备类型查询设备")
	 @ApiImplicitParams({
	  @ApiImplicitParam(value="设备类型编码", dataType="string", paramType="path", name="typeCode"),
	  @ApiImplicitParam(value="设备名称", dataType="string", paramType="query",name="name"),
	  @ApiImplicitParam(value="是否周期月结", dataType="string", paramType="query",name="name")
	 })
	 @GetMapping("/equip/{typeCode}")
	 public Result<List<EquipmentInfoVO>> getEquipmentInfoById(@PathVariable String typeCode, String name, String monthBalance){
	  List<EquipmentInfoVO> list = this.iotDataService.queryEquipment(typeCode,name,monthBalance);
	  return Result.success(list, "success");
	 }
}
