package com.example.demo.member.adapter.out.persistence

import com.example.demo.member.application.port.out.MemberQueryPort
import com.example.demo.member.domain.Member
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
@Transactional(readOnly = true)
class MemberQueryAdapter(

    private val memberRepository: MemberRepository,
    private val memberPersistenceMapper: MemberPersistenceMapper,
    private val queryFactory: JPAQueryFactory,
    private val qMemberJpaEntity: QMemberJpaEntity,

    ) : MemberQueryPort {


    override fun searchMembers(pageable: Pageable): Page<Member> {

        val content = queryFactory
            .selectFrom(qMemberJpaEntity)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
            .map(memberPersistenceMapper::toDomain)

        val total =
            if (content.isEmpty() && pageable.offset > 0) {
                0L
            } else {
                queryFactory
                    .select(qMemberJpaEntity.count())
                    .from(qMemberJpaEntity)
                    .fetchOne() ?: 0L
            }

        return PageImpl(content, pageable, total)
    }

    override fun findById(id: String): Member =
        memberRepository.findByIdOrThrow(id)
            .let(memberPersistenceMapper::toDomain)
}
