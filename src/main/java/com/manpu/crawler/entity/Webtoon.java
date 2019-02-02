package com.manpu.crawler.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString
@Entity
public class Webtoon extends AbstractEntity {

    public enum SerialState {
        ACTIVE,
        INACTIVE,
        DORMANT
    }

    @Id
    @Column(length = 50)
    private String id;
    @Column(length = 50, nullable = false)
    private String site;
    @Column(length = 50, nullable = false)
    private String crawledId;
    @Column(length = 50, nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "INT(4) UNSIGNED")
    private long lastEpisodeNum;

    @Column
    private LocalDateTime lastUpdateAt;

    @Enumerated(EnumType.STRING)
    @Column
    private SerialState serialState;

    @Column(length = 300)
    private String thumbnailUrl;

    @Column(length = 10)
    private String rating;

    @Column(length = 300, nullable = false)
    private String webtoonMainUrl;

    public Webtoon() {

    }
}
