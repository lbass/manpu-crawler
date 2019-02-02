package com.manpu.crawler.worker.crawler.lezhin.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LezhinItem {
    @JsonProperty("alias")
    private String alias;

    @JsonProperty("authors")
    private List<LezhinAuthor> authors;

    @JsonProperty("idLezhinObject")
    private long idLezhinObject;

    @JsonProperty("targetUrl")
    private String targetUrl;

    @JsonProperty("title")
    private String title;

    @JsonProperty("mediaList")
    private List<LezhinMedia> mediaList;

}
