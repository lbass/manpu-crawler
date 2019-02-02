package com.manpu.crawler.worker.crawler.lezhin.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LezhinRankingDataRoot {

    @JsonProperty("data")
    private List<LezhinRankingData> data;

    @Data
    public static class LezhinRankingData {
        @JsonProperty("id")
        private Long id;
    }

}
