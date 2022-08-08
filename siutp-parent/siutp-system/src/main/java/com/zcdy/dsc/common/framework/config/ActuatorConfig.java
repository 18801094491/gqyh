package com.zcdy.dsc.common.framework.config;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Roberto
 * 创建时间:2020年4月24日 上午10:36:52
 * 描述: <p>配置httptrace端点信息</p>
 */
@Configuration
public class ActuatorConfig {

	@ConditionalOnMissingBean
	@Bean
	public HttpTraceRepository httpTraceRepository() {
	  return new InMemoryHttpTraceRepository();
	}
}
