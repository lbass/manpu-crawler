package com.manpu.crawler.entity.generate;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.manpu.crawler.entity.Artist;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QArtist is a Querydsl query type for Artist
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QArtist extends EntityPathBase<Artist> {

    private static final long serialVersionUID = -1110341700L;

    public static final QArtist artist = new QArtist("artist");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath artistName = createString("artistName");

    public final StringPath crawledArtistId = createString("crawledArtistId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath id = createString("id");

    public final StringPath site = createString("site");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QArtist(String variable) {
        super(Artist.class, forVariable(variable));
    }

    public QArtist(Path<? extends Artist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QArtist(PathMetadata metadata) {
        super(Artist.class, metadata);
    }

}

