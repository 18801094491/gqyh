package com.zcdy.dsc.modules.system.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.framework.redis.RedisService;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.base.controller.AbstractPrincipalController;
import com.zcdy.dsc.common.util.KaptchaUtils;
import com.zcdy.dsc.common.util.MD5Util;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码控制器
 * @author Roberto
 * @date 2020/06/30
 */
@Api(value = "验证码接口集")
@RestController
@RequestMapping("/anon/vcode")
public class VerifyCodeController implements AbstractPrincipalController{

    private final int WIDTH=200;
    
    private final int HEIGHT=80;
    
    private final int CHAR_LENGTH=4;
    
    private final long CODE_EXPIRE = 60L;
    
    @Resource
    private RedisService redisService;
    
    /**
     * @return
     * @throws IOException
     */
    @ApiVersion(group = VersionConstant.VERSION_200)
    @GetMapping("/base64")
    public Result<Map<String, String>> getBase64VerifyCode() throws IOException{
        String verifyCode = KaptchaUtils.generateVerifyCode(CHAR_LENGTH);
        String base64Str;
        try {
            base64Str = KaptchaUtils.outputVerifyBase64(WIDTH, HEIGHT, verifyCode);
        } catch (IOException e) {
            throw e;
        }
        Session session = SecurityUtils.getSubject().getSession();
        if(null==session) {
            return Result.fail("no session");
        }
        String key = session.getId().toString();
        key = MD5Util.md5Encode(key, StandardCharsets.UTF_8.name());
        Map<String, String> map = new HashMap<String, String>(2);
        map.put("key", key);
        map.put("url", base64Str);
        redisService.setEx(key, verifyCode, CODE_EXPIRE);
        return Result.success(map, "success");
    }
}
