package com.zcdy.dsc.common.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;

/**
 * 分页插件
 * 
 * @author Roberto
 * @date 2020/05/27
 */
@Configuration
@MapperScan(value = {"com.zcdy.dsc.modules.**.mapper*"})
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        // 设置sql的limit为无限制，默认是500
        return new PaginationInterceptor().setLimit(-1);
    }

    /**
     * mybatis-plus SQL执行效率插件 开发环境使用，方便查询慢sql
     */
    @Profile(value = {"dev"})
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
}
