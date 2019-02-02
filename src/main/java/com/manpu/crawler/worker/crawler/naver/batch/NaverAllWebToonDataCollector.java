package com.manpu.crawler.worker.crawler.naver.batch;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.NAVER_CONSTANT;
import com.manpu.crawler.dto.ArtistDto;
import com.manpu.crawler.dto.WebtoonDto;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NaverAllWebToonDataCollector {
    private Logger logger = LoggerFactory.getLogger(NaverAllWebToonDataCollector.class);

    public List<WebtoonDto> work(final String genreCode) {
        final String url = NAVER_CONSTANT.COMIC_HOST + NAVER_CONSTANT.MAIN_URL + "&genreDto=" + genreCode;
        logger.debug("collet url {}", url);
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Element targetArea = document.select("ul.img_list").first();

        List<WebtoonDto> webtoonDtos = targetArea.children().stream().map(child -> {
            WebtoonDto webtoonDto = new WebtoonDto();
            Elements imgElement = child.select("img");
            if(imgElement.size() > 1) {
                Elements finishElement = child.select("img").last().select("finish");
                if (finishElement != null && finishElement.size() > 0) {
                    return null;
                }
            }

            webtoonDto.setThumbnailUrl(imgElement.attr("src"));
            Element titleElement = child.select("dt").first().child(0);

            String title = titleElement.attributes().get("title");
            webtoonDto.setWebtoonTitle(title);

            String targetUrl = titleElement.attributes().get("href");
            webtoonDto.setUrl(targetUrl);
            String[] queryString = StringUtils.split(targetUrl, "?");
            if(queryString.length > 1) {
                String parameter = queryString[1];
                String[] pairs = StringUtils.split(parameter, "&");
                for (String pair : pairs) {
                    final int idx = pair.indexOf("=");
                    if(idx != -1 && "titleId".equalsIgnoreCase(pair.substring(0, idx))) {
                        String webToonId = pair.substring(idx + 1);
                        webtoonDto.setCrawledId(webToonId);
                        break;
                    }
                }
            }
            webtoonDto.setRating(child.select("div.rating_type").select("strong").first().html());
            webtoonDto.setLastUpdateDt(child.select("dd.date2").first().html());

            List<ArtistDto> artistDtos = getArtist(targetUrl);
            webtoonDto.setArtistDtoList(artistDtos);
            webtoonDto.setGenre(genreCode);
            logger.debug("data: {}", webtoonDto);
            return webtoonDto;
        }).collect(Collectors.toList());

        return webtoonDtos;
    }

    public List<ArtistDto> getArtist(String targetUrl) {
        final String url = NAVER_CONSTANT.COMIC_HOST + targetUrl;
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element script = document.select("script").get(3);
        BufferedReader reader = new BufferedReader(new StringReader(script.html()));
        String line;
        List<ArtistDto> artistDtos = new ArrayList<>();
        try {
            List<String> artistIds = new ArrayList<>();
            List<String> nickNames = new ArrayList<>();
            while((line = reader.readLine()) != null) {
                if(line.indexOf("\"artistId\"") != -1) {
                    String artistId = StringUtils.replace(line, "\"artistId\"", "");
                    artistId = artistId.replaceAll("[\'\"\\{\\},.:]", "");
                    artistId = StringUtils.trim(artistId);
                    artistIds.add(artistId);
                }
                if(line.indexOf("\"nickname\"") != -1) {
                    String nickName = StringUtils.replace(line, "\"nickname\"", "");
                    nickName = nickName.replaceAll("[\'\"\\{\\},.:]", "");
                    nickName = StringUtils.trim(nickName);
                    nickNames.add(nickName);
                }
            }

            if(artistIds.size() > 0 && (artistIds.size() == nickNames.size())) {
                for(int i = 0 ; i < artistIds.size() ; i++) {
                    ArtistDto artistDto = new ArtistDto();
                    artistDto.setArtistId(artistIds.get(i));
                    artistDto.setNickName(nickNames.get(i));
                    artistDtos.add(artistDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return artistDtos;
    }
}