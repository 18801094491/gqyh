package com.zcdy.dsc.common.framework.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.zcdy.dsc.modules.collection.gis.WebsocketObserver;

/**
 * Websocket配置
 * 
 * @author Roberto
 * @date 2020/05/27
 */
@Configuration
public class WebSocketConfig {
    
    /**
     * 注入ServerEndpointExporter， 这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     */
    @Profile({"dev", "dat", "sit", "uat", "pro"})
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * @author：Roberto
     * @create:2020年3月5日 上午11:29:29 描述:
     *                   <p>
     *                   初始化Websocket旁观者
     *                   </p>
     */
    @Bean("observers")
    public List<Observer> observers() {
        List<Observer> observers = new ArrayList<Observer>();
        WebsocketObserver bean = new WebsocketObserver();
        observers.add(bean);
        return observers;
    }
}
