package com.manpu.crawler.worker.crawler.daum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DaumWebtoonData {

    @JsonProperty("id")
    private long webtoonId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("pcThumbnailImage")
    private DaumThumbnailImage thumbnailImage;

    @JsonProperty("ageGrade")
    private int ageGrade;

    /**
     * 휴재 여부
     */
    @JsonProperty("restYn")
    private String restYn;


    @JsonProperty("cartoon")
    private DaumCartoon cartoon;

    @JsonProperty("latestWebtoonEpisode")
    private DaumEpisode lastEpisode;

    @JsonProperty("averageScore")
    private Double averageScore;

    @JsonProperty("webtoonEpisodes")
    private List<DaumEpisode> webtoonEpisodes;

    public long getWebtoonId() {
        return webtoonId;
    }

    public void setWebtoonId(long webtoonId) {
        this.webtoonId = webtoonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DaumThumbnailImage getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(DaumThumbnailImage thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public int getAgeGrade() {
        return ageGrade;
    }

    public void setAgeGrade(int ageGrade) {
        this.ageGrade = ageGrade;
    }

    public String getRestYn() {
        return restYn;
    }

    public void setRestYn(String restYn) {
        this.restYn = restYn;
    }

    public DaumCartoon getCartoon() {
        return cartoon;
    }

    public void setCartoon(DaumCartoon cartoon) {
        this.cartoon = cartoon;
    }

    public DaumEpisode getLastEpisode() {
        return lastEpisode;
    }

    public void setLastEpisode(DaumEpisode lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<DaumEpisode> getWebtoonEpisodes() {
        return webtoonEpisodes;
    }

    public void setWebtoonEpisodes(List<DaumEpisode> webtoonEpisodes) {
        this.webtoonEpisodes = webtoonEpisodes;
    }

    @Override
    public String toString() {
        return "DaumWebtoonData{" +
                "webtoonId=" + webtoonId +
                ", title='" + title + '\'' +
                ", nickname='" + nickname + '\'' +
                ", thumbnailImage=" + thumbnailImage +
                ", ageGrade=" + ageGrade +
                ", restYn='" + restYn + '\'' +
                ", cartoon=" + cartoon +
                ", lastEpisode=" + lastEpisode +
                ", averageScore=" + averageScore +
                "} + \n";
    }
}
