package com.manpu.crawler.worker.crawler.daum.batch;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.DAUM_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.DAUM_SERVICE_TYPE;
import com.manpu.crawler.dto.EpisodeDto;
import com.manpu.crawler.helper.JacksonJsonHelper;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.worker.crawler.daum.model.DaumDetailResponse;
import com.manpu.crawler.worker.crawler.daum.model.DaumEpisode;
import com.manpu.crawler.worker.crawler.daum.model.DaumWebtoonData;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class DaumAllEpisodeDataCollector {

    private Logger logger = LoggerFactory.getLogger(DaumAllEpisodeDataCollector.class);

    public List<EpisodeDto> work(String crawledId) {
        final String url = String.format(DAUM_CONSTANT.COMIC_HOST + DAUM_CONSTANT.DETAIL_API_URL,
                crawledId, System.currentTimeMillis());

        logger.info("collet url {}", url);
        Document document;
        try {
            document = JsoupHelper.connect(url, JSOUP_METHOD.GET);
        } catch (SocketTimeoutException e) {
            logger.error("Daum timeout error: {}", url);
            return null;
        } catch (IOException e) {
            logger.error("Daum io error error: {}", url);
            return null;
        }

        List<EpisodeDto> episodeDtoList = new ArrayList<>();

        DaumDetailResponse daumResponse = JacksonJsonHelper.convertToObject(DaumDetailResponse.class, document.body().html());
        logger.trace("daum res data {}", daumResponse.toString());
        if(daumResponse.getData() != null) {
            DaumWebtoonData webtoonData = daumResponse.getData().getWebtoon();
            List<DaumEpisode> episodes = webtoonData.getWebtoonEpisodes();
            for(int i = 0 ; i < episodes.size() ; i++) {
                EpisodeDto episodeDto = new EpisodeDto();
                DaumEpisode episode = episodes.get(i);
                if (!DAUM_SERVICE_TYPE.free.name().equalsIgnoreCase(episode.getServiceType())) {
                    episodeDto.setFree(false);
                }
                DaumEpisode daumEpisode = episodes.get(i);
                episodeDto.setUpdateDate(daumEpisode.getDateCreated().substring(0, 8));
                episodeDto.setUrl(DAUM_CONSTANT.VIEWER_PATH + String.valueOf(daumEpisode.getEpisodeId()));
                episodeDto.setRating("");
                episodeDto.setEpisodeNumber(daumEpisode.getEpisodeNumber());
                episodeDto.setEpisodeThumbnailUrl(daumEpisode.getEpisodeThumbnailImage().getImageUrl());
                episodeDto.setEpisodeTitle(daumEpisode.getTitle());

                episodeDtoList.add(episodeDto);
            }

        } else {
            logger.error("Daum detail crawling is null");
        }

        return episodeDtoList;
    }
}
