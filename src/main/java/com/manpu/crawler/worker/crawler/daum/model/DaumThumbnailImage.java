package com.manpu.crawler.worker.crawler.daum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DaumThumbnailImage {

    @JsonProperty("url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "DaumThumbnailImage{" +
                "imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
