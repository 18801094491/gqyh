package com.zcdy.dsc.modules.system.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.system.entity.SysUserCheck;
import com.zcdy.dsc.modules.system.service.ISysUserService;
import com.zcdy.dsc.modules.system.service.SysUserCheckService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * 描述: 用户密码相关接口校验
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-14 11:08:22
 * 版本号: V1.0
 */
@Api(tags="用户密码更新时间")
@RestController
@RequestMapping("/system/userCheck")
public class SysUserCheckController extends BaseController<SysUserCheck, SysUserCheckService> {
	
	@Autowired
	private ISysUserService sysUserService;
	
	/**
	 * 描述: 校验用户密码是否超过30天
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月14日 上午10:06:43 
	 * 版本号: V1.0
	 */
	@ApiOperation(value="校验用户密码是否超过30天", notes=" 校验用户密码是否超过30天")
	@GetMapping("/password")
	public Result<Boolean> checkPasswordTime(){
		Result<Boolean> result=new Result<>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		Boolean bool= sysUserService.checkUserPasswordTime(sysUser.getUsername());
		result.setResult(bool);
		result.setCode(CommonConstant.SC_OK_200);
		return result;
	}
	
	
	

}