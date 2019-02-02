package com.manpu.crawler.worker.crawler.daum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DaumResponse {

    @JsonProperty("result")
    private DaumResult result;

    @JsonProperty("data")
    private List<DaumWebtoonData> webtoonList;

    public DaumResult getResult() {
        return result;
    }

    public void setResult(DaumResult result) {
        this.result = result;
    }

    public List<DaumWebtoonData> getWebtoonList() {
        return webtoonList;
    }

    public void setWebtoonList(List<DaumWebtoonData> webtoonList) {
        this.webtoonList = webtoonList;
    }

    @Override
    public String toString() {
        return "DaumResponse{" +
                "result=" + result +
                ", webtoonList=" + webtoonList +
                '}';
    }
}
