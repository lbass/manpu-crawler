package com.manpu.crawler.worker.crawler.naver;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.NAVER_CONSTANT;
import com.manpu.crawler.dto.ArtistDto;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.WebtoonCrawler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class NaverWebtoonCrawler implements WebtoonCrawler {

    private Logger logger = LoggerFactory.getLogger(NaverWebtoonCrawler.class);

    @Autowired
    private CrawlerMetric crawlerMetric;

    @Override
    public WebtoonDto process(WebtoonDto webtoonDto) {
        final String url = webtoonDto.getUrl();
        logger.trace("collet url {}", url);
        Document document;
        try {
            document = JsoupHelper.connect(url, JSOUP_METHOD.GET);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        crawlerMetric.addNaverEpisodeWorkCount();
        try {
            Elements targetArea = document.select("table.viewList");
            Elements targetTbody = targetArea.select("tbody");
            Elements tables = targetTbody.select("tr");

            if(tables.size() == 0) {
                logger.error("webtoon table data is null");
                return null;
            }

            int trIndex = 0;

            // table 에 광고 배너가 있을 경우 무시한다.
            for(int i = 0 ; i < tables.size() ; i++) {
                String trClass = tables.get(i).attr("class");
                if(trClass != null && trClass.indexOf("band") != -1) {
                    continue;
                }
                trIndex = i;
                break;
            }

            Element firstRow = tables.get(trIndex);
            String rating = firstRow.select("div.rating_type").select("strong").first().html();
            String updateDate = firstRow.select("td.num").html();
            Element script = document.select("script").get(3);
            List<ArtistDto> artistDtoList = getArtist(script);

            webtoonDto.setArtistDtoList(artistDtoList);
            webtoonDto.setEpisodeRating(rating);
            webtoonDto.setEpisodeUpdateDate(StringUtils.replaceAll(updateDate, "[.]", ""));
            webtoonDto.setLastUpdateDt(StringUtils.replaceAll(updateDate, "[.]", ""));

            Element aTag = firstRow.select("a[href]").first();
            Element title = firstRow.select("td.title").select("a").first();

            String episodeUrl = aTag.attributes().get("href");
            if(episodeUrl != null && !episodeUrl.isEmpty()) {
                Element imgElement = aTag.select("img").first();
                webtoonDto.setEpisodeTitle(title.html());
                webtoonDto.setEpisodeThumbnailUrl(imgElement.attr("src"));
                webtoonDto.setEpisodeUrl(NAVER_CONSTANT.COMIC_HOST + episodeUrl);
                String[] queryString = StringUtils.split(episodeUrl, "?");
                if (queryString.length > 1) {
                    String parameter = queryString[1];
                    String[] pairs = StringUtils.split(parameter, "&");
                    for (String pair : pairs) {
                        final int idx = pair.indexOf("=");
                        if (idx != -1 && "no".equalsIgnoreCase(pair.substring(0, idx))) {
                            String episodeNum = pair.substring(idx + 1);
                            webtoonDto.setEpisodeNumber(Integer.parseInt(episodeNum));
                            break;
                        }
                    }
                }
            }

            logger.trace("data: {}", webtoonDto);
        } catch (Exception e) {
            logger.error("data: {}", webtoonDto);
            logger.error("process error ", e);
        }

        return webtoonDto;
    }

    public List<ArtistDto> getArtist(Element script) {
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
                    artistDto.setArtistId(StringUtils.trim(artistIds.get(i)));
                    artistDto.setNickName(StringUtils.trim(nickNames.get(i)));
                    artistDtos.add(artistDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return artistDtos;
    }
}
