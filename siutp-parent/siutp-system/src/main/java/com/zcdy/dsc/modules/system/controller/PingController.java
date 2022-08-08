package com.zcdy.dsc.modules.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： Roberto
 * 创建时间：2020年1月13日 下午3:39:04
 * 描述: <p>心跳控制器，定时发送心跳可以让jwttoken定时刷新而不会过期</p>
 */
@RestController
@RequestMapping("sys/ping")
public class PingController {

	/**
	 * @author：Roberto
	 * @create:2020年1月13日 下午3:41:06
	 * 描述:<p>心跳</p>
	 */
	@GetMapping
	public String ping(){
		return "pong";
	}
}
