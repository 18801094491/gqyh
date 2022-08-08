package com.zcdy.dsc.modules.datacenter.statistic.controller;

import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.system.base.controller.AbstractBaseController;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IotDataService;
import com.zcdy.dsc.modules.collection.iot.vo.IotHistoryDataParam;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import com.zcdy.dsc.modules.datacenter.statistic.vo.CellData;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipment;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentService;
import com.zcdy.dsc.modules.system.service.ISysCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author： Roberto
 * 创建时间：2020年3月28日 下午5:36:56
 * 描述: <p>历史数据导出接口</p>
 */
@Api(tags="历史数据导出接口")
@Controller
@RequestMapping("anon/statistic/export")
public class HistoryDataExportController extends AbstractBaseController{

	@Resource
	private InfluxService influxService;
	
	@Resource
	private IotDataService iotDataService;
	
	@Resource
	private IOptEquipmentService optEquipmentService;
	
	@Resource
	private ISysCategoryService sysCategoryService;
	
	@Resource
	private ISysBaseApi sysBaseApi;
	
	private final int defaultPageSize = 5000;
	
	/**
	 * @author：Roberto
	 * @throws IOException 
	 * @create:2020年3月28日 下午7:29:24
	 * 描述:<p>导出历史采集数据</p>
	 */
	@ApiOperation(value = "导出采集数据", notes = "导出采集数据")
	@RequestMapping(value="/hisdata", method=RequestMethod.GET)
	public void exprot(IotHistoryDataParam param, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(null==param.getTimestamp()){
			return;
		}
		long timeGap = System.currentTimeMillis()-param.getTimestamp().longValue();
		if(timeGap<-60*1000L  || timeGap>60*1000L ){
			return;
		}
		// 如果参数无效
		if (StringUtils.isEmpty(param.getEquipmentId())){
			return;
		}

		// 根据设备获取设备的变量
		List<VariableInfo> varInfos = this.iotDataService.queryVariableInfoByEquipmentId(param.getEquipmentId());
		if(varInfos.size()==0){
			return;
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
		timestampCofnig.put("index", 0);
		timestampCofnig.put("title", "时间");
		headers.add(timestampCofnig);
		//记录表格头及对应字段
		Map<String, Integer> nameMap = new LinkedHashMap<>();
		int code = 1;
		for(String title : headerTitle){
			if(title.startsWith("AAA")){
				title = title.substring(3);
			}
			nameMap.put(title, code);
			Map<String, Object> one = new HashMap<>(2);
			one.put("index", code);
			String united = unitedMap.get(title);
			united = StringUtils.isEmpty(united)?"":("["+united+"]");
			one.put("title", title+united);
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
		
		int size = varNamesSqlParam.size(), pageSize = defaultPageSize*size;
		boolean hasNext = true;
		
		//查询资产信息
		OptEquipment bean = this.optEquipmentService.getDetailById(param.getEquipmentId());
		String excelTitle = bean.getEquipmentSection() + bean.getEquipmentLocation()+bean.getEquipmentCategory()+"["+bean.getEquipmentSn()+"]";
		String fileName = excelTitle+"-历史数据";
		
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		int pageNo = 1;
		while(hasNext){
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("select var_value from iot_point_data where ");
			sqlBuilder.append("(");
			int index = 0,rowNo = pageSize*(pageNo-1);
			for(;index < size; index++){
				if(index>0){
					sqlBuilder.append(" or ");
				}
				String item = varNamesSqlParam.get(index);
				sqlBuilder.append("var_name='");
				sqlBuilder.append(item);
				sqlBuilder.append("'");
			}
			
			sqlBuilder.append(") and ").append("time>=" + leftTimestamp +"ms and time<="+ rightTimestamp+"ms");
			
			sqlBuilder.append(" group by var_name").append(" order by time desc");
			sqlBuilder.append("  limit ");
			sqlBuilder.append(pageSize);
			sqlBuilder.append(" OFFSET ");
			sqlBuilder.append(rowNo);
			List<PointData> pointDatas = this.influxService.query(sqlBuilder.toString(), PointData.class);
			
			TreeMap<Long, List<CellData>> resultMap = new TreeMap<>(new Comparator<Long>() {
				@Override
				public int compare(Long o1, Long o2) {
					return o2.compareTo(o1);
				}
			});
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
		 			Long timeStampLong = Long.valueOf(timeStamp);
		 			List<CellData> item = resultMap.get(timeStampLong);
		 			if(null==item){
		 				item = new ArrayList<>();
		 				resultMap.put(timeStampLong, item);
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
		 			Integer key = nameMap.get(variableInfo.getVarTitle());
		 			String realValue = bigDecimal.setScale(scale, RoundingMode.HALF_UP).toString();
		 			if(one.getVarName().endsWith("#ZT")){
		 				String status = "无状态";
		 				switch (realValue) {
						case "0":
							status = "无状态";
							break;
						case "1":
							status = "全开";
							break;
						case "2":
							status = "全关";
							break;
						case "3":
							status = "本体故障";
							break;
						default:
							status = "无状态";
							break;
						}
		 				CellData cell = CellData.CELL_DATA.clone();
			 			cell.setIndex(key);
			 			cell.setValue(status);
			 			item.add(cell);
		 			}else{
		 				CellData cell = CellData.CELL_DATA.clone();
			 			cell.setIndex(key);
			 			cell.setValue(bigDecimal.setScale(scale, RoundingMode.HALF_UP).toString());
			 			item.add(cell);
		 			}
		 		}
		 	}
			
			//创建sheet
			Sheet sheet = workbook.createSheet();
			//创建标题
			Row title = sheet.createRow(0);
			title.setHeight((short) (64 * 20));
			int columns = headers.size();
			int columnEndIndex = columns-1;
			for(int columnNo=0; columnNo<columns; columnNo++){
				Cell cell = title.createCell(columnNo);
				if(columnNo==0){
					cell.setCellValue(excelTitle);
					CellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
					cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					Font font = workbook.createFont();
					font.setBoldweight(Font.BOLDWEIGHT_BOLD);
					font.setFontHeightInPoints((short) 16);
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);
				}
			}
			//计算跨列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnEndIndex));
			
			//创建到出人和导出时间
			String exporter = "未知";
			String username = JwtUtil.getUsername(param.getToken());
			if(!StringUtils.isEmpty(username)){
				LoginUser sysUser = sysBaseApi.getUserByName(username);
				if(null!=sysUser && null!=sysUser.getRealname() && sysUser.getStatus()==1){
					exporter = sysUser.getRealname();
				}
			}
			
			Row infoRow = sheet.createRow(1);
			int infoLeftEnd = (columns-1)/2;
			int infoRightStart = infoLeftEnd+1;
			for(int columnNo=0; columnNo<columns; columnNo++){
				Cell cell = infoRow.createCell(columnNo);
				if(columnNo == 0){
					cell.setCellValue("导出人："+ exporter);
				}
				if(columnNo == infoRightStart){
					SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
					cell.setCellValue("导出时间："+sf.format(new Date()));
				}
			}
			if(infoLeftEnd>0){
				sheet.addMergedRegion(new CellRangeAddress(1,1,0, infoLeftEnd));
			}
			if(columnEndIndex>infoRightStart){
				sheet.addMergedRegion(new CellRangeAddress(1,1,infoRightStart, columnEndIndex));
			}
			
			//创建表格头
			Row header = sheet.createRow(2);
			for(Map<String, Object> item : headers){
				int cellIndex = (int) item.get("index");
				Cell cell = header.createCell(cellIndex);
				cell.setCellValue((String)item.get("title"));
			}
			
			//创建数据区域
			int sheetRowNo = 3;
			Set<Entry<Long, List<CellData>>> entrySet = resultMap.entrySet();
			for(Entry<Long, List<CellData>> item : entrySet){
				Row row = sheet.createRow(sheetRowNo);
				Long timestamp = item.getKey();
				String timeCellTitle = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp/1000000L), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
				Cell cell = row.createCell(0);
				cell.setCellValue(timeCellTitle);
				List<CellData> datas = item.getValue();
				for(CellData data : datas){
					int cellIndex = data.getIndex();
					cell = row.createCell(cellIndex);
					cell.setCellValue(data.getValue());
				}
				sheetRowNo++;
			}
			sheet.setColumnWidth(0, (int)(256*(25 + 0.72)));
			
			//如果还有数据继续执行
			hasNext = pointDatas.size()==pageSize;
			pageNo++;
		}
		response.setContentType("application/octet-stream;charset=utf-8");
		if (isIe(request)) {
			try {
				fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException e) {
				//一定是支持utf-8的
			}
		} else {
			fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
		}
		response.setHeader("content-disposition", "attachment;filename="+fileName+".xlsx");
		ServletOutputStream outputstream = response.getOutputStream();
		workbook.write(outputstream);
		outputstream.flush();
		outputstream.close();
	}
	
	private boolean isIe(HttpServletRequest request) {
		return (request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0) ? true : false;
	}
}
