package com.manpu.crawler.pagecrawler.naver;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

public class JsoupExceptionTest {

    @Test
    public void testJsoup() {
        final String url = "https://comic.naver.com/webtoon/list.nhn?titleId=651673&page=999";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
