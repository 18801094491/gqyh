package com.zcdy.dsc.common.util;

import org.apache.shiro.authc.AuthenticationException;

import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.framework.redis.RedisService;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.system.vo.LoginUser;

import javax.servlet.http.HttpServletRequest;

/**
 
 * @Date 2019/9/23 14:12
 * 描述: 编程校验token有效性
 * @author : songguang.jiao
 */
public class TokenUtils {

    /**
     * 验证Token
     */
    public static boolean verifyToken(HttpServletRequest request, ISysBaseApi sysBaseApi, RedisService redisService) {
        String token = request.getParameter("token");

        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token非法无效!");
        }

        // 查询用户信息
        LoginUser user = sysBaseApi.getUserByName(username);
        if (user == null) {
            throw new AuthenticationException("用户不存在!");
        }
        // 判断用户状态
        if (user.getStatus() != 1) {
            throw new AuthenticationException("账号已被锁定,请联系管理员!");
        }
        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, username, user.getPassword(), redisService)) {
            throw new AuthenticationException("Token失效，请重新登录!");
        }
        return true;
    }

    /**
     * 刷新token（保证用户在线操作不掉线）
     * @param token
     * @param userName
     * @param passWord
     * @param redisService
     * @return
     */
    private static boolean jwtTokenRefresh(String token, String userName, String passWord, RedisService redisService) {
        String cacheToken = String.valueOf(redisService.get(CommonConstant.PREFIX_USER_TOKEN +userName+":"+ token));
        if (ConvertUtils.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, passWord)) {
                String newAuthorization = JwtUtil.sign(userName, passWord);
                // 设置Toekn缓存有效时间
                redisService.set(CommonConstant.PREFIX_USER_TOKEN +userName+":"+ token, newAuthorization);
                redisService.expire(CommonConstant.PREFIX_USER_TOKEN +userName+":"+ token, JwtUtil.EXPIRE_TIME*2 / 1000);
            }
            return true;
        }
        return false;
    }

}
