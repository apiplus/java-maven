package com.wanpucs.openapi.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.apache.commons.httpclient.methods.multipart.FilePart;

import com.wanpucs.openapi.exception.OpenApiException;
import com.wanpucs.openapi.sdk.OpenApi;

/**
 * OpenAPI V3 SDK 示例代码
 * 
 * @version 3.0.0
 * @since jdk1.5
 * @author open.qq.com
 * @copyright © 2012, Tencent Corporation. All rights reserved.
 * @History: 3.0.0 | coolinchen | 2012-11-6 12:01:05 | initialization
 * 
 */

public class TestUploadFile {
	public static void main(String args[]) {
		// 应用基本信息
		String secretKey = "";
		String appkey = "";

		String serverName = "";

		String protocol = null;
		OpenApi sdk;
		try {
			sdk = new OpenApi(appkey, secretKey, serverName, protocol);
			sdk.setServerName(serverName);
		} catch (OpenApiException e) {
			// exception to be handled!
		}

		System.out.println("===========test AddArticle===========");
		// testAddPicWeibo(sdk, openid, openkey, pf);
	}

	/**
	 * 测试调用UserInfo接口
	 * 
	 */
	public static void testAddPicWeibo(OpenApi sdk, String openid,
			String openkey, String pf) {
		// 指定OpenApi Cgi名字
		String scriptName = "/v3/t/add_pic_t";

		// 指定HTTP请求协议类型
		String protocol = "http";

		// 填充URL请求参数
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		params.put("openkey", openkey);
		params.put("pf", pf);
		params.put("content", "图片描述。。。@xxx");
		params.put("format", "json");

		// 上传的图片文件,支持中文命名
		String filepath = "/data/home/coolinchen/photo/图片.jpg";
		File p = new File(filepath);
		FilePart pic;
		try {
			// 指定要上传的文件,文件命名（如"pic"）为add_pic_t的参数
			pic = new FilePart("pic", new File(filepath));
			String resp = sdk.apiUploadFile(scriptName, params, pic, protocol);
			System.out.println(resp);
		} catch (OpenApiException e) {
			System.out.printf("Request Failed. code:%d, msg:%s\n",
					e.getErrorCode(), e.getMessage());
			e.printStackTrace();
		}
		// 针对FilePart构造函数的异常
		catch (FileNotFoundException e) {
		}
	}
}
