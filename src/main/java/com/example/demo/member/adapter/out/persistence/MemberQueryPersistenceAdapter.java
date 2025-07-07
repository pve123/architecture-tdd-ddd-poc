package com.example.demo.member.adapter.out.persistence;

import com.example.demo.member.application.port.out.QueryMemberPort;
import com.example.demo.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberQueryPersistenceAdapter implements QueryMemberPort {

    private final JPAQueryFactory queryFactory;
    private final MemberPersistenceMapper memberPersistenceMapper;
    private final QMemberJpaEntity qMemberJpaEntity = QMemberJpaEntity.memberJpaEntity;

    @Override
    public Page<Member> findAllByPage(Pageable pageable) {

        List<Member> content = queryFactory
                .selectFrom(qMemberJpaEntity)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(memberPersistenceMapper::toDomain)
                .toList();

        Long total = queryFactory
                .select(qMemberJpaEntity.count())
                .from(qMemberJpaEntity)
                .fetchOne();

        return new PageImpl<>(content, pageable, ObjectUtils.isNotEmpty(total) ? total : 0);
    }
}
