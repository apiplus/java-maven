package com.wanpucs.openapi.exception;

/**
 * 定义异常。
 * 
 * @since jdk1.7
 * @author open.qq.com
 * 
 */
public class OpenApiException extends Exception {
	// 序列化UID
	private static final long serialVersionUID = 3128610276011819281L;
	// 错误码
	private int code;

	/**
	 * 
	 * @return 异常状态码。
	 */
	public int getErrorCode() {
		return code;
	}

	/**
	 * 构造异常
	 * 
	 * @param code
	 *            异常状态码
	 * @param msg
	 *            异常讯息
	 */
	public OpenApiException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	/**
	 * 构造异常
	 * 
	 * @param code
	 *            异常状态码
	 * @param ex
	 *            异常来源
	 */
	public OpenApiException(int code, Exception ex) {
		super(ex);
		this.code = code;
	}
}
