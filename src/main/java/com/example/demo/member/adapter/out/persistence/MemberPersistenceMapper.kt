package com.example.demo.member.adapter.out.persistence

import com.example.demo.member.domain.Member
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface MemberPersistenceMapper {

    fun toJpaEntity(member: Member): MemberJpaEntity
    fun toDomain(memberJpaEntity: MemberJpaEntity): Member

}
