package com.wanpucs.openapi.sdk;

import com.wanpucs.openapi.exception.OpenApiException;

/**
 * 
 * @ClassName: AccessTokenCheck
 * @Description:生成Token的函数
 * @author William.jiang
 * @date 2016年10月25日 下午2:16:35
 * 
 */
public class AccessTokenCheck {

	protected static class AccessTokenResult {
		private String token;
		private String refreshToken;
		private boolean isExpired;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getRefreshToken() {
			return refreshToken;
		}

		public void setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
		}

		public boolean isExpired() {
			return isExpired;
		}

		public void setExpired(boolean isExpired) {
			this.isExpired = isExpired;
		}

	}

	/**
	 * 
	 * @Title: encodeUrl
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param input
	 * @param @return
	 * @param @throws OpenApiException 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String makeAccessToken(String appKey, String secretKey)
			throws OpenApiException {
		return null;

	}

	public static String makeAccessToken(String refreshToken)
			throws OpenApiException {
		return null;
	}

}
