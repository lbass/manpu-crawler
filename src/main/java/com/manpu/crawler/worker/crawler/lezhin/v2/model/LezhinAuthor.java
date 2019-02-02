package com.manpu.crawler.worker.crawler.lezhin.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LezhinAuthor {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
}
