package com.manpu.crawler;

import com.manpu.crawler.alert.slack.SlackSendData.ALERT_LEVEL;
import com.manpu.crawler.alert.slack.SlackSender;
import com.manpu.crawler.cache.ManpuCacheMap;
import com.manpu.crawler.worker.WebtoonWorker;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ManpuCrawlerRunner  implements ApplicationRunner {

    @Autowired
    private SlackSender slackSender;

    private Logger logger = LoggerFactory.getLogger(ManpuCrawlerRunner.class);

    private final static int WORKER_COUNT = 4;
    private final static int DELAY = 200;

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    private ManpuCacheMap manpuCacheMap;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        manpuCacheMap.collectWebtoonCache();

        BasicThreadFactory factory =
                new BasicThreadFactory.Builder().
                        namingPattern("crawler" + "-%d").
                        daemon(true).
                        priority(Thread.MAX_PRIORITY).build();
        ScheduledExecutorService scheduledThreadPool =
                Executors.newScheduledThreadPool(WORKER_COUNT, factory);

        for(int i = 0 ; i < WORKER_COUNT ; i++) {
            WebtoonWorker weebtoonWorker = applicationContext.getBean(WebtoonWorker.class);
            scheduledThreadPool.scheduleAtFixedRate(weebtoonWorker, 0, DELAY, TimeUnit.MILLISECONDS);
        }

        DateTime now = new DateTime(System.currentTimeMillis());
        slackSender.sendSlack(now.toString("yyyy-MM-dd hh:mm:ss") +" Crawler Start", ALERT_LEVEL.DANGER);
    }
}