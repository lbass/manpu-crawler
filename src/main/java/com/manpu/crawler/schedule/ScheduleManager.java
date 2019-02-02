package com.manpu.crawler.schedule;

import com.manpu.crawler.alert.slack.SlackSendData.ALERT_LEVEL;
import com.manpu.crawler.alert.slack.SlackSender;
import com.manpu.crawler.helper.MonitoringHelper;
import com.manpu.crawler.helper.MonitoringHelper.ServerMonitorMetric;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.ManagerWorker;
import com.manpu.crawler.worker.PostProcessWorker;
import com.manpu.crawler.worker.RatingUpdateWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ScheduleManager {

    private Logger logger = LoggerFactory.getLogger(ScheduleManager.class);

    // 60 S
    private final static int PERIOD = 1000 * 60;

    // 30 S
    private final static int PROCESS_PERIOD = 1000 * 30;

    @Autowired
    private ManagerWorker managerWorker;

    @Autowired
    private SlackSender slackSender;

    @Autowired
    private PostProcessWorker postProcessWorker;

    @Autowired
    private CrawlerMetric crawlerMetric;

    @Autowired
    private RatingUpdateWorker ratingUpdateWorker;

    @Autowired
    private Environment env;

    private String profile = null;

    @PostConstruct
    public void init() {
        if(env.getActiveProfiles() == null || env.getActiveProfiles().length == 0) {
            profile = "DEV";
        } else {
            for(String springProfile : env.getActiveProfiles()) {
                profile = springProfile;
            }
        }
    }

    @Scheduled(initialDelay = 1000 * 10, fixedDelay = PERIOD)
    public void crawlingManagerJob() {
        managerWorker.process();
    }

    @Scheduled(initialDelay = 1000 * 50, fixedDelay = PROCESS_PERIOD)
    public void crawledDataProcessJob() {
         postProcessWorker.process();
    }

    @Scheduled(initialDelay = 1000 * 60 * 1, fixedDelay = PERIOD * 3)
    public void reportMetric() {
        logger.debug("Naver List Count: {}", crawlerMetric.getNaverListWorkCount());
        logger.debug("Daum List Count: {}", crawlerMetric.getDaumListWorkCount());
        logger.debug("Lezhin List Count: {}", crawlerMetric.getLezhinListWorkCount());
        logger.debug("Kakao List Count: {}", crawlerMetric.getKakaoListWorkCount());

        logger.debug("Naver List Error Count: {}", crawlerMetric.getNaverListWorkErrorCount());
        logger.debug("Daum List Error Count: {}", crawlerMetric.getDaumListWorkErrorCount());
        logger.debug("Lezhin List Error Count: {}", crawlerMetric.getLezhinListWorkErrorCount());
        logger.debug("Kakao List Error Count: {}", crawlerMetric.getKakaoListWorkErrorCount());

        logger.debug("Naver Episode Count: {}", crawlerMetric.getNaverEpisodeWorkCount());
        logger.debug("Daum Episode Count: {}", crawlerMetric.getDaumEpisodeWorkCount());
        logger.debug("Lezhin Episode Count: {}", crawlerMetric.getLezhinEpisodeWorkCount());
        logger.debug("Kakao Episode Count: {}", crawlerMetric.getKakaoEpisodeWorkCount());

        logger.debug("Process Count: {}", crawlerMetric.getProcessCount());
        logger.debug("Episode Insert Count: {}", crawlerMetric.getEpisodeInsertCount());

        logger.debug("Push Api Latency Average : {}", crawlerMetric.getPushApiAverage());

        crawlerMetric.clearMetric();

        ServerMonitorMetric serverMonitorMetric = MonitoringHelper.collect();
        serverMonitorMetric.setEnv(this.profile);
        if(serverMonitorMetric.getCpuLoad().longValue() > 60) {
            logger.warn("[{}] cpu 60% 초과 {}", profile, serverMonitorMetric);
            slackSender.sendSlack(serverMonitorMetric.toString(), ALERT_LEVEL.WARN);
        } else {
            logger.info("[{}]  monitoring {}", profile, serverMonitorMetric);
        }
    }

    @Scheduled(cron = "0 30 0-22 * * *")
    public void updateRating() {
        ratingUpdateWorker.updateRating();
    }
}
