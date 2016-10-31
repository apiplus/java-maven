package com.wanpucs.openapi.httpclient;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 对外部系统打点的监控都是存放在此对象里，
 * 更新清除的时候，可以做原子性更新
 *
 */
public class HttpStoreDataInfo {
	
	private String monitorName;//第1层Map的key
	
	private String key;//第2层Map的key
	
	private AtomicLong accessTotal = new AtomicLong(0);//单位时间内访问总数
	
	private AtomicLong accessSuccess = new AtomicLong(0);//单位时间内成功访问数
	
	private AtomicLong accessFailure = new AtomicLong(0);//单位时间内失败访问数
	
	private AtomicLong accessSpendTime = new AtomicLong(0);//单位时间内访问总耗时
	
	private AtomicLong threadCnt = new AtomicLong(0);//单位时间内访问并发数
	
	public HttpStoreDataInfo(String monitorName, String key) {
		this.monitorName = monitorName;
		this.key = key;
	}
	
	public long addTotalAccess() {
		return accessTotal.incrementAndGet();
	}
	
	public long addSuccessAccess() {
		return accessSuccess.incrementAndGet();
	}
	
	public long addFailureAccess() {
		return accessFailure.incrementAndGet();
	}
	
	public long addSpendTime(long spendTime) {
		return accessSpendTime.addAndGet(spendTime);
	}
	
	public long enterThread() {
		return threadCnt.incrementAndGet();
	}
	
	public long releaeThread() {
		return threadCnt.decrementAndGet();
	}
	
	public long getTotal() {
		return accessTotal.get();
	}
	
	public long getSuccess() {
		if (accessSuccess.get() > accessTotal.get()) {
			//如果成功数还大于总数，则让成功数等于总数
			return accessTotal.get();
		} 
		return accessSuccess.get();
	}
	
	public long getFailure() {
		if (accessFailure.get() > accessTotal.get()) {
			//如果失败数还大于总数，则让失败数等于总数
			return accessTotal.get();
		}
		return accessFailure.get();
	}
	
	public long getAvgTime() {
		if (accessTotal.get() == 0) {
			return 0;
		}
		return accessSpendTime.get() / accessTotal.get();
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public AtomicLong getAccessTotal() {
		return accessTotal;
	}

	public void setAccessTotal(AtomicLong accessTotal) {
		this.accessTotal = accessTotal;
	}

	public AtomicLong getAccessSuccess() {
		return accessSuccess;
	}

	public void setAccessSuccess(AtomicLong accessSuccess) {
		this.accessSuccess = accessSuccess;
	}

	public AtomicLong getAccessFailure() {
		return accessFailure;
	}

	public void setAccessFailure(AtomicLong accessFailure) {
		this.accessFailure = accessFailure;
	}

	public AtomicLong getAccessSpendTime() {
		return accessSpendTime;
	}

	public void setAccessSpendTime(AtomicLong accessSpendTime) {
		this.accessSpendTime = accessSpendTime;
	}

	public String getMonitorName() {
		return monitorName;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public AtomicLong getThreadCnt() {
		return threadCnt;
	}

	public void setThreadCnt(AtomicLong threadCnt) {
		this.threadCnt = threadCnt;
	} 
}