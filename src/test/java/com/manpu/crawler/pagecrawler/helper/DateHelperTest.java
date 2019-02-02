package com.manpu.crawler.pagecrawler.helper;

import com.manpu.crawler.helper.DateHelper;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import java.time.LocalDateTime;

public class DateHelperTest {

    @Test
    public void testDate() {
        LocalDateTime result = DateHelper.getDateStringToDateTime("2018.10.06", "yyyy.MM.dd");
        System.out.println(result);
    }

    @Test
    public void testDate2() {
        System.out.println(new DateTime(1533818870662L));
        System.out.println(new DateTime(1539961200000L));
        System.out.println(new DateTime(1529679600000L));
    }

    @Test
    public void testDate3() {
        System.out.println(StringUtils.replaceAll("2018.10.06", "[.]", ""));
    }
}
