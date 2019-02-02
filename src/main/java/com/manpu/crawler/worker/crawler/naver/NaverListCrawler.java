package com.manpu.crawler.worker.crawler.naver;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.NAVER_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.Webtoon.SerialState;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.worker.crawler.WebtoonUrlGenerator;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class NaverListCrawler {

    private Logger logger = LoggerFactory.getLogger(NaverListCrawler.class);

    public List<WebtoonDto> work(WEEK week) {
        final String url = NAVER_CONSTANT.COMIC_HOST + NAVER_CONSTANT.MAIN_URL + "?week=" + week.name();
        Document document = null;
        try {
            document = JsoupHelper.connect(url, JSOUP_METHOD.GET);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Elements targetArea = document.select("ul.img_list");
        if(targetArea == null) {
            logger.error("list table data is null");
            return null;
        }

        final AtomicInteger counter = new AtomicInteger(0);
        List<WebtoonDto> webtoonDtos = targetArea.first().children().stream().map(child -> {
            WebtoonDto webtoonDto = new WebtoonDto();

            Map<WEEK, Integer> weekInfo = new HashMap<>();
            weekInfo.put(week, counter.incrementAndGet());
            webtoonDto.setWeekInfo(weekInfo);

            webtoonDto.setSite(SITE.NAVER.name());
            Element imgElement = child.select("img").first();
            Elements breakElement = child.select("em.ico_break");
            if(breakElement != null && breakElement.first() != null) {
                webtoonDto.setSerialState(SerialState.DORMANT);
            } else {
                webtoonDto.setSerialState(SerialState.ACTIVE);
            }

            webtoonDto.setThumbnailUrl(imgElement.attr("src"));
            Element titleElement = child.select("dt").first().child(0);
            titleElement.attributes().asList().stream().forEach(e -> {
                if("href".equalsIgnoreCase(e.getKey())) {
                    webtoonDto.setUrl(WebtoonUrlGenerator.getWebtoonMainPageUrl(SITE.NAVER, e.getValue()));
                    webtoonDto.setMainPageUrl(WebtoonUrlGenerator.getWebtoonDataUrl(SITE.NAVER, e.getValue()));
                    String[] queryString = StringUtils.split(e.getValue(), "?");
                    if(queryString.length > 1) {
                        String parameter = queryString[1];
                        String[] pairs = StringUtils.split(parameter, "&");
                        for (String pair : pairs) {
                            final int idx = pair.indexOf("=");
                            if(idx != -1 && "titleId".equalsIgnoreCase(pair.substring(0, idx))) {
                                String webtoonId = pair.substring(idx + 1);
                                webtoonDto.setCrawledId(webtoonId);
                                break;
                            }
                        }
                    }
                }
                if("title".equalsIgnoreCase(e.getKey())) {
                    webtoonDto.setWebtoonTitle(e.getValue());
                }
            });

            webtoonDto.setRating(child.select("div.rating_type").select("strong").first().html());
            // webtoonDto.setLastUpdateAt(child.select("dd.date2").first().html());
            return webtoonDto;

        }).collect(Collectors.toList());

        logger.trace("data: {}", webtoonDtos);
        return webtoonDtos;
    }
}
