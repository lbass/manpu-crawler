package com.manpu.crawler.dto;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.entity.Webtoon.SerialState;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class WebtoonDto {
    private String site;
    private String webtoonTitle;
    private String url;
    private String mainPageUrl;
    private String crawledId;

    private String genre;
    private String thumbnailUrl;
    private String rating;
    private String lastUpdateDt;
    private SerialState serialState;

    private String episodeUrl;
    private int episodeNumber;
    private String episodeRating;
    private String episodeUpdateDate;
    private String episodeThumbnailUrl;
    private String episodeTitle;
    private List<ArtistDto> artistDtoList;

    private Map<WEEK, Integer> weekInfo;

    private List<Long> payEpisodeNumbers;

}
