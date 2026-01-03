package com.example.demo.member.adapter.out.persistence

import com.example.demo.member.application.port.`in`.UpdateMemberCommand
import com.example.demo.member.application.port.out.MemberCommandPort
import com.example.demo.member.domain.Member
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
@Transactional
class MemberCommandAdapter(

    private val memberRepository: MemberRepository,
    private val memberPersistenceMapper: MemberPersistenceMapper

) : MemberCommandPort {


    override fun save(member: Member): Member =
        memberPersistenceMapper.toJpaEntity(member)
            .let(memberRepository::save)
            .let(memberPersistenceMapper::toDomain)

    override fun update(member: Member): Member =
        memberRepository.findByIdOrThrow(member.id)
            .also { it.applyUpdate(member) }
            .let(memberPersistenceMapper::toDomain)

    override fun softDeleteById(id: String) {
        memberRepository.findByIdOrThrow(id)
            .also { it.softDelete() }
    }


}