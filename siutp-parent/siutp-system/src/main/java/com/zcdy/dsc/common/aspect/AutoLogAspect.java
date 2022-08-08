package com.zcdy.dsc.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.aspect.log.LogEnum.Operation;
import com.zcdy.dsc.common.aspect.log.LogEnum.Type;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.IPUtil;
import com.zcdy.dsc.common.util.SpringContextUtils;
import com.zcdy.dsc.constant.ModuleConstant;
import com.zcdy.dsc.modules.system.entity.SysLog;
import com.zcdy.dsc.modules.system.service.ISysLogService;

/**
 * 系统日志，切面处理类
 * 
 * @author Roberto
 * @date 2020/05/22
 */
@Aspect
@Component
public class AutoLogAspect {

    @Autowired
    private ISysLogService sysLogService;

    @Pointcut("@annotation(com.zcdy.dsc.common.aspect.annotation.AutoLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        // 保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        AutoLog autoLog = method.getAnnotation(AutoLog.class);
        if (autoLog != null) {
            // 注解上的描述,操作日志内容
            sysLog.setLogContent(autoLog.value());
            int logType = autoLog.logType();
            Type type = autoLog.type();
            if (Type.UNDEFINED != type) {
                logType = type.getValue();
            }
            sysLog.setLogType(logType);
            
            ModuleConstant group = autoLog.group();
            if(group!=ModuleConstant.UNDEFINED){
                sysLog.setGroupCode(group.getCode());
                sysLog.setGroupName(group.getName());
            }
        }

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        // 设置操作类型
        if (sysLog.getLogType() == CommonConstant.LOG_TYPE_2) {
            sysLog.setOperateType(getOperateType(methodName, autoLog.operateType(), autoLog.operation()));
        }

        // 请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSONObject.toJSONString(args);
            sysLog.setRequestParam(params);
        } catch (Exception e) {
        }

        // 获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        // 设置IP地址
        sysLog.setIp(IPUtil.getIpAddr(request));

        // 获取登录用户信息
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        if (sysUser != null) {
            sysLog.setUserid(sysUser.getUsername());
            sysLog.setUsername(sysUser.getRealname());
        }
        // 耗时
        sysLog.setCostTime(time);
        sysLog.setCreateTime(new Date());
        // 保存系统日志
        sysLogService.save(sysLog);
    }

    /**
     * 获取操作类型
     * @param operation 
     */
    private int getOperateType(String methodName, int operateType, Operation operation) {
        if (operateType > 0) {
            return operateType;
        }
        
        if(Operation.UNDEFINED!=operation){
            return operation.getValue();
        }
        
        //一下保留兼容历史代码
        if (methodName.startsWith("list")) {
            return CommonConstant.OPERATE_TYPE_1;
        }
        if (methodName.startsWith("add")) {
            return CommonConstant.OPERATE_TYPE_2;
        }
        if (methodName.startsWith("edit")) {
            return CommonConstant.OPERATE_TYPE_3;
        }
        if (methodName.startsWith("delete")) {
            return CommonConstant.OPERATE_TYPE_4;
        }
        if (methodName.startsWith("import")) {
            return CommonConstant.OPERATE_TYPE_5;
        }
        if (methodName.startsWith("export")) {
            return CommonConstant.OPERATE_TYPE_6;
        }
        return CommonConstant.OPERATE_TYPE_1;
    }
}
