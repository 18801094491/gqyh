 package com.zcdy.dsc.common.system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.common.api.vo.Result;

import javax.servlet.http.HttpServletResponse;

 /**
  * @author： Roberto
  * 创建时间：2020年2月25日 上午10:10:14
  * 描述: <p>框架权限控制器</p>
  */
 @RestController
 @RequestMapping("/sys/common")
public class FrameworkPermissionController {

     /**
      * @author：Roberto
      * @create:2020年2月25日 上午10:10:28
      * 描述:<p>定义403请求处理</p>
      */
     @GetMapping("/403")
     public Result<?> noauth(HttpServletResponse response)  {
         response.setStatus(HttpStatus.FORBIDDEN.value());
         Result<String> result = new Result<>();
         result.setCode(HttpStatus.FORBIDDEN.value());
         result.setSuccess(false);
         result.setMessage(HttpStatus.FORBIDDEN.getReasonPhrase());
         return result;
     }
}
