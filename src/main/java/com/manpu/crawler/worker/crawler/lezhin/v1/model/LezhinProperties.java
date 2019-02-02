package com.manpu.crawler.worker.crawler.lezhin.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class LezhinProperties {

    @JsonProperty("days")
    private List<String> days;
}
