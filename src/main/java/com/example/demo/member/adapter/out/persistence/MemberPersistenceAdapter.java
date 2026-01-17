package com.example.demo.member.adapter.out.persistence;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.MemberErrorCodeEnum;
import com.example.demo.member.application.port.out.MemberCommandPort;
import com.example.demo.member.application.port.out.MemberQueryPort;
import com.example.demo.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberPersistenceAdapter implements MemberQueryPort, MemberCommandPort {


    private final MemberRepository memberRepository;
    private final JPAQueryFactory queryFactory;
    private final QMemberJpaEntity qMemberJpaEntity = QMemberJpaEntity.memberJpaEntity;
    private final MemberPersistenceMapper memberPersistenceMapper;


    @Override
    public Page<Member> searchMembers(Pageable pageable) {

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

    @Override
    public Member findById(String id) {
        MemberJpaEntity memberJpaEntity = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MemberErrorCodeEnum.NOT_FOUND_MEMBER));
        Member resultMember = memberPersistenceMapper.toDomain(memberJpaEntity);
        return resultMember;
    }

    @Override
    @Transactional
    public Member save(Member member) {
        MemberJpaEntity memberJpaEntity = memberPersistenceMapper.toJpaEntity(member);
        MemberJpaEntity resultMemberJpaEntity = memberRepository.save(memberJpaEntity);
        Member resultMember = memberPersistenceMapper.toDomain(resultMemberJpaEntity);
        return resultMember;
    }


    @Override
    @Transactional
    public Member update(Member member) {
        MemberJpaEntity memberJpaEntity = memberRepository.findById(member.getId())
                .orElseThrow(() -> new BusinessException(MemberErrorCodeEnum.NOT_FOUND_MEMBER));
        memberJpaEntity.update(member);
        Member resultMember = memberPersistenceMapper.toDomain(memberJpaEntity);
        return resultMember;
    }

    @Override
    @Transactional
    public void softDeleteById(String id) {
        MemberJpaEntity memberJpaEntity = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MemberErrorCodeEnum.NOT_FOUND_MEMBER));
        memberJpaEntity.softDeleted();
    }
}
