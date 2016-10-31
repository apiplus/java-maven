package com.wanpucs.openapi.httpclient;

import java.util.HashMap;
import java.util.Map;

public class GlobelViewMap {
	public static Map<String, Map<String, HttpStoreDataInfo>> viewMap = new HashMap<String, Map<String, HttpStoreDataInfo>>();

	public static Map<String, Map<String, HttpStoreDataInfo>> getNewViewMap() {
		viewMap = new HashMap<String, Map<String, HttpStoreDataInfo>>();
		return viewMap;
	}

}
