package com.manpu.crawler.worker.crawler.lezhin.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class LezhinEpisode {

    @JsonProperty("id")
    private long id;
    @JsonProperty("updatedAt")
    private long updatedAt;
    @JsonProperty("freedAt")
    private Long freedAt;
    @JsonProperty("publishedAt")
    private long publishedAt;
    @JsonProperty("name")
    private String name;
    @JsonProperty("display")
    private LezhinDisplay display;
    @JsonProperty("seq")
    private int seq;

}
