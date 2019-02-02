package com.manpu.crawler.worker.crawler.daum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DaumCartoon {

    @JsonProperty("artists")
    private List<DaumArtist> artists;

    public List<DaumArtist> getArtists() {
        return artists;
    }

    public void setArtists(List<DaumArtist> artists) {
        this.artists = artists;
    }

    @Override
    public String toString() {
        return "DaumCartoon{" +
                "artists=" + artists +
                '}';
    }
}
