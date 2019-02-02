package com.manpu.crawler.entity.generate;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.manpu.crawler.entity.WebtoonArtist;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWebtoonArtist is a Querydsl query type for WebtoonArtist
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWebtoonArtist extends EntityPathBase<WebtoonArtist> {

    private static final long serialVersionUID = 1195615680L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWebtoonArtist webtoonArtist = new QWebtoonArtist("webtoonArtist");

    public final QArtist artist;

    public final QWebtoon webtoon;

    public QWebtoonArtist(String variable) {
        this(WebtoonArtist.class, forVariable(variable), INITS);
    }

    public QWebtoonArtist(Path<? extends WebtoonArtist> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWebtoonArtist(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWebtoonArtist(PathMetadata metadata, PathInits inits) {
        this(WebtoonArtist.class, metadata, inits);
    }

    public QWebtoonArtist(Class<? extends WebtoonArtist> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.artist = inits.isInitialized("artist") ? new QArtist(forProperty("artist")) : null;
        this.webtoon = inits.isInitialized("webtoon") ? new QWebtoon(forProperty("webtoon")) : null;
    }

}

