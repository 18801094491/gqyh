package com.zcdy.dsc.common.exception;

/**
 * @author： Roberto
 * 创建时间：2020年2月25日 上午9:40:47
 * 描述: <p>自定义系统异常</p>
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BusinessException(String message){
		super(message);
	}
	
	public BusinessException(Throwable cause)
	{
		super(cause);
	}
	
	public BusinessException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
