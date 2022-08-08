package com.zcdy.dsc.collector.util;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Roberto
 * @CreateTime:2020年2月25日 上午9:33:17
 * @Description: <p>通用接口返回数据格式</p>
 */
@Data
@ApiModel(value = "接口返回对象", description = "接口返回对象")
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	@ApiModelProperty(value = "成功标志")
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	@ApiModelProperty(value = "返回处理消息")
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	@ApiModelProperty(value = "返回代码")
	private Integer code = 0;

	/**
	 * 返回数据对象 data
	 */
	@ApiModelProperty(value = "返回数据对象")
	private T result;

	/**
	 * 时间戳
	 */
	@ApiModelProperty(value = "时间戳")
	private long timestamp = System.currentTimeMillis();

	public Result() {
	}

	/**
	 * @author:Roberto
	 * @create:2020年3月7日 下午6:20:12
	 * @Description:<p>定义静态响应结果</p>
	 */
	public static final <T> Result<T> success(T bean, String message) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(ResultConstant.SC_OK_200);
		r.setMessage(message);
		r.setResult(bean);
		return r;
	}
	
	/**
	 * @author:Roberto
	 * @create:2020年4月7日 上午9:57:11
	 * @Description:<p>失败结果定义</p>
	 */
	public static final <T> Result<T> fail(String message) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(ResultConstant.SC_INTERNAL_SERVER_ERROR_500);
		r.setMessage(message);
		return r;
	}
}