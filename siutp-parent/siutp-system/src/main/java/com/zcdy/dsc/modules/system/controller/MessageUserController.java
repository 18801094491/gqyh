package com.zcdy.dsc.modules.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.common.system.base.controller.AbstractPrincipalController;

import io.swagger.annotations.Api;

/**
 * 消息发送配置接口
 * @author Roberto
 * @date 2020/06/04
 */
@Api(tags="消息发送配置")
@RestController
@RequestMapping("/system/message/user")
public class MessageUserController implements AbstractPrincipalController {

    
}
