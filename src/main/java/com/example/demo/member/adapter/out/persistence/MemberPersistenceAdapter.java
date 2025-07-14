package com.example.demo.member.adapter.out.persistence;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.MemberErrorCodeEnum;
import com.example.demo.member.application.port.out.CreateMemberPort;
import com.example.demo.member.application.port.out.DeleteMemberPort;
import com.example.demo.member.application.port.out.GetMemberPort;
import com.example.demo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberPersistenceAdapter implements CreateMemberPort, GetMemberPort, DeleteMemberPort {

    private final MemberRepository memberRepository;
    private final MemberPersistenceMapper memberPersistenceMapper;

    @Override
    @Transactional
    public Member save(Member member) {
        MemberJpaEntity memberJpaEntity = memberPersistenceMapper.toJpaEntity(member);
        MemberJpaEntity resultMemberJpaEntity = memberRepository.save(memberJpaEntity);
        Member resultMember = memberPersistenceMapper.toDomain(resultMemberJpaEntity);
        return resultMember;
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
    public void softDeleteById(String id) {
        MemberJpaEntity memberJpaEntity = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MemberErrorCodeEnum.NOT_FOUND_MEMBER));
        memberJpaEntity.softDeleted();
    }
}
