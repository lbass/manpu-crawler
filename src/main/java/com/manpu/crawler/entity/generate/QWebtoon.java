package com.manpu.crawler.entity.generate;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.manpu.crawler.entity.Webtoon;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWebtoon is a Querydsl query type for Webtoon
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWebtoon extends EntityPathBase<Webtoon> {

    private static final long serialVersionUID = 1895878937L;

    public static final QWebtoon webtoon = new QWebtoon("webtoon");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath crawledId = createString("crawledId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath id = createString("id");

    public final NumberPath<Long> lastEpisodeNum = createNumber("lastEpisodeNum", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdateAt = createDateTime("lastUpdateAt", java.time.LocalDateTime.class);

    public final StringPath rating = createString("rating");

    public final EnumPath<Webtoon.SerialState> serialState = createEnum("serialState", Webtoon.SerialState.class);

    public final StringPath site = createString("site");

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath webtoonMainUrl = createString("webtoonMainUrl");

    public QWebtoon(String variable) {
        super(Webtoon.class, forVariable(variable));
    }

    public QWebtoon(Path<? extends Webtoon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWebtoon(PathMetadata metadata) {
        super(Webtoon.class, metadata);
    }

}

