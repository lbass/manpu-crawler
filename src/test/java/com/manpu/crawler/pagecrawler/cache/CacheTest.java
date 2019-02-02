package com.manpu.crawler.pagecrawler.cache;

import com.manpu.crawler.cache.ManpuCacheMap;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.pagecrawler.configuration.TestConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@SpringBootTest(classes = {TestConfiguration.class})
public class CacheTest {
    @Autowired
    private ManpuCacheMap cacheMap;

    @Test
    public void testCache() {
        cacheMap.collectWebtoonCache();
        Assert.assertTrue(cacheMap.getMapCount(SITE.NAVER.name()) > 0);
    }
}
