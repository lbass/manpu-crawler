package com.manpu.crawler.worker.crawler.daum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DaumDetailResponse {

    @JsonProperty("result")
    private DaumResult result;

    private DaumDetailWebtoon data;
}
