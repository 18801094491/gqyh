package com.zcdy.dsc.collector.task;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: 王海东
 * @Date: 2020/2/27 10:03
 */
@Component
@Slf4j
public class IotVariableValueBean {

	@Resource
	VariableCollectTask variableCollectTask;

    public static volatile boolean isRun = false;
    public static Timer timer = new Timer();
    
	public void timer(Map<Object, Object> map) {
        if(isRun){
            timer.cancel();
            timer=new Timer();
            isRun = false;
        }
        //遍历获取的map集合
        Iterator<?> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            int iotCycle = Integer.parseInt(entry.getKey().toString()) * 1000;//获取分组周期
            //先获取map集合的value即一个数组
//            String variableNames[]= (String[]) entry.getValue();
            //，由于变量是存在数组的第一个位置，直接获取变量
//            String variableName=variableNames[0];
            String variableName= entry.getValue().toString();
            //根据周期创建定时任务，每个采集周期定义一个定时任务
            timer.schedule(new TimerTask() {
                public void run() {
                    //从ioserver中采集数据
                    try {
                        variableCollectTask.fetchData(Arrays.asList(variableName.split(",")));
                    }catch (Exception e){
                        log.error(e.getMessage());
                        log.error("采集变量为："+variableName);
                    }

                }
            }, 5000 , iotCycle);
        }
        isRun=true;
    }
}
