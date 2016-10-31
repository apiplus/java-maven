/**
 * 
 */
package com.wanpucs.openapi.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.wanpucs.openapi.utils.StringUtils;

/**
 * @ClassName: HttpProxyClientManager
 * @Description: TODO
 * @author William.jiang
 * @date 2016-1-15 下午4:39:35
 */
public class HttpProxyClientManager {

	@SuppressWarnings("finally")
	public static String proxyRequest(String requestType, String url,
			String proxyHost, int proxyPort) {
		HttpClient httpClient = HttpProxyClientFactory.getClient();
		// 设置代理服务器地址和端口
		if (!StringUtils.isEmpty(proxyHost) && proxyPort > 0) {
			httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
		}
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		HttpMethod method = null;
		String result = null;
		try {
			if (StringUtils.isEmpty(requestType)
					|| requestType.equalsIgnoreCase("get")) {
				method = new GetMethod(url);
			} else {
				method = new PostMethod(url);
			}
			method.setRequestHeader("Connection", "close");

			httpClient.executeMethod(method);

			InputStream resStream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					resStream));
			StringBuffer resBuffer = new StringBuffer();
			String resTemp = "";
			while ((resTemp = br.readLine()) != null) {
				resBuffer.append(resTemp);
			}
			result = resBuffer.toString();

			br.close();
			resStream.close();
			resStream = null;
			br = null;
		} catch (Exception e) {
			System.out.println("proxyRequest#" + e.getMessage());
		} finally {
			// 释放连接
			method.releaseConnection();
			return result;
		}
	}

	private static HttpMethod getHttpMethod(String url, String requestType) {
		HttpMethod method = null;
		if (StringUtils.isEmpty(requestType)
				|| requestType.equalsIgnoreCase("get")) {
			method = new GetMethod(url);
		}
		if (requestType.equalsIgnoreCase("post")) {
			method = new PostMethod(url);
		}

		return method;
	}

	public static HttpMethod getMethodForProxyRequest(String requestType,
			String url, String proxyHost, String proxyPort)
			throws HttpException, IOException {
		HttpClient httpClient = HttpProxyClientFactory.getClient();

		// 设置代理服务器地址和端口
		if (!StringUtils.isEmpty(proxyHost) && !StringUtils.isEmpty(proxyPort)) {
			httpClient.getHostConfiguration().setProxy(proxyHost,
					Integer.valueOf(proxyPort));
		}
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		HttpMethod method = getHttpMethod(url, requestType);

		httpClient.executeMethod(method);

		return method;
	}

	public static HttpMethod getMethodForRequestWithHeader(String requestType,
			String url, String proxyHost, String proxyPort,
			Map<String, String> headerParamsMap) throws HttpException,
			IOException {

		HttpMethod method = getHttpMethod(url, requestType);

		if (headerParamsMap != null && headerParamsMap.size() > 0) {
			for (Map.Entry<String, String> entry : headerParamsMap.entrySet()) {
				method.addRequestHeader(entry.getKey(), entry.getValue());
			}
		}

		HttpClient httpClient = HttpProxyClientFactory.getClient();
		// 设置代理服务器地址和端口
		if (!StringUtils.isEmpty(proxyHost) && !StringUtils.isEmpty(proxyPort)) {
			httpClient.getHostConfiguration().setProxy(proxyHost,
					Integer.valueOf(proxyPort));
		}

		httpClient.executeMethod(method);

		return method;

	}

	public static String loadRequest(String requestType, String httpUrl,
			String proxyHost, String proxyPort, Map<String, String> headerMap) {
		HttpMethod method = null;
		String htmlInfo = null;
		try {
			method = getMethodForRequestWithHeader(requestType, httpUrl,
					proxyHost, proxyPort, headerMap);
			InputStream resStream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					resStream));
			StringBuffer resBuffer = new StringBuffer();
			String resTemp = "";
			while ((resTemp = br.readLine()) != null) {
				resBuffer.append(resTemp);
			}
			htmlInfo = resBuffer.toString();
			br.close();
			resStream.close();

			resStream = null;
			br = null;

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
		}
		return htmlInfo;
	}

}
