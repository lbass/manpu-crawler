package com.manpu.crawler.worker.crawler.daum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class DaumArtist {

    @JsonProperty("id")
    private long artistId;

    @JsonProperty("name")
    private String name;

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DaumArtist{" +
                "artistId=" + artistId +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DaumArtist artist = (DaumArtist) o;
        return artistId == artist.artistId &&
                Objects.equals(name, artist.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(artistId, name);
    }
}
