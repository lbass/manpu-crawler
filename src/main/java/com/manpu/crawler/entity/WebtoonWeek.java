package com.manpu.crawler.entity;


import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@Entity
public class WebtoonWeek  implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name="webtoon_id", columnDefinition = "VARCHAR(50)")
    private Webtoon webtoon;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private WEEK day;

    @Column(columnDefinition = "int(4)")
    private Long ranking;

}
