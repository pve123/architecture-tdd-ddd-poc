package com.example.demo.member.adapter.out.persistence;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberJpaEntity is a Querydsl query type for MemberJpaEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberJpaEntity extends EntityPathBase<MemberJpaEntity> {

    private static final long serialVersionUID = -1393659364L;

    public static final QMemberJpaEntity memberJpaEntity = new QMemberJpaEntity("memberJpaEntity");

    public final com.example.demo.common.jpa.QBaseTimeEntity _super = new com.example.demo.common.jpa.QBaseTimeEntity(this);

    public final StringPath address = createString("address");

    public final ListPath<com.example.demo.board.adapter.out.persistence.BoardJpaEntity, com.example.demo.board.adapter.out.persistence.QBoardJpaEntity> boardList = this.<com.example.demo.board.adapter.out.persistence.BoardJpaEntity, com.example.demo.board.adapter.out.persistence.QBoardJpaEntity>createList("boardList", com.example.demo.board.adapter.out.persistence.BoardJpaEntity.class, com.example.demo.board.adapter.out.persistence.QBoardJpaEntity.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final EnumPath<com.example.demo.member.domain.GenderEnum> gender = createEnum("gender", com.example.demo.member.domain.GenderEnum.class);

    public final StringPath id = createString("id");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberJpaEntity(String variable) {
        super(MemberJpaEntity.class, forVariable(variable));
    }

    public QMemberJpaEntity(Path<? extends MemberJpaEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberJpaEntity(PathMetadata metadata) {
        super(MemberJpaEntity.class, metadata);
    }

}

