package com.wanpucs.openapi.httpclient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpMonitorStore {
	private static final Logger log = LoggerFactory.getLogger(HttpMonitorStore.class);
	// 默认延迟时间是1分钟
	public static long DELAY_TIME = 1 *60 * 1000;
	// 默认的第一次启动线程的延迟时间，默认1分钟
	public static long INITIAL_DELAY_TIME = 1 * 60 * 1000;
	
	private static final HttpMonitorStore monitorStore = new HttpMonitorStore();

	private Map<String, Map<String, AtomicReference<HttpStoreDataInfo>>> storeMap = new ConcurrentHashMap<String, Map<String, AtomicReference<HttpStoreDataInfo>>>();

	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
			1);

	private HttpMonitorStore() {
		executor.scheduleWithFixedDelay(new LogMonitorDataTask(),
				INITIAL_DELAY_TIME, DELAY_TIME, TimeUnit.MILLISECONDS);
	}
	
	public static HttpMonitorStore newInstance() {
		return monitorStore;
	}
	
	public HttpStoreDataInfo addSuccessAccess(String monitorName, String key, long spendTime) {

		HttpStoreDataInfo storeData = getStoreDataInfo(monitorName, key);
		if (storeData != null) {
			storeData.addSuccessAccess();
			storeData.addTotalAccess();
			storeData.addSpendTime(spendTime);
		}else{ //正常不会为null，除非getStoreDataInfo有bug
			log.warn("MonitorStore.getStoreDataInfo() 意外返回null，影响到了监控 ");
		}
		return storeData;
	}

	public HttpStoreDataInfo addFailureAccess(String monitorName, String key, long spendTime) {

		HttpStoreDataInfo storeData = getStoreDataInfo(monitorName, key);
		if (storeData != null) {
			storeData.addFailureAccess();
			storeData.addTotalAccess();
			storeData.addSpendTime(spendTime);
		}
		return storeData;
	}
	
	/**
	 * 
	 * @param monitorName
	 * @param key
	 * @return null 当程序出现bug时
	 */
	public HttpStoreDataInfo enterThread(String monitorName, String key) {

		HttpStoreDataInfo storeData = getStoreDataInfo(monitorName, key);
		if (storeData != null) {
			storeData.enterThread();
		}
		return storeData;
	}

	/**
	 * 
	 * @param monitorName
	 * @param key
	 * @return monitorName:key对应的StoreDataInfo对象，如果不存在则没有必要释放返回null
	 */
	public HttpStoreDataInfo releaseThread(String monitorName, String key) {
		// 简单获取monitorName:key对应的StoreDataInfo实例，如果不存在将直接返回null，以提高性能
		HttpStoreDataInfo storeData = null ;
		Map<String,AtomicReference<HttpStoreDataInfo>> dataMap = storeMap.get(monitorName);
		if(dataMap!=null){
			storeData = dataMap.get(key).get();
		};
		if (storeData != null) {
			long cnt = storeData.releaeThread();
			if (cnt < 0) {
				storeData.getThreadCnt().set(0);
			}
		}
		return storeData;
	}
	
	private class LogMonitorDataTask implements Runnable {
		public void run() {
			Map<String, Map<String, HttpStoreDataInfo>> viewMap = monitorStore.getAndCleanAllToZero();
			
			for (Iterator<Map.Entry<String, Map<String, HttpStoreDataInfo>>> it = viewMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Map<String, HttpStoreDataInfo>> entry = it.next();
				String monitorName = entry.getKey();
				log.info("==============monitorName:"+monitorName);
				Map<String, HttpStoreDataInfo> sourceMap = entry.getValue();
				TreeMap<String, HttpStoreDataInfo> storeMap = new TreeMap<String, HttpStoreDataInfo>(sourceMap);
				for (Iterator<Map.Entry<String,HttpStoreDataInfo>> storeIt = storeMap.entrySet().iterator(); storeIt.hasNext();) {
					Map.Entry<String, HttpStoreDataInfo> storeEntry = storeIt.next();
					String key = storeEntry.getKey();
					HttpStoreDataInfo storeData = storeEntry.getValue();
					log.info("=========monitor key:"+key);
					log.info("success:"+storeData.getAccessSuccess()+" failure:"+storeData.getAccessFailure()
							+" total:"+storeData.getTotal()+" thread count:"+storeData.getThreadCnt()
							+" spend avg time:"+storeData.getAvgTime());
				}
			}
		}

	}
	
