package com.zcdy.dsc.collector.util;

import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.zcdy.dsc.collector.task.IotVariableValueBean;
import sun.rmi.runtime.Log;

/**
 * 2 * @Author: 王海东
 * 3 * @Date: 2020/3/5 17:52
 * 4
 */
@Slf4j
@Component
public class InitProject  implements ApplicationRunner {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IotVariableValueBean iotVariableValueBean;

    @Override
    public void run(ApplicationArguments args){
        try {
            //根据hash值从数据库获取所有需要采集的数据
            log.info("启动成功，根据hash值从redis获取所有需要采集的数据");
            Map<Object, Object> map= stringRedisTemplate.opsForHash().entries("com:zcdy:dsc:iotVariable");
            iotVariableValueBean.timer(map);
        } catch (Exception e) {
            log.info("启动失败，出现异常："+e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
