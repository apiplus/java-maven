package com.wanpucs.openapi.tx.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 自定义签名证书管理类 (接受任意来源证书)
 * 
 * @version 1.0.0
 * @since jdk1.7
 * @author www.wanpucs.com
 * @copyright © 2016, www.wanpucs.com. All rights reserved.
 * @History: initialization
 */

public class MyX509TrustManager implements X509TrustManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.
	 * X509Certificate[], java.lang.String)
	 */
	public void checkClientTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.
	 * X509Certificate[], java.lang.String)
	 */
	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
