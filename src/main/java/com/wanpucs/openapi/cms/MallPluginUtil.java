package com.wanpucs.openapi.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wanpucs.openapi.exception.OpenApiException;
import com.wanpucs.openapi.sdk.OpenApi;

public class MallPluginUtil extends OpenApiUtils {

	private static Object[] initParam(String name, Object value) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(name, value);
		Object[] objects = new Object[] { paramMap };
		return objects;
	}

	/*
	 * 获取商品分类
	 */
	public static List<Map<String, Object>> getCategoryList(OpenApi sdk,
			Long parentId) throws OpenApiException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("parent_id", parentId);

		List<Map<String, Object>> categoryList = (List<Map<String, Object>>) api(
				sdk, "wanpu.item_getCategoryTree", paramMap, List.class);
		return categoryList;

	}

	public static void main(String[] args) {
		String appkey = "CugxLRt8X9604C9q";

		String secretKey = "7LRveUPWhxTjFCWiztUPKptfHsM3wD9x";

		String serverName = "http://118.178.167.238";

		OpenApi sdk;
		try {
			sdk = new OpenApi(appkey, secretKey, serverName, "http");
			sdk.setServerName(serverName);

			getCategoryList(sdk, 0l);
			// PageList pageList = getPageListByCategoryId(sdk, 70, null, null);

		} catch (OpenApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