public HttpStoreDataInfo getStoreDataInfo(String monitorName, String key) {
		
		HttpStoreDataInfo storeData = null;
		Map<String, AtomicReference<HttpStoreDataInfo>> keyHttpStoreDataInfoMap = storeMap.get(monitorName);
		if (keyHttpStoreDataInfoMap==null) {// 判空应该比contains方法调用更快
			synchronized (storeMap) {
				keyHttpStoreDataInfoMap = storeMap.get(monitorName); //获得锁后可能已经存在了，再取一次
				if(keyHttpStoreDataInfoMap == null){
					keyHttpStoreDataInfoMap = new ConcurrentHashMap<String, AtomicReference<HttpStoreDataInfo>>();
					storeMap.put(monitorName, keyHttpStoreDataInfoMap);
					AtomicReference<HttpStoreDataInfo> storeDataRef = new AtomicReference<HttpStoreDataInfo>();
					storeData = new HttpStoreDataInfo(monitorName, key);
					storeDataRef.set(storeData);
					keyHttpStoreDataInfoMap.put(key, storeDataRef);
				}else{// 轮到锁时，monitorName已经有了
					AtomicReference<HttpStoreDataInfo> storeDataRef = keyHttpStoreDataInfoMap.get(key);
					if(storeDataRef==null){ //但key还没有，但是这里处于临界区，无需加锁
						storeDataRef = new AtomicReference<HttpStoreDataInfo>();
						storeData = new HttpStoreDataInfo(monitorName,key);
						storeDataRef.set(storeData);
						keyHttpStoreDataInfoMap.put(key, storeDataRef);	
					}else{
						storeData = keyHttpStoreDataInfoMap.get(key).get();
					}
				}	
			}
		} else {//有monitorName
			AtomicReference<HttpStoreDataInfo> storeDataRef = keyHttpStoreDataInfoMap.get(key);
			if(storeDataRef==null){//但是没有key
				synchronized (storeMap) {
					storeDataRef = keyHttpStoreDataInfoMap.get(key); //获得锁后再次读尝试
					if(storeDataRef == null){
						storeDataRef = new AtomicReference<HttpStoreDataInfo>();
						storeData = new HttpStoreDataInfo(monitorName,key);
						storeDataRef.set(storeData);
						keyHttpStoreDataInfoMap.put(key, storeDataRef);
					}else{
						storeData = storeDataRef.get();
					}
				}
			}else{
				storeData = storeDataRef.get();
			}
		}
		return storeData;
	}
	
	/**
	 * 把持有的所有数据都清空，只保留并发数，清空时不允许其它线程访问
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, Map<String, HttpStoreDataInfo>> getAndCleanAllToZero() {
		Map<String, Map<String, HttpStoreDataInfo>> viewMap = GlobelViewMap.getNewViewMap();

		for (Iterator<Map.Entry<String, Map<String, AtomicReference<HttpStoreDataInfo>>>> it = storeMap
				.entrySet().iterator(); it.hasNext();) {

			Map.Entry<String, Map<String, AtomicReference<HttpStoreDataInfo>>> entry = it
					.next();
			String monitorName = entry.getKey();
			Map<String, AtomicReference<HttpStoreDataInfo>> data = entry
					.getValue();

			Map<String, HttpStoreDataInfo> viewTmpStoreMap = new HashMap<String, HttpStoreDataInfo>();
			viewMap.put(monitorName, viewTmpStoreMap);
			for (Iterator<Map.Entry<String, AtomicReference<HttpStoreDataInfo>>> storeIt = data
					.entrySet().iterator(); storeIt.hasNext();) {
				Map.Entry<String, AtomicReference<HttpStoreDataInfo>> storeEntry = storeIt
						.next();
				String key = storeEntry.getKey();

				HttpStoreDataInfo newStoreInfo = new HttpStoreDataInfo(
						monitorName, key);// 做清空，只保留总线程次数
				newStoreInfo.setThreadCnt(storeEntry.getValue().get()
						.getThreadCnt());

				HttpStoreDataInfo oldStoreInfo = storeEntry.getValue()
						.getAndSet(newStoreInfo);// 将老值返回给viewMap，再设置新值
				viewTmpStoreMap.put(key, oldStoreInfo);
			}
		}
		return viewMap;
	}
}
