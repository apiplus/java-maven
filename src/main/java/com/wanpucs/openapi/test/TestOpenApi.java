package com.wanpucs.openapi.test;

import java.util.HashMap;
import java.util.Map;

import com.wanpucs.openapi.exception.OpenApiException;
import com.wanpucs.openapi.sdk.OpenApi;

/**
 * OpenAPI V3 SDK 示例代码
 * 
 * @version 3.0.0
 * @since jdk1.5
 * @author open.qq.com
 * @copyright © 2012, Tencent Corporation. All rights reserved.
 * @History: 3.0.0 | nemozhang | 2012-03-21 12:01:05 | initialization
 * 
 */

public class TestOpenApi {
	public static void main(String args[]) {
		// 应用基本信息
		String appkey = "aE7U76KxBe604C9o";

		String secretKey = "uyhXT7CKZ0Q5ZVcJ1V10BFlvMuVa63sG";

		String serverName = "http://118.178.167.238";

		OpenApi sdk;
		try {
			sdk = new OpenApi(appkey, secretKey, serverName, "http");
			sdk.setServerName(serverName);

			System.out.println("===========test GetUserInfo===========");
			// 请求的目标方法名
			String methodName = "wanpu.navigation_getTree";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("type", "main");
			paramMap.put("status", 1);
			Object[] paramValues = new Object[] { paramMap };
			testEchoApi(sdk, methodName, paramValues);
		} catch (OpenApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 测试调用UserInfo接口
	 * 
	 */
	public static void testEchoApi(OpenApi sdk, String methodName, Object[] args) {

		// 请求方法参数，数组顺序为调用目标方法的参数顺序，类型保持一致，如本例目标方法参数只有一个，参数类型为三个integer类型的参数结构。

		try {
			String resp = sdk.commonApi(methodName, args, null);

			System.out.println(resp);
		} catch (OpenApiException e) {
			System.out.printf("Request Failed. code:%d, msg:%s\n",
					e.getErrorCode(), e.getMessage());
			e.printStackTrace();
		}
	}
}
