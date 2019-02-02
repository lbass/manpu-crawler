package com.manpu.crawler.helper;

import com.sun.management.OperatingSystemMXBean;
import lombok.Data;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;

public class MonitoringHelper {

	private static final Runtime runtime = Runtime.getRuntime();

	public static ServerMonitorMetric collect() {
		ServerMonitorMetric serverMonitorMetric = new ServerMonitorMetric();
		int mb = 1024 * 1024;
		OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		// CPU
		BigDecimal cpuLoad = new BigDecimal(os.getProcessCpuLoad());
		serverMonitorMetric.setCpuLoad(cpuLoad.multiply(BigDecimal.valueOf(100)).setScale(1, 1));

		// MEMORY
		long physicalMemorySize = os.getTotalPhysicalMemorySize();
		long physicalFreeMemorySize = os.getFreePhysicalMemorySize();
		BigDecimal totalUsedMemory = BigDecimal.valueOf(physicalMemorySize - physicalFreeMemorySize)
				.divide(BigDecimal.valueOf(physicalMemorySize), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.setScale(1, 1);
		BigDecimal totalFreeMemory = BigDecimal.valueOf(physicalFreeMemorySize)
				.divide(BigDecimal.valueOf(physicalMemorySize), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.setScale(1, 1);
		long crawlerTotal = runtime.totalMemory();
		long crawlerFree = runtime.freeMemory();
		long crawlerUsed = crawlerTotal - crawlerFree;

		// CRAWLER MEMORY
		BigDecimal crawlerTotalMemory = BigDecimal.valueOf(crawlerTotal)
				.divide(BigDecimal.valueOf(mb), 2, BigDecimal.ROUND_HALF_UP)
				.setScale(1, 1);
		BigDecimal crawlerMemoryUsed = BigDecimal.valueOf(crawlerUsed)
				.divide(BigDecimal.valueOf(crawlerTotal), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.setScale(1, 1);
		BigDecimal crawlerMemoryFree = BigDecimal.valueOf(crawlerFree)
				.divide(BigDecimal.valueOf(crawlerTotal), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.setScale(1, 1);

		serverMonitorMetric.setPhysicalMemorySize(BigDecimal.valueOf(physicalMemorySize).divide(BigDecimal.valueOf(mb), 2, BigDecimal.ROUND_HALF_UP));
		serverMonitorMetric.setPhysicalFreeMemorySize(BigDecimal.valueOf(physicalFreeMemorySize).divide(BigDecimal.valueOf(mb), 2, BigDecimal.ROUND_HALF_UP));
		serverMonitorMetric.setTotalUsedPer(totalUsedMemory);
		serverMonitorMetric.setTotalFreePer(totalFreeMemory);

		serverMonitorMetric.setCrawlerTotal(crawlerTotalMemory);
		serverMonitorMetric.setCrawlerUsed(crawlerMemoryUsed);
		serverMonitorMetric.setCrawlerFree(crawlerMemoryFree);

		return serverMonitorMetric;
	}

	@Data
	public static class ServerMonitorMetric {
		private String env;
		private BigDecimal cpuLoad;
		private BigDecimal physicalMemorySize;
		private BigDecimal physicalFreeMemorySize;
		private BigDecimal totalUsedPer;
		private BigDecimal totalFreePer;

		private BigDecimal crawlerTotal;
		private BigDecimal crawlerUsed;
		private BigDecimal crawlerFree;

		@Override
		public String toString() {
			return "[" + this.env + "] Server State ====" +
					"\n cpuLoad =" + cpuLoad + "%" +
					"\n physicalMemorySize=" + physicalMemorySize +
					"\n physicalFreeMemorySize=" + physicalFreeMemorySize +
					"\n totalUsedPer=" + totalUsedPer + "%" +
					"\n totalFreePer=" + totalFreePer +
					"\n crawlerTotal=" + crawlerTotal +
					"\n crawlerUsed=" + crawlerUsed + "%" +
					"\n crawlerFree=" + crawlerFree + "%";
		}
	}
}
