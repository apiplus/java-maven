package com.wanpucs.openapi.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class SignUtil {

	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	public static String createNonce() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	public static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	/*
	 * 制作签名
	 */
	public static String makeSignature(String secretKey, String timestamp,
			String nonce) {
		List<String> params = new ArrayList<String>();
		params.add(secretKey);
		params.add(timestamp);
		params.add(nonce);
		// 1. 将secretKey、timestamp、nonce三个参数进行字典序排序
		Collections.sort(params, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		String signature = SHA1.encode(params.get(0) + params.get(1)
				+ params.get(2));

		return signature;

	}
}
