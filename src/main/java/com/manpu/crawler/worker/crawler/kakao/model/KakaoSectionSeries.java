package com.manpu.crawler.worker.crawler.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class KakaoSectionSeries {

    @JsonProperty("list")
    private List<KakaoWebtoon> list;

    public List<KakaoWebtoon> getList() {
        return list;
    }

    public void setList(List<KakaoWebtoon> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "KakaoSectionSeries{" +
                "list=" + list +
                '}';
    }
}
