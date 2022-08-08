package com.zcdy.dsc.common.api.vo;

import com.zcdy.dsc.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author： 在信汇通
 * 创建时间：2020年12月11日 下午1:33:17
 * 描述: <p>通用接口返回数据格式</p>
 */
@Data
@ApiModel(value="接口返回对象", description="接口返回对象")
public class ResultData<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 返回处理消息
	 */
	@ApiModelProperty(value = "返回处理消息")
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	@ApiModelProperty(value = "返回代码")
	private Integer code = CommonConstant.SC_OK_200;

	/**
	 * 返回数据对象 data
	 */
	@ApiModelProperty(value = "返回数据对象")
	private T data;

	/**
	 * 返回数据对象列表 list
	 */
	@ApiModelProperty(value = "返回数据对象列表")
	private List<T> list;


	public ResultData() {
		
	}
	
	/**
	 * @author：Roberto
	 * @create:2020年3月7日 下午6:20:12
	 * 描述:<p>定义静态响应结果</p>
	 */
	public static final <T> ResultData<T> success(T bean, String message) {
		ResultData<T> r = new ResultData<T>();
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage(message);
		r.setData(bean);
		return r;
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月7日 下午6:20:12
	 * 描述:<p>定义静态响应结果</p>
	 */
	public static final <T> ResultData<T> success(List<T> list, String message) {
		ResultData<T> r = new ResultData<T>();
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage(message);
		r.setList(list);
		return r;
	}

	public static final <T> ResultData<T> success(List<T> list) {
		ResultData<T> r = new ResultData<T>();
		r.setCode(CommonConstant.SC_OK_200);
		r.setList(list);
		return r;
	}
	
	/**
	 * @author：Roberto
	 * @create:2020年3月7日 下午6:20:12
	 * 描述:<p>定义静态响应结果</p>
	 */
	public static final <T> ResultData<T> fail(String message) {
		ResultData<T> r = new ResultData<T>();
		r.setCode(CommonConstant.SC_INTERNAL_SERVER_ERROR_500);
		r.setMessage(message);
		return r;
	}

	public ResultData<T> success(String message) {
		this.message = message;
		this.code = CommonConstant.SC_OK_200;
		return this;
	}

	public ResultData<T> success() {
        this.code = CommonConstant.SC_OK_200;
        return this;
    }
	
	
	public static ResultData<Object> ok() {
		ResultData<Object> r = new ResultData<Object>();
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage("成功");
		return r;
	}
	
	public static ResultData<Object> ok(String msg) {
		ResultData<Object> r = new ResultData<Object>();
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage(msg);
		return r;
	}
	
	public static <T> ResultData<T> ok(T data) {
		ResultData<T> r = new ResultData<>();
		r.setCode(CommonConstant.SC_OK_200);
		r.setData(data);
		return r;
	}
	
	public static ResultData<Object> error(String msg) {
		return error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, msg);
	}
	
	public static ResultData<Object> error(int code, String msg) {
		ResultData<Object> r = new ResultData<Object>();
		r.setCode(code);
		r.setMessage(msg);
		return r;
	}

	public ResultData<T> error500(String message) {
		this.message = message;
		this.code = CommonConstant.SC_INTERNAL_SERVER_ERROR_500;
		return this;
	}
	/**
	 * 无权限访问返回结果
	 */
	public static ResultData<Object> noauth(String msg) {
		return error(CommonConstant.SC_SYSTEM_NO_AUTHZ, msg);
	}
}