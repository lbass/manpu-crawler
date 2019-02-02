package com.manpu.crawler.worker.crawler.naver.batch;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.NAVER_CONSTANT;
import com.manpu.crawler.dto.GenreDto;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class NaverGenreCollectWorker {

    private Logger logger = LoggerFactory.getLogger(NaverGenreCollectWorker.class);

    @Transactional(TxType.REQUIRES_NEW)
    public List<GenreDto> work() {
        final String url = NAVER_CONSTANT.COMIC_HOST + NAVER_CONSTANT.MAIN_URL;
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements targetArea = document.select("ul.spot");
        Elements linksOnPage = targetArea.select("a[href]");

        List<GenreDto> genreDtos = linksOnPage.stream().map(element -> {
            GenreDto genreDto = new GenreDto();
            logger.trace("data: {}", element);

            String targetUrl = element.attributes().get("href");
            String[] queryString = StringUtils.split(targetUrl, "?");
            if (queryString.length > 1) {
                String parameter = queryString[1];
                String[] pairs = StringUtils.split(parameter, "&");
                for (String pair : pairs) {
                    final int idx = pair.indexOf("=");
                    if (idx != -1 && "genre".equalsIgnoreCase(pair.substring(0, idx))) {
                        String genreCode = pair.substring(idx + 1);
                        genreDto.setGenreCode(genreCode);
                        break;
                    }
                }
            }
            genreDto.setGenreName(element.html());
            logger.debug("data: {}", genreDto);
            return genreDto;
        }).collect(Collectors.toList());

        return genreDtos;
    }
}
