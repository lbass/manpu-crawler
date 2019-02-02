package com.manpu.crawler.config;

import com.manpu.crawler.alert.slack.SlackSendData.ALERT_LEVEL;
import com.manpu.crawler.alert.slack.SlackSender;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan("com.manpu.crawler")
@EnableTransactionManagement
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties
public class ManPuCrawlerConfiguration {

    @Autowired
    private SlackSender slackSender;

    private Logger logger = LoggerFactory.getLogger(ManPuCrawlerConfiguration.class);

    @Autowired
    private Environment env;

    private static final int SCHEDULE_THREAD_COUNT = 4;

    // schedule은 기본 설정이 단일 스레드로 실행되어 있기 때문에 스레드 갯수를 지정해야한다.
    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(SCHEDULE_THREAD_COUNT);
    }

    @PostConstruct
    public void ManPuCrawlerConfiguration() {
        if(env.getActiveProfiles() == null || env.getActiveProfiles().length == 0) {
            System.out.println("not set spring profile!!");
        } else {
            for(String profile : env.getActiveProfiles()) {
                System.out.println("Spring profile active : " + profile);
            }
        }
    }

    @Bean(name="dataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        DateTime now = new DateTime(System.currentTimeMillis());
        slackSender.sendSlack(now.toString("yyyy-MM-dd hh:mm:ss") +" Crawler Shutdown", ALERT_LEVEL.DANGER);
    }
}


