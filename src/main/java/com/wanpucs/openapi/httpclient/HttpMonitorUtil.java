package com.wanpucs.openapi.httpclient;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class HttpMonitorUtil {
	
	private static final DecimalFormat percentFormat = new DecimalFormat("0.0%");
	
	public static long caculateSpendTime(ThreadLocal<Long> startTime) {
		if (startTime.get() == null || startTime.get().longValue() == 0L) {
			return 0;
		}
		long endTime = System.currentTimeMillis();
		return endTime - startTime.get().longValue();
	}
	
	public static String getSuccessPercent(final HttpStoreDataInfo storeData) {
		Float successPercent = 0f;
		long totalValue = storeData.getTotal();
		long successValue = storeData.getSuccess();
		if (totalValue > 0) {
			successPercent = BigDecimal.valueOf(successValue)
			.divide(BigDecimal.valueOf(totalValue), 4, BigDecimal.ROUND_UP)
			.floatValue();
			if (successPercent > 1) {
				successPercent = 1f;
			}
		}
		return percentFormat.format(successPercent);	
	}
	
	public static String getFailurePercent(final HttpStoreDataInfo storeData) {
		Float failurePercent = 0f;
		long totalValue = storeData.getTotal();
		long failureValue = storeData.getFailure();
		if (totalValue > 0) {			
			failurePercent = BigDecimal.valueOf(failureValue)
			.divide(BigDecimal.valueOf(totalValue), 4, BigDecimal.ROUND_UP)
			.floatValue();
			if (failurePercent > 1) {
				failurePercent = 1f;
			}
		}
		return percentFormat.format(failurePercent);
	}
}