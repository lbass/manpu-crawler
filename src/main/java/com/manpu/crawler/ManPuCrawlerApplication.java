package com.manpu.crawler;


import com.manpu.crawler.config.ManPuCrawlerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ManPuCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ManPuCrawlerConfiguration.class);
        app.setRegisterShutdownHook(true);
        app.run(args);
    }
}
