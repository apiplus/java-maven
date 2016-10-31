package com.wanpucs.openapi.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.wanpucs.openapi.exception.OpenApiException;
import com.wanpucs.openapi.sdk.OpenApi;

/*
 * 封装全局SDK的工具类
 */
public class GlobalBaseUtil {

	/*
	 * 查询导航
	 */
	public static List<Map<String, Object>> getNavigatorTree(OpenApi sdk,
			Object[] args) throws OpenApiException {
		String result = sdk.commonApi("wanpu.navigation_getTree", args, null);
		List<Map<String, Object>> navList = JSON
				.parseObject(result, List.class);
		return navList;
	}

	/*
	 * 查询全局配置信息
	 */
	public static String getConfig(OpenApi sdk, String name)
			throws OpenApiException {
		Map<String, Object> logoParamMap = new HashMap<String, Object>();
		logoParamMap.put("name", name);
		Object[] logoParam = new Object[] { logoParamMap };
		String result = sdk.commonApi("configure.get", logoParam, null);
		return result;
	}

	public static void main(String[] args) {
		// 应用基本信息
		String appkey = "aE7U76KxBe604C9o";

		String secretKey = "uyhXT7CKZ0Q5ZVcJ1V10BFlvMuVa63sG";

		String serverName = "http://118.178.167.238";

		OpenApi sdk;
		try {
			sdk = new OpenApi(appkey, secretKey, serverName, "http");
			sdk.setServerName(serverName);

			String result = getConfig(sdk, "infoSlogan");
			System.out.println(result);

		} catch (OpenApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
