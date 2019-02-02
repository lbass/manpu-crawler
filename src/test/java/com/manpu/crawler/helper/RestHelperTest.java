package com.manpu.crawler.helper;


import com.manpu.crawler.config.ManpuApiProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@EnableConfigurationProperties
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ManpuApiProperties.class, RestHelper.class})
public class RestHelperTest {

    private Logger logger = LoggerFactory.getLogger(RestHelperTest.class);

    @Autowired
    private ManpuApiProperties properties;

    @Autowired
    private RestHelper restHelper;

    @Test
    public void test() {
        logger.info("{}", properties);
        restHelper.pushWebtoonInfomation(
                "LEZHIN-revatoon"
                , "LEZHIN"
                , "[LEZHIN] 레바툰 업데이트"
                , "랜덤능력사탕 中편");

        restHelper.pushWebtoonInfomation(
                "LEZHIN-revatoon"
                , "NAVER"
                , "[NAVER] 소녀 연대기"
                , "6화");

        restHelper.pushWebtoonInfomation(
                "LEZHIN-revatoon"
                , "NAVER"
                , "[NAVER] 삼국지톡 업데이트"
                , "55화: 충신, 조조");
    }
}
