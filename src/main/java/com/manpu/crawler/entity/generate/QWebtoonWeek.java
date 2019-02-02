package com.manpu.crawler.entity.generate;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.manpu.crawler.entity.WebtoonWeek;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWebtoonWeek is a Querydsl query type for WebtoonWeek
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWebtoonWeek extends EntityPathBase<WebtoonWeek> {

    private static final long serialVersionUID = 1941549229L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWebtoonWeek webtoonWeek = new QWebtoonWeek("webtoonWeek");

    public final EnumPath<com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK> day = createEnum("day", com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK.class);

    public final NumberPath<Long> ranking = createNumber("ranking", Long.class);

    public final QWebtoon webtoon;

    public QWebtoonWeek(String variable) {
        this(WebtoonWeek.class, forVariable(variable), INITS);
    }

    public QWebtoonWeek(Path<? extends WebtoonWeek> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWebtoonWeek(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWebtoonWeek(PathMetadata metadata, PathInits inits) {
        this(WebtoonWeek.class, metadata, inits);
    }

    public QWebtoonWeek(Class<? extends WebtoonWeek> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.webtoon = inits.isInitialized("webtoon") ? new QWebtoon(forProperty("webtoon")) : null;
    }

}

