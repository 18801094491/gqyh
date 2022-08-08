package com.zcdy.dsc.collector.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.collector.task.IotVariableValueBean;
import com.zcdy.dsc.collector.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 2 * @Author: 王海东
 * 3 * @Date: 2020/3/4 9:05
 */
@RestController
@Api(tags="采集策略控制")
@RequestMapping("/iot/task")
public class CollectionConfigController {
    @Resource
	private StringRedisTemplate stringRedisTemplate;
    
    @Resource
    private IotVariableValueBean iotVariableValueBean;

    @ApiOperation(value="根据设备采集策略生成采集数据任务", notes="根据设备采集策略生成采集数据任务")
    @PostMapping (value = "/getValuesByCycle")
    public Result<String> updateCycleByCate(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	 //从system应用中的http请求中获取参数的map集合
        //由于参数只能由Map<String, String[]> 接收，传过来的采集周期存在map的key中，采集的变量存在map的value的数组的第一个数组中
        Map<String, String[]> maps =request.getParameterMap();
        Map<String, String> hash = new HashMap<String, String>();
        if(maps!=null&&maps.size()>0){
            //遍历获取的map集合
            Iterator it = maps.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String iotCycle = entry.getKey().toString();//获取分组周期
                //先获取map集合的value即一个数组
            String variableNames[]= (String[]) entry.getValue();
                //，由于变量是存在数组的第一个位置，直接获取变量
            String variableName=variableNames[0];
                hash.put(iotCycle,variableName);
            }
            //先清除缓存中旧的采集策略
            this.stringRedisTemplate.delete("com:zcdy:dsc:iotVariable");
            //像缓存中写入新的缓存策略
            this.stringRedisTemplate.opsForHash().putAll("com:zcdy:dsc:iotVariable", hash);
        }

        //根据hash值从缓存获取所有需要采集的数据
        Map<Object, Object> map= stringRedisTemplate.opsForHash().entries("com:zcdy:dsc:iotVariable");
        //生成采集任务
        iotVariableValueBean.timer(map);
        return Result.success("operation success", "操作成功");
    }
}
