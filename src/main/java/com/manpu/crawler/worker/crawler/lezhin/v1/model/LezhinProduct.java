package com.manpu.crawler.worker.crawler.lezhin.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class LezhinProduct {

    @JsonProperty("id")
    private long id;

    @JsonProperty("episodes")
    private List<LezhinEpisode> episodes;

    @JsonProperty("publishedEpisodeSize")
    private int publishedEpisodeSize = -1;

    @JsonProperty("freedEpisodeSize")
    private int freedEpisodeSize = -1;

}
