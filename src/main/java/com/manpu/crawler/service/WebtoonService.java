package com.manpu.crawler.service;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.NAVER_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.ArtistDto;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.*;
import com.manpu.crawler.helper.DateHelper;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class WebtoonService {

    private Logger logger = LoggerFactory.getLogger(WebtoonService.class);

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private WebtoonArtistRepository webtoonArtistRepository;

    @Autowired
    private WebtoonWeekRepository webtoonWeekRepository;

    @Autowired
    private CrawlerMetric crawlerMetric;

    public List<Webtoon> getWebtoonInSerial() {
        return webtoonRepository.findInSerial();
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public List<WebtoonDto> updateWebtoonData(List<WebtoonDto> webtoonDtoList) {
        List<WebtoonDto> updateEpisodeWebtoonList = new ArrayList<>();
        for (WebtoonDto webtoonDto : webtoonDtoList) {
            crawlerMetric.addProcessCount();
            try {
                Webtoon webtoon = getWebtoonDtoToWebtoon(webtoonDto);
                if (webtoonRepository.findOne(webtoon.getId()) == null) {
                    webtoon = webtoonRepository.save(webtoon);

                    List<Artist> artistList = getWebtoonDtoToArtist(webtoonDto);
                    for (Artist artist : artistList) {
                        if (artistRepository.findOne(artist.getId()) == null) {
                            artistRepository.save(artist);
                        }
                        WebtoonArtist webtoonArtist = new WebtoonArtist();
                        webtoonArtist.setWebtoon(webtoon);
                        webtoonArtist.setArtist(artist);
                        webtoonArtistRepository.save(webtoonArtist);
                    }

                }
                Map<WEEK, Integer> workInfo = webtoonDto.getWeekInfo();
                for (Entry<WEEK, Integer> e : workInfo.entrySet()) {
                    WebtoonWeek webtoonWeek = new WebtoonWeek();
                    webtoonWeek.setWebtoon(webtoon);
                    webtoonWeek.setDay(e.getKey());
                    webtoonWeek.setRanking((long) e.getValue());
                    if (webtoonWeekRepository.findOne(webtoon, e.getKey()) == null) {
                        webtoonWeekRepository.save(webtoonWeek);
                    } else {
                        webtoonWeekRepository.update(webtoonWeek);
                        logger.debug("update week ranking data [{}] - {} {}"
                                , webtoonDto.getWebtoonTitle()
                                , webtoonWeek.getRanking()
                                , webtoonWeek.getDay()
                        );
                    }
                }
                /**
                 * TODO:: feature 수집은 이후 ...
                 */
                // episode data insert
                Episode episode = getWebtoonDtoToEpisode(webtoonDto);

                Episode selectedEpisode = episodeRepository.findOne(episode, webtoonDto.getEpisodeNumber());
                if (selectedEpisode == null) {
                    logger.debug("insert episode [{}] - {} {}"
                            , webtoonDto.getWebtoonTitle()
                            , webtoonDto.getEpisodeTitle()
                            , webtoonDto.getEpisodeNumber()
                    );

                    episodeRepository.save(episode);
                    // update된 웹툰 정보를 담는다.
                    updateEpisodeWebtoonList.add(webtoonDto);

                    crawlerMetric.addEpisodeInsertCount();
                    if(webtoonDto.getPayEpisodeNumbers() != null && webtoonDto.getPayEpisodeNumbers().size() > 0) {
                        episodeRepository.updatePayEpisode(webtoon.getId(), webtoonDto.getPayEpisodeNumbers());
                    }
                    webtoonRepository.updateLastEpisode(webtoon);
                }

                /**
                 * TODO:: PushServer 처리
                 */
            } catch (Exception e) {
                logger.error("work error: {}", webtoonDto);
                throw e;
            }
        }

        return updateEpisodeWebtoonList;
    }

    private Episode getWebtoonDtoToEpisode(WebtoonDto webtoonDto) {
        Episode episode = new Episode();
        Webtoon webtoon = webtoonRepository.findOne(webtoonDto.getSite() + "-" + webtoonDto.getCrawledId());
        episode.setWebtoon(webtoon);
        episode.setNum(webtoonDto.getEpisodeNumber());
        episode.setRating(webtoonDto.getEpisodeRating());
        episode.setUrl(webtoonDto.getEpisodeUrl());
        episode.setThumbnailUrl(webtoonDto.getEpisodeThumbnailUrl());
        episode.setTitle(webtoonDto.getEpisodeTitle());
        episode.setUploadAt(DateHelper.getDateStringToDateTime(
                webtoonDto.getEpisodeUpdateDate() + "235959", MANPU_CRAWLER_CONSTANT.DATETIME_FORMAT));
        return episode;
    }

    private List<Artist> getWebtoonDtoToArtist(final WebtoonDto webtoonDto) {
        List<ArtistDto> artistDtoList = webtoonDto.getArtistDtoList();
        return artistDtoList.stream().map(artistDto -> {
            Artist artist = new Artist();
            artist.setId(webtoonDto.getSite() + "-" + artistDto.getArtistId());
            artist.setCrawledArtistId(artistDto.getArtistId());
            artist.setArtistName(artistDto.getNickName());
            artist.setSite(webtoonDto.getSite());
            return artist;
        }).collect(Collectors.toList());
    }

    private Webtoon getWebtoonDtoToWebtoon(WebtoonDto webtoonDto) {
        Webtoon webtoon = new Webtoon();
        webtoon.setId(webtoonDto.getSite() + "-" + webtoonDto.getCrawledId());
        webtoon.setLastEpisodeNum(webtoonDto.getEpisodeNumber());
        webtoon.setLastUpdateAt(DateHelper.getDateStringToDateTime(
                webtoonDto.getEpisodeUpdateDate() + "235959", MANPU_CRAWLER_CONSTANT.DATETIME_FORMAT));
        webtoon.setRating(webtoonDto.getRating());
        webtoon.setCrawledId(webtoonDto.getCrawledId());
        webtoon.setSerialState(webtoonDto.getSerialState());
        webtoon.setSite(webtoonDto.getSite());
        webtoon.setWebtoonMainUrl(webtoonDto.getMainPageUrl());
        webtoon.setThumbnailUrl(webtoonDto.getThumbnailUrl());
        webtoon.setTitle(webtoonDto.getWebtoonTitle());

        return webtoon;
    }

    public List<WebtoonWeek> getWebtoonWeekInfo(Webtoon webtoonId) {
        return webtoonWeekRepository.findWeeKInfoByWebtoon(webtoonId);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public void updatBatcheWebtoonRaking(List<WebtoonDto> webtoonDtoList) {
        List<Webtoon> webtoonList =
                webtoonDtoList
                        .stream()
                        .map(w -> getWebtoonDtoToWebtoon(w))
                        .collect(Collectors.toList());
        webtoonRepository.updateBatchRakingInfo(webtoonList);
    }

    public List<WebtoonDto> getNaverAndDaumWebtoon() {
        List<Webtoon> webtoonList = webtoonRepository.findNaverAndDaumWebtoon();
        if(webtoonList == null ||webtoonList.size() == 0 ) {
            return null;
        }

        return webtoonList.stream().map(webtoon -> {
            WebtoonDto webtoonDto = new WebtoonDto();
            webtoonDto.setSite(webtoon.getSite());
            webtoonDto.setCrawledId(webtoon.getCrawledId());
            webtoonDto.setRating(webtoon.getRating());
            webtoonDto.setUrl(NAVER_CONSTANT.COMIC_DETAIL + webtoon.getCrawledId());
            return webtoonDto;
        }).collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public void insertEpisode(Episode episode) {
        episodeRepository.save(episode);
    }

    public Episode selectEpisode(Episode episode, int episodeNumber) {
        return episodeRepository.findOne(episode, episodeNumber);
    }

    public List<Webtoon> selectWebtoonListBySite(SITE site) {
        return webtoonRepository.findBySite(site);
    }
}
