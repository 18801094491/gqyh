package com.zcdy.dsc.modules.datacenter.statistic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.datacenter.statistic.entity.Chart;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartItem;
import com.zcdy.dsc.modules.datacenter.statistic.param.ChartParam;
import com.zcdy.dsc.modules.datacenter.statistic.service.ChartItemService;
import com.zcdy.dsc.modules.datacenter.statistic.service.ChartService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

 /**
 * 描述: 统计事项
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13 15:36:28
 * 版本号: V1.0
 */
@Api(tags="统计事项结果集")
@RestController
@RequestMapping("/statistic/chart")
public class ChartController extends BaseController<Chart, ChartService> {
	
	@Resource
	private ChartService chartService;
	
	@Resource
	private ChartItemService chartItemService;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	/**
	 * 分页列表查询
	 *
	 * @param chart
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "统计事项-分页列表查询")
	@ApiOperation(value="统计事项-分页列表查询", notes="统计事项-分页列表查询")
	@GetMapping
	public Result<Page<ChartVo>> queryPageList(ChartParam param) {
		Page<ChartVo> page = new Page<ChartVo>(param.getPageNo(), param.getPageSize());
		Page<ChartVo> pageList = (Page<ChartVo>) chartService.pageQuery(page, param);
		return Result.success(pageList, "operation success");
	}
	
	/**
	 * @author：Roberto
	 * @create:2020年3月14日 上午11:50:35
	 * 描述:<p>保存图表信息</p>
	 */
	@AutoLog(value = "保存图表信息")
	@ApiOperation(value="保存图表信息", notes="保存图表信息")
	@PostMapping(value = "one")
	public Result<String> saveChart(@RequestBody ChartItemVO param){
		Chart chart;
		if(!StringUtils.isEmpty(param.getId())){
			chart = this.chartService.getById(param.getId());
			BeanUtils.copyProperties(param, chart);
			this.chartService.updateById(chart);
			QueryWrapper<ChartItem> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(ChartItem::getStatisticId, chart.getId());
			this.chartItemService.remove(queryWrapper);
		}else{
			chart = new Chart();
			BeanUtils.copyProperties(param, chart);
			if(StringUtils.isEmpty(chart.getCycleTime())){
				chart.setCycleTime("0");
			}
			this.chartService.save(chart);
		}
		List<ItemVO> itemVos = param.getItems();
		List<ChartItem> chartItems = new ArrayList<>();
		for(ItemVO vo : itemVos){
			vo.getSerials().forEach(item->{
				ChartItem chartItem = new ChartItem();
				chartItem.setStatisticId(chart.getId());
				chartItem.setOptId(vo.getEquipmentId());
				chartItem.setSerialName(item.getSerialName());
				chartItem.setVariableName(item.getVariableName());
				chartItems.add(chartItem);
			});
		}
		this.chartItemService.saveBatch(chartItems);
		return Result.success("", "success");
	}
	
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "统计事项-通过id删除")
	@ApiOperation(value="统计事项-通过id删除", notes="统计事项-通过id删除")
	@GetMapping(value = "del/{id}")
	public Result<String> delete(@PathVariable("id") String id) {
		chartService.removeById(id);
		return Result.success("", "success");
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "统计事项-通过id查询")
	@ApiOperation(value="统计事项-通过id查询", notes="统计事项-通过id查询")
	@GetMapping(value = "/{id}")
	public Result<ChartItemVO> queryById(@PathVariable("id") String id) {
		Chart chart = chartService.getById(id);
		QueryWrapper<ChartItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ChartItem::getStatisticId, chart.getId());
		List<ChartItem> chartItems = this.chartItemService.list(queryWrapper);
		ChartItemVO vo = new ChartItemVO();
		BeanUtils.copyProperties(chart, vo);
		
		Map<String, ItemVO> map = new HashMap<String, ItemVO>(chartItems.size());
		chartItems.forEach(item->{
			SerialVO serialVo = new SerialVO();
			serialVo.setSerialName(item.getSerialName());
			serialVo.setVariableName(item.getVariableName());
			
			ItemVO itemVO = map.get(item.getOptId());
			if(null==itemVO){
				itemVO = new ItemVO();
				map.put(item.getOptId(), itemVO);
			}
			itemVO.setEquipmentId(item.getOptId());
			List<SerialVO> serialVos = itemVO.getSerials();
			if(null==serialVos){
				serialVos = new ArrayList<>();
				itemVO.setSerials(serialVos);
			}
			serialVos.add(serialVo);
		});
		
		vo.setItems(new ArrayList<>(map.values()));
		return Result.success(vo, "success");
	}
	
	/**
	 * 通过id启用停用
	 * @param id
	 * @return
	 */
	@AutoLog(value = "通过id启用停用")
	@ApiOperation(value="通过id启用停用", notes="通过id启用停用")
	@GetMapping(value = "sts/{id}")
	public Result<String> changeStatus(@PathVariable("id") String id) {
		Chart chart = chartService.getById(id);
		if(chart.getStatisticStatus().equals(StatusConstant.RUN +"")){
			chart.setStatisticStatus(StatusConstant.STOP +"");
		}else{
			chart.setStatisticStatus(StatusConstant.RUN +"");
		}
		this.chartService.updateById(chart);
		return Result.success("", "success");
	}
	
	@AutoLog(value = "获取设备资产列表")
	@ApiOperation(value="获取设备资产列表", notes="获取设备资产列表")
	@GetMapping(value = "opt")
	public Result<List<EquipmentInfoVO>> getEquipmentData(String name) {
		List<EquipmentInfoVO> datas = this.chartService.queryEquipment(name);
		return Result.success(datas, "success");
	}
	
	@AutoLog(value = "根据资产id获取采集变量")
	@ApiOperation(value="根据资产id获取采集变量", notes="根据资产id获取采集变量")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", dataType="string", paramType="path", required=true)
	})
	@GetMapping(value = "/{id}/var")
	public Result<List<VarNameTitleVO>> getVarinfoByOptId(@PathVariable String id){
		List<VarNameTitleVO> datas = this.chartService.queryVarByOptId(id);
		return Result.success(datas, "success");
	}
	
	
	/**
	 * 描述: 清除Redis缓存
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月21日 下午7:06:43
	 * 版本号: V1.0
	 */
	@ApiOperation(value="清除Redis缓存", notes="清除Redis缓存")
	@PostMapping(value = "clearRedis")
	public Result<Object> clearRedis() {
		//删除缓存
		Set<String> keys = stringRedisTemplate.keys("com:zcdy:dsc:modules:statistic:service:impl:ChartItemServiceImpl:*");
		stringRedisTemplate.delete(keys);
		return Result.ok("清除成功");
	}
}