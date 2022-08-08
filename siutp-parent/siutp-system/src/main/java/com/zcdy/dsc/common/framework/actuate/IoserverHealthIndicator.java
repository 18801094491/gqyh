package com.zcdy.dsc.common.framework.actuate;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;

/**
 * IOServer健康状态检测
 * 
 * @author Roberto
 * @date 2020/06/01
 */
@ConditionalOnProperty(prefix = "com.zcdy.dsc.ioserver.health", name = "enabled", havingValue = "true")
@Component("ioserverHealthIndicator")
public class IoserverHealthIndicator implements HealthIndicator {

    @Value("${com.zcdy.dsc.Ioserver.variable.url}")
    private String ioserverProxyHost;

    @Value("${com.zcdy.dsc.ioserver.health.path}")
    private String ioserverHealthUrl;

    /* (non-Javadoc)
     * @see org.springframework.boot.actuate.health.HealthIndicator#health()
     */
    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 200) {
            return Health.down().withDetail("status", "offline").build();
        }
        return Health.up().withDetail("code", errorCode).withDetail("status", "online").build();
    }

    /**
     * 执行健康检测
     * 
     * @return the code of health check
     */
    private int check() {
        int down = 503, up = 200;
        String responseStr =
            HttpUtil.get(this.ioserverProxyHost.concat(this.ioserverHealthUrl), StandardCharsets.UTF_8);
        if (null == responseStr) {
            return down;
        }
        JSONObject jsonObject = JSON.parseObject(responseStr);
        if(jsonObject.getInteger("code").intValue() == 500){
            return down;
        }else{
            IoserverStatus ioserverStatus = jsonObject.getObject("result", IoserverStatus.class);
            if(ioserverStatus.getLinkStatus()==up && ioserverStatus.getWorkStatus() == up){
                return up;
            }
        }
        return down;
    }

}
