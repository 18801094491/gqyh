package com.zcdy.dsc.eureka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 当Spring Security位于类路径上时，它将要求在每次向应用程序发送请求时都发送一个有效的CSRF令牌。
 * Eureka客户机通常不会拥有一个有效的跨站点请求伪造令牌(CSRF)，您需要禁用/ Eureka /**端点的这个请求
 * 
 * @author Roberto
 * @date 2020/06/12
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}
