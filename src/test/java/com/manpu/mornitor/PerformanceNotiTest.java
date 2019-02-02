package com.manpu.mornitor;

import com.manpu.crawler.helper.MonitoringHelper;
import org.junit.Test;

public class PerformanceNotiTest {
	@Test
	public void test() {
		System.out.println(MonitoringHelper.collect());
	}
}
