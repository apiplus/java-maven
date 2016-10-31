package com.wanpucs.openapi.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

public class HttpClientManager {
    private static HttpClientFactory httpClientFactory = new HttpClientFactory(0, 0);

    public static String doPost(String requestPath, Map<String, String> params, String encoding) {
        return httpClientFactory.doPost(requestPath, params, encoding);
    }

    public static String doGet(String url, String encoding) {
        return httpClientFactory.doGet(url, encoding);
    }

    public static HttpResponse doGetForResponse(String url, String encoding) {
        return httpClientFactory.doGetForGetResponse(url, encoding);
    }

    public static HttpResponse doPostForGetResponse(String reqURL, Map<String, String> params, String encoding) {
        return httpClientFactory.doPostForGetResponse(reqURL, params, encoding);
    }

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        //                System.out.println(doPost("http://42.121.30.122/ip.php", params, "utf-8"));
        //                System.out.println(doGet("http://42.121.30.122/ip.php", "utf-8"));
        //        System.out.println(proxyRequest("GET", "http://42.121.30.122/ip.php", params, "utf-8"));

        try {
            URL url = new URL("http://42.121.30.122/ip.php");
            //            URLConnection connection = url.openConnection();

            // /创建代理服务器
            InetSocketAddress addr = new InetSocketAddress("112.126.72.101", 8080);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
            //        Authenticator.setDefault(new MyAuthenticator("username", "password"));// 设置代理的用户和密码
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);// 设置代理访问
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            while (true) {
                String s = reader.readLine();
                if (s != null) {
                    System.out.println(s);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
