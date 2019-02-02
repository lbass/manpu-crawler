package com.manpu.crawler.worker.crawler.lezhin.v2.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LezhinRoot {

    @JsonProperty("data")
    private LezhinData data;



}
