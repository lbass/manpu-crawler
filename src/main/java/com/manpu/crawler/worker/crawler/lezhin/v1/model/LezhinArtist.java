package com.manpu.crawler.worker.crawler.lezhin.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class LezhinArtist {

    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private String id;

}
