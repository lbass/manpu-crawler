package com.manpu.crawler.worker.crawler.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class KakaoListDataRoot {

    @JsonProperty("section_containers")
    private List<KakaoSectionContainer> sectionContainers;

    @JsonProperty("result_code")
    private int resultCode;

    public List<KakaoSectionContainer> getSectionContainers() {
        return sectionContainers;
    }

    public void setSectionContainers(List<KakaoSectionContainer> sectionContainers) {
        this.sectionContainers = sectionContainers;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "KakaoListDataRoot{" +
                "sectionContainers=" + sectionContainers +
                '}';
    }
}
