package com.wanpucs.openapi.exception;

/**
 * 定义错误码。
 * 
 * @since jdk1.7
 * @author www.wanpucs.com
 * 
 * 
 */

public class ErrorCode {

	private static final long serialVersionUID = -1679458253208555786L;

	/**
	 * 必填参数为空。
	 */
	public final static int PARAMETER_EMPTY = 1801;

	/**
	 * 必填参数无效。
	 */
	public final static int PARAMETER_INVALID = 1802;

	/**
	 * 服务器响应数据无效。
	 */
	public final static int RESPONSE_DATA_INVALID = 1803;

	/**
	 * 生成accessToken失败
	 */
	public final static int MAKE_ACCESS_TOKEN_FAIL = 1805;

	/**
	 * ACCESSTOKEN过期
	 */
	public final static int ACCESS_TOKEN_EXPIRED = 1806;

	/**
	 * refreshAccessToken过期
	 */
	public final static int REFRESH_ACCESS_TOKEN_EXPIRED = 1807;

	/**
	 * 网络错误。
	 */
	public final static int NETWORK_ERROR = 1900;
}
