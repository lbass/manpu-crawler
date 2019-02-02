package com.manpu.crawler.worker.crawler.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoWebtoonSingle {
    @JsonProperty("id")
    private long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("order_value")
    private int orderValue;
    @JsonProperty("land_thumbnail_url")
    private String landThumbnailUrl;
    @JsonProperty("free_change_dt")
    private String freeChangeDt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public String getLandThumbnailUrl() {
        return landThumbnailUrl;
    }

    public void setLandThumbnailUrl(String landThumbnailUrl) {
        this.landThumbnailUrl = landThumbnailUrl;
    }

    public String getFreeChangeDt() {
        return freeChangeDt;
    }

    public void setFreeChangeDt(String freeChangeDt) {
        this.freeChangeDt = freeChangeDt;
    }

    @Override
    public String toString() {
        return "KakaoWebtoonSingle{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", orderValue=" + orderValue +
                ", landThumbnailUrl='" + landThumbnailUrl + '\'' +
                ", freeChangeDt='" + freeChangeDt + '\'' +
                '}';
    }
}
