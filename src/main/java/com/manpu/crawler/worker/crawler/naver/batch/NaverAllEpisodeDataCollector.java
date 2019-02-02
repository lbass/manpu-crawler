package com.manpu.crawler.worker.crawler.naver.batch;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.NAVER_CONSTANT;
import com.manpu.crawler.dto.EpisodeDto;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class NaverAllEpisodeDataCollector {
    private Logger logger = LoggerFactory.getLogger(NaverAllEpisodeDataCollector.class);

    public List<EpisodeDto> work(String crawledId, String pageIndex) {
        final String url = NAVER_CONSTANT.COMIC_DETAIL + crawledId + "&page=" + pageIndex;
        logger.debug("collet url {}", url);
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.error("error {}, {}", crawledId, pageIndex);
            return null;
        }

        Element currentPage = document.select("strong.page").first();
        if(currentPage != null) {
            String currentPageNum = currentPage.child(0).html();
            if(!pageIndex.equalsIgnoreCase(StringUtils.trim(currentPageNum))) {
               throw new InvalidPageException("Invalid Page Index");
            }
        }

        Elements targetArea = document.select("tbody");

        Elements episodeElements = targetArea.select("tr");
        List<EpisodeDto> episodeDtoList =  episodeElements.stream().map(element -> {
            EpisodeDto episodeDto = new EpisodeDto();
            if(StringUtils.indexOf(element.attr("class"),"band_banner") > -1) {
                logger.trace("pass");
                return null;
            }
            String rating = element.select("div.rating_type").select("strong").first().html();
            String updateDate = element.select("td.num").html();

            episodeDto.setRating(rating);
            episodeDto.setUpdateDate(updateDate);
            Element aTag = element.select("a[href]").first();
            Element title = aTag.child(0);

            String episodeUrl = aTag.attributes().get("href");
            episodeDto.setEpisodeTitle(title.attr("title"));
            episodeDto.setEpisodeThumbnailUrl(title.attr("src"));

            if(episodeUrl != null && !episodeUrl.isEmpty()) {
                episodeDto.setUrl(NAVER_CONSTANT.COMIC_HOST + episodeUrl);
                String[] queryString = StringUtils.split(episodeUrl, "?");
                if (queryString.length > 1) {
                    String parameter = queryString[1];
                    String[] pairs = StringUtils.split(parameter, "&");
                    for (String pair : pairs) {
                        final int idx = pair.indexOf("=");
                        if (idx != -1 && "no".equalsIgnoreCase(pair.substring(0, idx))) {
                            String episodeNum = pair.substring(idx + 1);
                            episodeDto.setEpisodeNumber(Integer.parseInt(episodeNum));
                            break;
                        }
                    }
                }
            }
            return episodeDto;
        }).collect(Collectors.toList());

        logger.debug("data: {}", episodeDtoList);
        return episodeDtoList;
    }

    public static class InvalidPageException extends RuntimeException {
        public InvalidPageException(String invalid_page_index) {
        }
    }
}
