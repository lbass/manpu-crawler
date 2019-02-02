package com.manpu.crawler.worker.crawler.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoWebtoon {
    @JsonProperty("age_grade")
    private int ageGrade;
    @JsonProperty("author")
    private String author;
    @JsonProperty("caption")
    private String caption;
    @JsonProperty("image")
    private String image;
    @JsonProperty("land_thumb_img")
    private String landThumbImg;
    @JsonProperty("last_slide_added_date")
    private String lastSlideAddedDate;
    @JsonProperty("read_count")
    private long readCount;
    @JsonProperty("series_id")
    private long seriesId;
    @JsonProperty("thumb_img")
    private String thumbImg;
    @JsonProperty("title")
    private String title;
    @JsonProperty("uid")
    private long uid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAgeGrade() {
        return ageGrade;
    }

    public void setAgeGrade(int ageGrade) {
        this.ageGrade = ageGrade;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLandThumbImg() {
        return landThumbImg;
    }

    public void setLandThumbImg(String landThumbImg) {
        this.landThumbImg = landThumbImg;
    }

    public String getLastSlideAddedDate() {
        return lastSlideAddedDate;
    }

    public void setLastSlideAddedDate(String lastSlideAddedDate) {
        this.lastSlideAddedDate = lastSlideAddedDate;
    }

    public long getReadCount() {
        return readCount;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    public long getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(long seriesId) {
        this.seriesId = seriesId;
    }

    public String getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "KakaoWebtoon{" +
                "ageGrade=" + ageGrade +
                ", author='" + author + '\'' +
                ", caption='" + caption + '\'' +
                ", image='" + image + '\'' +
                ", landThumbImg='" + landThumbImg + '\'' +
                ", lastSlideAddedDate='" + lastSlideAddedDate + '\'' +
                ", readCount=" + readCount +
                ", seriesId=" + seriesId +
                ", thumbImg='" + thumbImg + '\'' +
                ", title='" + title + '\'' +
                ", uid=" + uid +
                '}';
    }
}
