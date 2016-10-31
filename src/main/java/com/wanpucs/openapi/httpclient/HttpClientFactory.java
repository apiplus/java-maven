package com.wanpucs.openapi.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient连接池封装，需要配置成单例的
 * 
 * @author moxiang
 */
public class HttpClientFactory {
    private static final Logger LOG                               = LoggerFactory.getLogger(HttpClientFactory.class);

    private static final int    DEFAULT_MAX_TOTAL_CONNECTIONS     = 300;
    private static final int    DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 60;                                              //同一个目标机器的最大并发连接
    private static final int    DEFAULT_READ_TIMEOUT_MILLISECONDS = (10 * 1000);                                     //读超时默认为10秒
    private static final int    DEFAULT_CONN_TIMEOUT_MILLISECONDS = 5 * 1000;                                        //连接超时默认为5秒
    private static final int    INIT_DELAY                        = 5 * 1000;
    private static final int    CHECK_INTERVAL                    = 5 * 60 * 1000;

    protected HttpMonitorStore  monitorStore                      = HttpMonitorStore.newInstance();
    protected ThreadLocal<Long> startTime                         = new ThreadLocal<Long>();

    private HttpClient          httpClient;

    public HttpClientFactory(int soTimeOut, int connTimeOut) {
        if (soTimeOut <= 0) {
            soTimeOut = DEFAULT_READ_TIMEOUT_MILLISECONDS;
        }
        if (connTimeOut <= 0) {
            connTimeOut = DEFAULT_CONN_TIMEOUT_MILLISECONDS;
        }

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(schemeRegistry);
        connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);

        ScheduledExecutorService scheduledExeService = Executors.newScheduledThreadPool(1, new DaemonThreadFactory(
                "Http-client-ConenctionPool-Monitor"));
        scheduledExeService.scheduleAtFixedRate(new IdleConnectionMonitor(connectionManager), INIT_DELAY,
                CHECK_INTERVAL, TimeUnit.MILLISECONDS);

        this.httpClient = new DefaultHttpClient(connectionManager);
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setSoTimeout(params, soTimeOut);
        HttpConnectionParams.setConnectionTimeout(params, connTimeOut);
        HttpConnectionParams.setTcpNoDelay(params, Boolean.TRUE);
        HttpConnectionParams.setStaleCheckingEnabled(params, Boolean.FALSE);
    }

    public HttpResponse doPostForGetResponse(String reqURL, Map<String, String> params, String encoding) {
        HttpResponse response = null;
        try {
            HttpPost httpPost = buildHttpPostRequest(reqURL, params, encoding);
            //============================================
            // 为了计算每个请求的执行时间，需要把进入方法时的开始时间计算出来
            long time = System.currentTimeMillis();
            startTime.set(time);
            monitorStore.enterThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================

            response = httpClient.execute(httpPost);

            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addSuccessAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================

        } catch (SocketTimeoutException e) {
            LOG.warn("Read time out!", e);
            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addFailureAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================
        } catch (SSLPeerUnverifiedException e) {
            LOG.warn("Peer not authenticated!", e);
            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addFailureAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addFailureAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================
        } finally {
            startTime.set(null);
        }
        return response;
    }

    public HttpResponse doGetForGetResponse(String url, String encoding) {
        HttpResponse response = null;
        HttpGet httpget = new HttpGet(url);
        try {
            //============================================
            // 为了计算每个请求的执行时间，需要把进入方法时的开始时间计算出来
            long time = System.currentTimeMillis();
            startTime.set(time);
            monitorStore.enterThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet);
            //============================================

            response = httpClient.execute(httpget);

            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addSuccessAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet);

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addFailureAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet);
            //============================================
        } finally {
            startTime.set(null);
        }
        return response;
    }

    /**
     * http client入口
     * 
     * @param reqURL
     * @param params
     * @param encoding
     * @return
     */
    public String doPost(String reqURL, Map<String, String> params, String encoding) {
        String responseContent = "";
        try {
            HttpPost httpPost = buildHttpPostRequest(reqURL, params, encoding);
            //============================================
            // 为了计算每个请求的执行时间，需要把进入方法时的开始时间计算出来
            long time = System.currentTimeMillis();
            startTime.set(time);
            monitorStore.enterThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================

            HttpResponse response = httpClient.execute(httpPost);

            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addSuccessAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================
            validateResponse(response, httpPost);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseContent = EntityUtils.toString(entity, encoding);
            } else {
                LOG.warn("Http entity is null! request url is {},response status is {}", reqURL,
                        response.getStatusLine());
            }

        } catch (SocketTimeoutException e) {
            LOG.warn("Read time out!", e);
            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addFailureAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================
        } catch (SSLPeerUnverifiedException e) {
            LOG.warn("Peer not authenticated!", e);
            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addFailureAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addFailureAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoPost);
            //============================================
        } finally {
            startTime.set(null);
        }
        return responseContent;
    }

    /**
     * get 方法入口
     * 
     * @param url
     * @param encoding
     * @return
     */
    public String doGet(String url, String encoding) {
        String result = "";
        HttpGet httpget = new HttpGet(url);
        try {
            //============================================
            // 为了计算每个请求的执行时间，需要把进入方法时的开始时间计算出来
            long time = System.currentTimeMillis();
            startTime.set(time);
            monitorStore.enterThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet);
            //============================================

            HttpResponse response = httpClient.execute(httpget);

            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addSuccessAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet);
            //============================================

            validateResponse(response, httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, encoding);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            //============================================
            long spendTime = HttpMonitorUtil.caculateSpendTime(startTime);
            monitorStore.addFailureAccess(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet, spendTime);
            monitorStore.releaseThread(ConstantParams.MonitorName, ConstantParams.MonitorKeyDoGet);
            //============================================
        } finally {
            startTime.set(null);
        }
        return result;
    }

    private HttpPost buildHttpPostRequest(String url, Map<String, String> params, String encoding)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Entry<String, String>> paramEntrys = params.entrySet();
            for (Entry<String, String> entry : paramEntrys) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
        }
        return httpPost;
    }

    private void validateResponse(HttpResponse response, HttpGet get) throws IOException {
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() >= HttpStatus.SC_MULTIPLE_CHOICES) {
            LOG.warn("Did not receive successful HTTP response: status code = {}, status message = {}",
                    status.getStatusCode(), status.getReasonPhrase());
            get.abort();
            return;
        }
    }

    private void validateResponse(HttpResponse response, HttpPost post) throws IOException {
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() >= HttpStatus.SC_MULTIPLE_CHOICES) {
            LOG.warn("Did not receive successful HTTP response: status code = {}, status message = {}",
                    status.getStatusCode(), status.getReasonPhrase());
            post.abort();
            return;
        }
    }

}
