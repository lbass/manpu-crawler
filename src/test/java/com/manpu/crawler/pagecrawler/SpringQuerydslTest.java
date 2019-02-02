package com.manpu.crawler.pagecrawler;

import com.manpu.crawler.pagecrawler.configuration.TestConfiguration;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@SpringBootTest(classes = {TestConfiguration.class})
public class SpringQuerydslTest {

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        // repository.save(new Genre("test1"));
        // repository.save(new Genre("test2"));
    }

}