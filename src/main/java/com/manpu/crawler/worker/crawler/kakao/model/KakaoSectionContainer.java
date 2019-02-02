package com.manpu.crawler.worker.crawler.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class KakaoSectionContainer {

    @JsonProperty("section_series")
    private List<KakaoSectionSeries> sectionSeries;

    public List<KakaoSectionSeries> getSectionSeries() {
        return sectionSeries;
    }

    public void setSectionSeries(List<KakaoSectionSeries> sectionSeries) {
        this.sectionSeries = sectionSeries;
    }

    @Override
    public String toString() {
        return "KakaoSectionContainer{" +
                "sectionSeries=" + sectionSeries +
                '}';
    }
}
