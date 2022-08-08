package com.zcdy.dsc.collector.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.collector.entity.IOServerStatus;
import com.zcdy.dsc.collector.ioserver.IOServerClientBean;
import com.zcdy.dsc.collector.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: Roberto
 * @CreateTime:2020年3月12日 上午11:09:58
 * @Description: <p>服务检测控制器</p>
 */
@Api("服务状态服务")
@RestController
@RequestMapping("iot/monitor")
public class ServerStatusController {

	@Resource
    private IOServerClientBean ioServerBean;
	
	@ApiOperation(value="获取IOServer状态", notes="获取系统采集系统ioserver的连接状态")
    @GetMapping (value = "/ioserver")
    public Result<IOServerStatus> getIoserverStatus() {
		int linkStatus = this.ioServerBean.getLinkStatus();
		int workStatus = this.ioServerBean.getWorkStatus();
		IOServerStatus status = new IOServerStatus();
		status.setLinkStatus(linkStatus);
		status.setWorkStatus(workStatus);
        return Result.success(status, "Operation successfully");
    }
}
