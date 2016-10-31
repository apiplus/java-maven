package com.wanpucs.openapi.cms;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.wanpucs.openapi.exception.OpenApiException;
import com.wanpucs.openapi.sdk.OpenApi;
import com.wanpucs.openapi.utils.StringUtils;

public class OpenApiUtils {
	public static Object api(OpenApi sdk, String method,
			Map<String, Object> paramMap, Class clazz) throws OpenApiException {
		Object[] args = new Object[] {};
		if (paramMap != null) {
			args = new Object[] { paramMap };
		}

		String result = sdk.commonApi(method, args, null);

		if (StringUtils.isBlank(result)) {
			return null;
		}

		result = StringUtils.decodeUnicode(result);

		if (clazz.getName().equals(String.class.getName())) {
			return result;
		}

		result = result.replace("\"{", "{").replace("}\"", "}")
				.replace("\"[", "[").replace("]\"", "]");

		if (clazz == null) {
			clazz = Object.class;
		}

		return JSON.parseObject(StringUtils.jsonString(result), clazz);
	}
}
