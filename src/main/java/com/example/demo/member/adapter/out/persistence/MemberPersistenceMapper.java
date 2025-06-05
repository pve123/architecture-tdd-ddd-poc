package com.example.demo.member.adapter.out.persistence;

import com.example.demo.member.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberPersistenceMapper {

    MemberJpaEntity toJpaEntity(Member member);

    Member toDomain(MemberJpaEntity memberJpaEntity);

}
