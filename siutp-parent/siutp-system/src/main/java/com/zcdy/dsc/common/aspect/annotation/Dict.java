package com.zcdy.dsc.common.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author： Roberto
 * 创建时间：2020年2月25日 上午9:35:00
 * 描述: <p>数据字典注解</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    /**
     * 方法描述:  数据code
     * 日    期： 2019年03月17日-下午9:37:16
     * @return 返回类型： String
     */
    String dicCode();

    /**
     * 方法描述:  数据Text
     * 日    期： 2019年03月17日-下午9:37:16
     * @return 返回类型： String
     */
    String dicText() default "";

    /**
     * 方法描述: 数据字典表
     * 日    期： 2019年03月17日-下午9:37:16
     * @return 返回类型： String
     */
    String dictTable() default "";
}
