package com.manpu.crawler.worker.crawler.daum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DaumEpisode {

    @JsonProperty("id")
    private long episodeId;

    @JsonProperty("episode")
    private int episodeNumber;

    @JsonProperty("url")
    private String episodeUrl;

    @JsonProperty("thumbnailImage")
    private DaumThumbnailImage episodeThumbnailImage;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("title")
    private String title;

    @JsonProperty("price")
    private int price;

    @JsonProperty("serviceType")
    private String serviceType;


    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getEpisodeUrl() {
        return episodeUrl;
    }

    public void setEpisodeUrl(String episodeUrl) {
        this.episodeUrl = episodeUrl;
    }

    public DaumThumbnailImage getEpisodeThumbnailImage() {
        return episodeThumbnailImage;
    }

    public void setEpisodeThumbnailImage(DaumThumbnailImage episodeThumbnailImage) {
        this.episodeThumbnailImage = episodeThumbnailImage;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(long episodeId) {
        this.episodeId = episodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
