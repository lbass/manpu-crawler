package com.manpu.crawler.worker.crawler.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class KakaoDetailDataRoot {

    @JsonProperty("singles")
    private List<KakaoWebtoonSingle> singles;

    public List<KakaoWebtoonSingle> getSingles() {
        return singles;
    }

    public void setSingles(List<KakaoWebtoonSingle> singles) {
        this.singles = singles;
    }

    @Override
    public String toString() {
        return "KakaoDetailDataRoot{" +
                "singles=" + singles +
                '}';
    }
}
