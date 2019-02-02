package com.manpu.crawler.service;


import com.manpu.crawler.pagecrawler.configuration.TestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
@SpringBootTest(classes = {WebtoonService.class})
public class WebServiceTest {

}
