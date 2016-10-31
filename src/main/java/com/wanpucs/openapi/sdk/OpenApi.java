package com.wanpucs.openapi.sdk;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.methods.multipart.FilePart;

import com.alibaba.fastjson.JSON;
import com.wanpucs.openapi.exception.ErrorCode;
import com.wanpucs.openapi.exception.OpenApiException;
import com.wanpucs.openapi.network.NetWork;
import com.wanpucs.openapi.utils.SignUtil;
import com.wanpucs.openapi.utils.StringUtils;

/**
 * 提供访问WANPU开放平台 OpenApi 的接口
 * 
 * @version 3.0.2
 * @since jdk1.7
 * @author www.wanpucs.com
 */

public class OpenApi {

	/**
	 * 构造函数
	 * 
	 * @param appKey
	 *            应用的appKey
	 * @param secretKey
	 *            应用的随机密钥
	 */

	private String secretKey;;
	private String appkey;
	private String serverName;
	private String accessToken;
	// chunk.json,text.json,http,https支持四种协议
	private String protocol;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public OpenApi(String appkey, String secretKey, String serverName,
			String protocol) throws OpenApiException {
		this.protocol = protocol;
		this.secretKey = secretKey;
		this.appkey = appkey;
		this.serverName = serverName;
		this.accessToken = AccessTokenCheck.makeAccessToken(appkey, secretKey);
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * 设置OpenApi服务器的地址
	 * 
	 * @param serverName
	 *            OpenApi服务器的地址
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * 执行API调用
	 * 
	 * @param methodName
	 *            调用方法名 如：account_get_by_username
	 * 
	 * @param params
	 *            args的参数列表:如[{"username":"admin"},"abc"],第一项是map，第二项是Stirng
	 * @param protocol
	 *            HTTP请求协议 "http" / "https"
	 * @return 返回服务器响应内容
	 */
	public String commonApi(String methodName, Object[] args, String protocol)
			throws OpenApiException {
		// 检查openid openkey等参数
		if (StringUtils.isBlank(methodName)) {
			throw new OpenApiException(ErrorCode.PARAMETER_EMPTY,
					"methodName is empty");
		}

		if (StringUtils.isBlank(appkey)) {
			throw new OpenApiException(ErrorCode.PARAMETER_EMPTY,
					"appkey is empty");
		}
		if (StringUtils.isBlank(secretKey)) {
			throw new OpenApiException(ErrorCode.PARAMETER_EMPTY,
					"secretKey is empty");
		}

		String url = this.serverName;

		// cookie
		Map<String, String> cookies = null;

		// 通过调用以下方法，可以打印出最终发送到openapi服务器的请求参数以及url，默认注释
		// printRequest(url,method,params);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("methodName", methodName);
		params.put("args", args);
		params.put("appkey", appkey);
		String nonceStr = SignUtil.createNonce();
		params.put("nonce", nonceStr);
		String timeStamp = SignUtil.createTimestamp();
		params.put("timestamp", timeStamp);
		params.put("signature",
				SignUtil.makeSignature(secretKey, timeStamp, nonceStr));

		if (!StringUtils.isBlank(this.accessToken)) {
			params.put("access_token", this.accessToken);
		}

		System.out.println(JSON.toJSONString(params));

		// 发送请求
		String result = NetWork.postRequest(url, params, cookies, protocol);

		// 通过调用以下方法，可以打印出调用openapi请求的返回码以及错误信息，默认注释
		// printRespond(resp);
		if (!StringUtils.isBlank(result)) {
			result = StringUtils.decodeUnicode(result);
		}

		return result;
	}

	/*
	 * 对结果进行处理
	 */
	private String checkProcess(String result) {
		// 如果验证正常直接返回
		if (true) {
			return result;
		}
		// 如果验证token过期调用refreshToken

		return result;

	}

	/**
	 * 执行API调用
	 * 
	 * @param scriptName
	 *            OpenApi CGI名字 ,如/v3/user/get_info
	 * @param params
	 *            OpenApi的参数列表
	 * @param fp
	 *            上传的文件
	 * @param protocol
	 *            HTTP请求协议 "http" / "https"
	 * @return 返回服务器响应内容
	 */
	public String apiUploadFile(String methodName, Map<String, String> params,
			FilePart fp, String protocol) throws OpenApiException {
		// 检查openid openkey等参数
		if (StringUtils.isBlank(methodName)) {
			throw new OpenApiException(ErrorCode.PARAMETER_EMPTY,
					"methodName is empty");
		}

		// 请求方法
		String method = "post";

		String url = this.serverName;

		// cookie
		HashMap<String, String> cookies = null;

		long startTime = System.currentTimeMillis();

		// 通过调用以下方法，可以打印出最终发送到openapi服务器的请求参数以及url，默认注释
		// printRequest(url,method,params);

		// 发送请求
		String resp = NetWork.postRequestWithFile(url, params, cookies, fp,
				protocol);

		// 统计上报
		// ApiStat.statReport(startTime, serverName, params, method, protocol,
		// rc,
		// scriptName);

		// 通过调用以下方法，可以打印出调用openapi请求的返回码以及错误信息，默认注释
		// printRespond(resp);

		return resp;
	}

	/**
	 * 辅助函数，打印出完整的请求串内容
	 * 
	 * @param url
	 *            请求cgi的url
	 * @param method
	 *            请求的方式 get/post
	 * @param params
	 *            OpenApi的参数列表
	 */
	private void printRequest(String url, String method,
			HashMap<String, String> params) throws OpenApiException {
		System.out.println("==========Request Info==========\n");
		System.out.println("method:  " + method);
		System.out.println("url:  " + url);
		System.out.println("params:");
		System.out.println(params);
		System.out.println("querystring:");
		StringBuilder buffer = new StringBuilder(128);
		Iterator iter = params.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			try {
				buffer.append(
						URLEncoder.encode((String) entry.getKey(), "UTF-8")
								.replace("+", "%20").replace("*", "%2A"))
						.append("=")
						.append(URLEncoder
								.encode((String) entry.getValue(), "UTF-8")
								.replace("+", "%20").replace("*", "%2A"))
						.append("&");
			} catch (UnsupportedEncodingException e) {
				throw new OpenApiException(ErrorCode.MAKE_ACCESS_TOKEN_FAIL, e);
			}
		}
		String tmp = buffer.toString();
		tmp = tmp.substring(0, tmp.length() - 1);
		System.out.println(tmp);
		System.out.println();
	}

	/**
	 * 辅助函数，打印出完整的执行的返回信息
	 * 
	 * @return 返回服务器响应内容
	 */
	private void printRespond(String resp) {
		System.out.println("===========Respond Info============");
		System.out.println(resp);
	}

	public static void main(String[] args) {
		NetWork netWork = new NetWork();
		try {
			String result = netWork.postRequest(
					"http://121.41.46.199/test.php?a=1",
					new HashMap<String, Object>(), null, null);
			System.out.println(result);
		} catch (OpenApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
