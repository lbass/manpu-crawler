package com.manpu.crawler.worker.crawler;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.DAUM_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.LEZHIN_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.NAVER_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;

public class WebtoonUrlGenerator {

    public static String getWebtoonMainPageUrl(SITE site, String webtoonKey) {
        String webtoonMainUrl;
        if(site.equals(SITE.NAVER)) {
            webtoonMainUrl = NAVER_CONSTANT.COMIC_HOST + webtoonKey;
        } else if(site.equals(SITE.DAUM)) {
            webtoonMainUrl = DAUM_CONSTANT.COMIC_HOST + DAUM_CONSTANT.DETAIL_PATH + webtoonKey;
        } else if(site.equals(SITE.LEZHIN)) {
            webtoonMainUrl = LEZHIN_CONSTANT.COMIC_HOST + LEZHIN_CONSTANT.V1.DETAIL_PATH + webtoonKey;
        } else {
            webtoonMainUrl = null;
        }
        return webtoonMainUrl;
    }

    public static String getWebtoonDataUrl(SITE site, String webtoonKey) {
        String webtoonMainUrl;
        if(site.equals(SITE.NAVER)) {
            webtoonMainUrl = NAVER_CONSTANT.COMIC_HOST + webtoonKey;
        } else if(site.equals(SITE.DAUM)) {
            webtoonMainUrl = String.format(DAUM_CONSTANT.COMIC_HOST + DAUM_CONSTANT.DETAIL_API_URL,
                    webtoonKey, System.currentTimeMillis());
        } else if(site.equals(SITE.LEZHIN)) {
            webtoonMainUrl = LEZHIN_CONSTANT.COMIC_HOST + LEZHIN_CONSTANT.V1.DETAIL_PATH + webtoonKey;
        } else {
            webtoonMainUrl = null;
        }
        return webtoonMainUrl;
    }
}
