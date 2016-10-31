/**
 * 
 */
package com.wanpucs.openapi.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: HttpProxyClient
 * @Description: TODO
 * @author William.jiang
 * @date 2015-12-24 上午11:58:29
 */
public class HttpProxyClientFactory {

    private static final Logger                       LOG                               = LoggerFactory
                                                                                                .getLogger(HttpClientFactory.class);

    private static final int                          DEFAULT_MAX_TOTAL_CONNECTIONS     = 300;
    private static final int                          DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 60;                                       //同一个目标机器的最大并发连接
    private static final int                          DEFAULT_READ_TIMEOUT_MILLISECONDS = (10 * 1000);                              //读超时默认为10秒
    private static final int                          DEFAULT_CONN_TIMEOUT_MILLISECONDS = 5 * 1000;                                 //连接超时默认为5秒

    private static MultiThreadedHttpConnectionManager connectionManager                 = new MultiThreadedHttpConnectionManager();

    private static HttpClient                         httpClient                        = new HttpClient(
                                                                                                connectionManager);

    private static HttpProxyClientFactory             proxyClientFactory                = new HttpProxyClientFactory();

    public static synchronized HttpClient getClient() {
        return httpClient;
    }

    private HttpProxyClientFactory() {
        configClient(0, 0);
    }

    public static HttpProxyClientFactory getInstance() {
        return proxyClientFactory;
    }

    public void configClient(int soTimeOut, int connTimeOut) {
        if (soTimeOut <= 0) {
            soTimeOut = DEFAULT_READ_TIMEOUT_MILLISECONDS;
        }
        if (connTimeOut <= 0) {
            connTimeOut = DEFAULT_CONN_TIMEOUT_MILLISECONDS;
        }
        connectionManager.getParams().setConnectionTimeout(connTimeOut);
        connectionManager.getParams().setSoTimeout(soTimeOut);
        connectionManager.getParams().setMaxTotalConnections(DEFAULT_MAX_TOTAL_CONNECTIONS);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
        connectionManager.getParams().setTcpNoDelay(true);
        connectionManager.getParams().setStaleCheckingEnabled(false);
    }

    public static void main(String[] args) {
        //        String result = null;
        //        try {
        //            result = proxyRequest("post", "http://42.121.30.122/ip.php", "202.52.237.117", 8080);
        //            System.out.println("post###########" + result);
        //        } catch (HttpException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        } catch (IOException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }
        //        try {
        //            result = proxyRequest("get", "http://42.121.30.122/ip.php", "202.52.237.117", 8080);
        //            System.out.println("get###########" + result);
        //        } catch (HttpException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        } catch (IOException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }
    }
}
