# wanpucs-java-sdk

Basic useful class :

 * com.wanpucs.openapi.sdk.OpenApi
 
 
  create a  instance of OpenApi which is the main SDK class of all.

 

And here's some code from  com.wanpucs.openapi.test.TestOpenApi ! :+1:

```

package com.wanpucs.openapi.test;

import java.util.HashMap;
import java.util.Map;

import com.wanpucs.openapi.exception.OpenApiException;
import com.wanpucs.openapi.sdk.OpenApi;

/**
 * OpenAPI V1 SDK demo code
 * 
 * @version 3.0.0
 * @since jdk1.7
 * @author www.wanpucs.com
 * @copyright © 2016,www.wanpucs.com. All rights reserved.
 * @History:
 * 
 */

public class TestOpenApi {
	public static void main(String args[]) {
		// basic Key
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
			testGetMainNavigationTreeApi(sdk, methodName, paramValues);
		} catch (OpenApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * test getting navigationTree api
	 * 
	 */
	public static void testGetMainNavigationTreeApi(OpenApi sdk,
			String methodName, Object[] args) {

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





```

And also we make  some  encapsulation base on business and OpenApi.class, may be it  will save your  time to  maintenance, testing, and reuse,u can use it or drop it to encapsulate  your own util class 

 
### Encapsulation util class in package com.wanpucs.openapi.cms :

 ContentUtil.java 
 
 GlobalBaseUtil.java
 
 MallPluginUtils.java
 
 ...........


