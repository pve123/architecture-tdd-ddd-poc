package com.example.demo.board.adapter.out.persistence;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardJpaEntity is a Querydsl query type for BoardJpaEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardJpaEntity extends EntityPathBase<BoardJpaEntity> {

    private static final long serialVersionUID = 1158014024L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardJpaEntity boardJpaEntity = new QBoardJpaEntity("boardJpaEntity");

    public final com.example.demo.common.jpa.QBaseTimeEntity _super = new com.example.demo.common.jpa.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath id = createString("id");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final com.example.demo.member.adapter.out.persistence.QMemberJpaEntity member;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBoardJpaEntity(String variable) {
        this(BoardJpaEntity.class, forVariable(variable), INITS);
    }

    public QBoardJpaEntity(Path<? extends BoardJpaEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardJpaEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardJpaEntity(PathMetadata metadata, PathInits inits) {
        this(BoardJpaEntity.class, metadata, inits);
    }

    public QBoardJpaEntity(Class<? extends BoardJpaEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.demo.member.adapter.out.persistence.QMemberJpaEntity(forProperty("member")) : null;
    }

}

