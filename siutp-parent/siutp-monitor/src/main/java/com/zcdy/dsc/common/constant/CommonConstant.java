package com.zcdy.dsc.common.constant;

/**
 * @author： Roberto
 * 创建时间：2020年2月25日 上午9:37:55
 * 描述: <p>系统功能状态定义</p>
 */
public interface CommonConstant {

	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_OK_200 = 200;
    
    /**访问权限认证未通过 510*/
    public static final Integer SC_SYSTEM_NO_AUTHZ=510;
}
