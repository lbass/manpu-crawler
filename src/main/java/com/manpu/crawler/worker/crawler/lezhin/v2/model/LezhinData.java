package com.manpu.crawler.worker.crawler.lezhin.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LezhinData {

    @JsonProperty("inventoryList")
    private List<LezhinInventory> inventoryList;
}
