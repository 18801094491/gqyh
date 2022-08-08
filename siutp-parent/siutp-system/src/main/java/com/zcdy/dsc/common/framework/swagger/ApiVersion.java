package com.zcdy.dsc.common.framework.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口版本管理注解
 * @author Roberto
 * @date 2020/05/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD,ElementType.TYPE})
public @interface ApiVersion {

    /**
     * 接口版本号(对应swagger中的group)
     * @return String[]
     */
    String[] group();

}
