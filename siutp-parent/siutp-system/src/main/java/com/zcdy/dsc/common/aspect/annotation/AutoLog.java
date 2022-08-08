package com.zcdy.dsc.common.aspect.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.zcdy.dsc.common.aspect.log.LogEnum;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.constant.ModuleConstant;

/**
 * @author： Roberto
 * 创建时间：2020年2月25日 上午9:34:34
 * 描述: <p>系统日志处理注解</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {

	/**
	 * 日志内容
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 日志类型,不推荐使用，建议使用type()
	 * 
	 * @return 0:操作日志;1:登录日志;2:定时任务;
	 */
	int logType() default CommonConstant.LOG_TYPE_2;
	
	/**
	 * 日志类型
	 * @return 日志类型
	 * @see com.zcdy.dsc.common.aspect.log.LogEnum.Type
	 */
	LogEnum.Type type() default LogEnum.Type.UNDEFINED;
	
	/**
	 * 操作类型
	 * @return 操作类型
	 * @see com.zcdy.dsc.common.aspect.log.LogEnum.Operation
	 */
	LogEnum.Operation operation() default LogEnum.Operation.UNDEFINED;
	
	/**
	 * 操作日志类型，不推荐使用，建议使用operation()
	 * @return （1查询，2添加，3修改，4删除）
	 */
	int operateType() default 0;
	
	/**
	 * 操作分组
	 * @return 分组名称
	 * @see com.zcdy.dsc.constant.ModuleConstant
	 */
	ModuleConstant group() default ModuleConstant.UNDEFINED;
}
