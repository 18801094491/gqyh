package com.zcdy.dsc.modules.rdp.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.discovery.util.StringUtil;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.map.entity.MapRiverInformation;
import com.zcdy.dsc.modules.rdp.entity.RdpMonitorPointConf;
import com.zcdy.dsc.modules.rdp.param.RdpMonitorPointConfPageParam;
import com.zcdy.dsc.modules.rdp.service.RdpMonitorPointConfService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.rdp.vo.RdpMonitorPointConfVo;
import lombok.extern.slf4j.Slf4j;
import com.zcdy.dsc.common.system.base.controller.BaseController;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 大屏重点监控配置控制器
 * @Author: 在信汇通
 * @Date:   2020-12-10 20:01:16
 * @Version: V1.0
 */
@Slf4j
@Api(tags="大屏重点监控配置控制器")
@RestController
@RequestMapping("/rdp/monitor-point")
public class RdpMonitorPointConfController extends BaseController<RdpMonitorPointConf, RdpMonitorPointConfService> {
	@Autowired
	private RdpMonitorPointConfService rdpMonitorPointConfService;
	
	/**
	 * 大屏重点监控配置-分页列表查询
	 * @param param 请求参数对象
	 * @return
	 */
	@AutoLog(value = "大屏重点监控配置-分页列表查询")
	@ApiOperation(value="大屏重点监控配置-分页列表查询", notes="大屏重点监控配置-分页列表查询")
	@GetMapping(value = "/confs")
	public ResultData<IPage<RdpMonitorPointConfVo>> getConfPageList(RdpMonitorPointConfPageParam param) {
		ResultData<IPage<RdpMonitorPointConfVo>> resultData = new ResultData<>();
		Page<RdpMonitorPointConfVo> page = new Page<>(param.getPageNo(), param.getPageSize());
		IPage<RdpMonitorPointConfVo> pageData = rdpMonitorPointConfService.queryPageList(page, param);
		resultData.setData(pageData);
		return resultData;
	}

	 /**
	  * 大屏重点监控配置-编辑
	  * @param monitorPointConf
	  * @return
	  */
	@AutoLog(value = "大屏重点监控配置-编辑")
	@ApiOperation(value="大屏重点监控配置-编辑", notes="大屏重点监控配置-编辑")
	@PatchMapping("/confs")
	public ResultData<RdpMonitorPointConf> update (@RequestBody RdpMonitorPointConf monitorPointConf) {
		ResultData<RdpMonitorPointConf> resultData = new ResultData<>();
		try {
			if (StringUtils.isEmpty(monitorPointConf.getId())) {
				resultData.error500("id不能为空");
				return resultData;
			}
			boolean flag = rdpMonitorPointConfService.updateMonitorPointConfById(monitorPointConf);
			if (flag) {
				resultData.success("操作成功");
			} else {
				resultData.error500("操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultData.error500("绑定失败");
		}
		return resultData;
	}
}