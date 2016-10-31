package com.wanpucs.openapi.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vongosling.fengj 2013-5-8
 */
public class IdleConnectionMonitor extends Thread {
	private final ClientConnectionManager connMgr;

	private final Logger log = LoggerFactory.getLogger(IdleConnectionMonitor.class);

	public IdleConnectionMonitor(ClientConnectionManager connMgr) {
		super();
		this.connMgr = connMgr;
	}

	@Override
	public void run() {
		try {
			// Close expired connections
			log.info("Thread {} closeExpiredConnections & closeIdleConnections begin...",
							Thread.currentThread().getName());
			connMgr.closeExpiredConnections();
			// Optionally, close connections
			// that have been idle longer than 30 sec
			connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("IdleConnectionMonitor error", e);
		}
	}

}