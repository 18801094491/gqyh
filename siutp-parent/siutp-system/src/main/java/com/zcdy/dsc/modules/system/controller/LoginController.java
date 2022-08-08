package com.zcdy.dsc.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.framework.redis.RedisService;
import com.zcdy.dsc.common.framework.shiro.vo.DefContants;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.framework.websocket.WebSocket;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.common.util.MD5Util;
import com.zcdy.dsc.common.util.PasswordUtil;
import com.zcdy.dsc.common.util.RegexUtils;
import com.zcdy.dsc.common.util.encryption.EncryptedString;
import com.zcdy.dsc.common.util.sms.DySmsEnum;
import com.zcdy.dsc.common.util.sms.DySmsHelper;
import com.zcdy.dsc.modules.collection.gis.websocket.GISUserWebsocketManager;
import com.zcdy.dsc.modules.system.constant.LoginTypeConstant;
import com.zcdy.dsc.modules.system.entity.SysDepart;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.system.model.SysLoginModel;
import com.zcdy.dsc.modules.system.service.ISysDepartService;
import com.zcdy.dsc.modules.system.service.ISysLogService;
import com.zcdy.dsc.modules.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * 用户登录控制器
 *
 * @author
 * @date 2020/05/19 11:42:17
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "用户登录")
@Validated
@Slf4j
public class LoginController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysBaseApi sysBaseApi;
    @Autowired
    private ISysLogService logService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private WebSocket webSocket;

    private static final String BASE_CHECK_CODES = "wertyupkjhgfdaxcvbnmQW9ERT8YUPKJH6GFDSAZXCV5BNM56789";

    private static final String BASE_CHECK_NUMBER_CODE = "80126956789";

    /**
     * 找回密码Redis前缀
     */
    private static final String FIND_PASSWORD = "find_password_";

    /**
     * 描述:  登录接口
     *
     * @param sysLoginModel
     * @author： songguang.jiao
     * 创建时间： 2020年3月12日 下午2:34:21
     * 版本号: V1.0
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel, HttpServletRequest re) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
        //前端密码加密，后端进行密码解密
        //password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

        //update-begin-author:taoyan date:20190828 for:校验验证码
        Object checkCode = redisService.get(sysLoginModel.getCheckKey());
        if (checkCode == null) {
            result.error500("验证码失效");
            return result;
        }
        if (!checkCode.toString().equalsIgnoreCase(sysLoginModel.getCaptcha())) {
            result.error500("验证码错误");
            return result;
        }

        //1. 校验用户是否有效
        SysUser sysUser = sysUserService.getUserByNameOrPhone(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(sysUser.getUsername(), password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.setCode(CommonConstant.SC_PASSWORD_ERROR_530);
            result.setMessage("用户名或密码错误");
            return result;
        }
        //用户登录信息
        userInfo(sysUser, result, LoginTypeConstant.PC);
        sysBaseApi.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null);
        redisService.del(sysLoginModel.getCheckKey());
        return result;
    }

    /**
     * 移动端登录接口
     *
     * @param sysLoginModel
     * @param request
     * @return
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(hidden = false, value = "移动端登录接口")
    @PostMapping(value = "/app/login", headers = "X-request-platform")
    public Result<JSONObject> appLogin(@RequestBody SysLoginModel sysLoginModel, HttpServletRequest request) {
        Result<JSONObject> result = new Result<JSONObject>();
        //移动端登录的头信息
        final String phoneHeader = "X-request-platform";
        String header = request.getHeader(phoneHeader);
        if (StringUtils.isEmpty(header) && LoginTypeConstant.APP.equals(header)) {
            //不允许的登录方
            result.error500("Unsupoorted");
            return result;
        }
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        //1. 校验用户是否有效
        SysUser sysUser = sysUserService.getUserByNameOrPhone(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(sysUser.getUsername(), password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.setCode(CommonConstant.SC_PASSWORD_ERROR_530);
            result.setMessage("用户名或密码错误");
            return result;
        }

        //用户登录信息
        userInfo(sysUser, result, LoginTypeConstant.APP);
        sysBaseApi.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null);
        return result;
    }


    /**
     * app刷新token
     *
     * @param sysLoginModel
     * @param request
     * @return
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(hidden = false, value = "app刷新token")
    @PostMapping(value = "/app/refresh")
    public Result<JSONObject> appRefresh(@RequestBody SysLoginModel sysLoginModel, HttpServletRequest request) {
        Result<JSONObject> result = new Result<JSONObject>();
        //移动端的头信息
        final String phoneHeader = "X-request-refresh";
        String header = request.getHeader(phoneHeader);
        if (StringUtils.isEmpty(header) && LoginTypeConstant.APP.equals(header)) {
            //不允许的登录方
            result.error500("Unsupoorted");
            return result;
        }
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        //1. 校验用户是否有效
        SysUser sysUser = sysUserService.getUserByNameOrPhone(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(sysUser.getUsername(), password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.setCode(CommonConstant.SC_PASSWORD_ERROR_530);
            result.setMessage("用户名或密码错误");
            return result;
        }

        //用户登录信息
        userInfo(sysUser, result, LoginTypeConstant.APP);
        sysBaseApi.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null);
        return result;
    }


    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout")
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        //用户退出逻辑
        String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
        if (ConvertUtils.isEmpty(token)) {
            return Result.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        LoginUser sysUser = sysBaseApi.getUserByName(username);
        if (sysUser != null) {
            sysBaseApi.addLog("用户名: " + sysUser.getRealname() + ",退出成功！", CommonConstant.LOG_TYPE_1, null);
            log.info(" 用户名:  " + sysUser.getRealname() + ",退出成功！ ");
            //清空用户登录Token缓存
            redisService.del(CommonConstant.PREFIX_USER_TOKEN + username + ":" + token);
            //清空用户登录Shiro权限缓存
            redisService.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
            return Result.ok("退出登录成功！");
        } else {
            return Result.error("Token无效!");
        }
    }

    /**
     * 获取登录信息
     *
     * @return
     */
    @GetMapping("loginfo")
    public Result<JSONObject> getLoginfo() {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        //update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
        // 获取一天的开始和结束时间
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
        // 获取系统访问记录
        Long totalVisitCount = logService.findTotalVisitCount();
        obj.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = logService.findTodayVisitCount(dayStart, dayEnd);
        obj.put("todayVisitCount", todayVisitCount);
        Long todayIp = logService.findTodayIp(dayStart, dayEnd);
        obj.put("todayIp", todayIp);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 登录成功选择用户当前部门
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
    public Result<JSONObject> selectDepart(@RequestBody SysUser user) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = user.getUsername();
        if (ConvertUtils.isEmpty(username)) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            username = sysUser.getUsername();
        }
        String orgCode = user.getOrgCode();
        this.sysUserService.updateUserDepart(username, orgCode);
        SysUser sysUser = sysUserService.getUserByName(username);
        JSONObject obj = new JSONObject();
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        return result;
    }


    /**
     * 获取校验码
     */
    @ApiOperation(value = "获取校验码")
    @GetMapping(value = "/getCheckCode")
    public Result<Map<String, String>> getCheckCode() {
        Result<Map<String, String>> result = new Result<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>(2);
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String key = MD5Util.md5Encode(code + System.currentTimeMillis(), "utf-8");
            redisService.setEx(key, code, 60);
            map.put("key", key);
            map.put("code", code);
            result.setResult(map);
            result.setCode(CommonConstant.SC_OK_200);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 短信登录接口
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(hidden = true, value = "通过短信登录")
    @PostMapping(value = "/sms/login")
    public Result<String> sms(@RequestBody JSONObject jsonObject) {
        Result<String> result = new Result<String>();
        String mobile = jsonObject.get("mobile").toString();
        String smsmode = jsonObject.get("smsmode").toString();
        log.info(mobile);
        Object object = redisService.get(mobile);
        if (object != null) {
            result.setMessage("验证码10分钟内，仍然有效！");
            result.setSuccess(false);
            return result;
        }

        //随机数
        String captcha = RandomUtil.randomNumbers(6);
        JSONObject obj = new JSONObject();
        obj.put("code", captcha);
        try {
            boolean b = false;
            //注册模板
            if (CommonConstant.SMS_TPL_TYPE_1.equals(smsmode)) {
                SysUser sysUser = sysUserService.getUserByPhone(mobile);
                if (sysUser != null) {
                    result.error500(" 手机号已经注册，请直接登录！");
                    sysBaseApi.addLog("手机号已经注册，请直接登录！", CommonConstant.LOG_TYPE_1, null);
                    return result;
                }
                b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.REGISTER_TEMPLATE_CODE);
            } else {
                //登录模式，校验用户有效性
                SysUser sysUser = sysUserService.getUserByPhone(mobile);
                result = sysUserService.checkUserIsEffective(sysUser);
                if (!result.isSuccess()) {
                    return result;
                }

                /**
                 * smsmode 短信模板方式  0 .登录模板、1.注册模板、2.忘记密码模板
                 */
                if (CommonConstant.SMS_TPL_TYPE_0.equals(smsmode)) {
                    //登录模板
                    b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.LOGIN_TEMPLATE_CODE);
                } else if (CommonConstant.SMS_TPL_TYPE_2.equals(smsmode)) {
                    //忘记密码模板
                    b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
                }
            }

            if (b == false) {
                result.setMessage("短信验证码发送失败,请稍后重试");
                result.setSuccess(false);
                return result;
            }
            //验证码10分钟内有效
            redisService.setEx(mobile, captcha, 600);
            result.setSuccess(true);

        } catch (ClientException e) {
            e.printStackTrace();
            result.error500(" 短信接口未配置，请联系管理员！");
            return result;
        }
        return result;
    }

    /**
     * 获取加密字符串
     *
     * @return
     */
    @GetMapping(value = "/getEncryptedString")
    public Result<Map<String, String>> getEncryptedString() {
        Result<Map<String, String>> result = new Result<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>(2);
        map.put("key", EncryptedString.KEY);
        map.put("iv", EncryptedString.IV);
        result.setResult(map);
        return result;
    }

    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @param type    登录区分pc跟app端,存不同token
     * @return
     * @author songguang.jiao
     * @date 2020/05/19 11:45:11
     */
    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result, String type) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        //清除已登录的该用户的token信息
        String redisKey = CommonConstant.PREFIX_USER_TOKEN + username;
        if (LoginTypeConstant.APP.equals(type)) {
            redisKey = CommonConstant.PREFIX_USER_TOKEN + username + "_" + LoginTypeConstant.APP;
        }
        Set<String> keys = stringRedisTemplate.keys(redisKey + ":*");
        if (keys.size() > 0) {
            stringRedisTemplate.delete(keys);
            Map<String, Object> message = new HashMap<>(2);
            message.put("cmd", "sys");
            message.put("msgTxt", "当前用户已在其它机器登录");
            webSocket.sendOneMessage(sysUser.getId(), JSON.toJSONString(message));
            GISUserWebsocketManager.remove(sysUser.getId());
        }
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间

        redisService.set(redisKey + ":" + token, token);
        redisService.expire(redisKey + ":" + token, JwtUtil.EXPIRE_TIME * 2 / 1000);


        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
        obj.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            obj.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            obj.put("multi_depart", 1);
        } else {
            obj.put("multi_depart", 2);
        }
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 找回密码
     *
     * @param mobile
     * @return
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @AutoLog(value = "找回密码")
    @ApiOperation(value = "找回密码")
    @GetMapping("/app/findPassword")
    public Result<Object> findPassword(@RequestParam(value = "mobile",required = false) @NotBlank(message = "手机号不能为空")String mobile) {
        Result<Object> result = new Result<>();
        //校验格式
        if (!RegexUtils.checkMobile(mobile)) {
            return result.error500("手机号不合法");
        }
        SysUser sysUser = sysUserService.getUserByPhone(mobile);
        if (sysUser == null) {
            return result.error500("手机号不匹配");
        }
        //发送消息
        String key = FIND_PASSWORD + mobile;
        if (redisService.hasKey(key)) {
            return result.error500("请不要频繁发送短信，请稍后再发");
        }
        String code = RandomUtil.randomString(BASE_CHECK_NUMBER_CODE, 4);
        redisService.setEx(key, code, 60);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        try {
            DySmsHelper.sendSms(mobile, jsonObject, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            return result.error500("系统错误,请稍后再发送短信");
        }
        return result.success("发送成功");
    }

    /**
     * 校验验证码
     *
     * @param code
     * @return
     */
    @AutoLog("校验验证码")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(value = "校验验证码")
    @GetMapping("/app/checkCode")
    public Result<Object> checkAppCode(@RequestParam(value = "code",required = false) @NotBlank(message = "code不能为空") String code,
                                       @RequestParam(value = "mobile",required = false) @NotBlank(message = "手机号不能为空")String mobile) {
        String key = FIND_PASSWORD + mobile;
        if (code.equals(redisService.get(key))) {
            redisService.del(key);
            return Result.ok("校验成功");
        }
        return Result.error("验证码错误");
    }

    /**
     * 忘记密码后，重置密码
     *
     * @param password
     * @param confirmPassword
     * @param mobile
     * @return
     */
    @AutoLog("重置密码")
    @ApiOperation(value = "重置密码")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @GetMapping("/app/resetPassword")
    public Result<Object> resetPassword(@RequestParam(value = "password",required = false) @NotBlank(message = "密码不能为空")String password,
                                        @RequestParam(value = "confirmPassword",required = false) @NotBlank(message = "确认密码不能为空")String confirmPassword,
                                        @RequestParam(value = "mobile",required = false) @NotBlank(message = "手机号不能为空") String mobile) {
        return sysUserService.forgetResetPassword(password, confirmPassword, mobile);
    }

}