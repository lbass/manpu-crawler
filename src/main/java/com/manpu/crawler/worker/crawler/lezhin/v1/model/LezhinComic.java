package com.manpu.crawler.worker.crawler.lezhin.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class LezhinComic {

    @JsonProperty("id")
    private long id;

    @JsonProperty("display")
    private LezhinDisplay display;

    @JsonProperty("properties")
    private LezhinProperties properties;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("artists")
    private List<LezhinArtist> artists;

    @JsonProperty("state")
    private String state;

    @JsonProperty("updatedAt")
    private long updatedAt;

}
