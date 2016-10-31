/**
 * 
 */
package com.wanpucs.openapi.httpclient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @ClassName: TestProxyIp
 * @Description: TODO
 * @author William.jiang
 * @date 2015-12-23 下午4:21:29
 */
public class TestProxyIp {

	public static void main(String[] args) throws IOException {
		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");
		// 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
		String ip = "27.12.98.126";
		System.getProperties().setProperty("http.proxyHost", ip);
		System.getProperties().setProperty("http.proxyPort", "8090");

		// 确定代理是否设置成功
		System.out.println(getHtml("http://42.121.30.122/ip.php"));

	}

	private static String getHtml(String address) {
		StringBuffer html = new StringBuffer();
		String result = null;
		try {
			URL url = new URL(address);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
			BufferedInputStream in = new BufferedInputStream(
					conn.getInputStream());

			try {
				String inputLine;
				byte[] buf = new byte[4096];
				int bytesRead = 0;
				while (bytesRead >= 0) {
					inputLine = new String(buf, 0, bytesRead, "utf-8");
					html.append(inputLine);
					bytesRead = in.read(buf);
					inputLine = null;
				}
				buf = null;
			} finally {
				in.close();
				conn = null;
				url = null;
			}
			// result = new String(html.toString().trim().getBytes("utf-8"),
			// "gb2312").toLowerCase();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			html = null;
		}
		return result;
	}
}
