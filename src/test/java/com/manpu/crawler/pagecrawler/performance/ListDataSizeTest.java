package com.manpu.crawler.pagecrawler.performance;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDataSizeTest {

    private final int DATA_COUNT = 21_000_0;

    @Test
    public void testMemory() throws InterruptedException {
        StopWatch stopWatch = StopWatch.createStarted();

        List<Map<String, String>> testData = new ArrayList<>();
        for(int i = 0 ; i < DATA_COUNT / 2 ; i++) {
            HashMap<String, String> mapData = new HashMap<>();
            mapData.put("a000" + i, "messageTest");
            testData.add(mapData);
        }
        stopWatch.stop();
        System.out.println(stopWatch.toString());

        Runtime runtime = Runtime.getRuntime();
        while(true) {
            Thread.sleep(500);
            viewMonitor(runtime);
        }
    }

    public void viewMonitor(Runtime runtime) {
        long total, free, used;
        int mb = 1024*1024;

        total = runtime.totalMemory();
        free = runtime.freeMemory();
        used = total - free;
        System.out.println("\nTotal Memory: " + total / mb + "MB");
        System.out.println(" Memory Used: " + used / mb + "MB");
        System.out.println(" Memory Free: " + free / mb + "MB");
        System.out.println("Percent Used: " + ((double)used/(double)total)*100 + "%");
        System.out.println("Percent Free: " + ((double)free/(double)total)*100 + "%");
    }
}
