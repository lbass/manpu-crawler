package com.manpu.crawler.helper;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

public class JsoupHelper {

    public enum JSOUP_METHOD {
        GET,
        POST
    }

    public static Document connect(String url, JSOUP_METHOD method) throws IOException {
        Connection conn = Jsoup.connect(url).
                timeout(MANPU_CRAWLER_CONSTANT.REQUEST_TIMEOUT).
                ignoreContentType(true);
        Document result;
        if (method.equals(JSOUP_METHOD.GET)) {
            result = conn.get();
        } else {
            result = conn.post();
        }
        return result;
    }

    public static Document connectWithHaeder(String url,
            JSOUP_METHOD method, Map<String, String> headers) throws IOException {
        Connection conn = Jsoup.connect(url)
                .headers(headers)
                .timeout(MANPU_CRAWLER_CONSTANT.REQUEST_TIMEOUT)
                .ignoreContentType(true);
        Document result;
        if (method.equals(JSOUP_METHOD.GET)) {
            result = conn.get();
        } else {
            result = conn.post();
        }
        return result;
    }

    public static Document connectPostWithParam(String url, SimpleEntry<String, String>... params) throws IOException {
        Connection conn = Jsoup.connect(url).
                timeout(MANPU_CRAWLER_CONSTANT.REQUEST_TIMEOUT).
                ignoreContentType(true);
        for (SimpleEntry<String, String> param : params) {
            conn = conn.data(param.getKey(), param.getValue());
        }
        return conn.post();
    }
}
