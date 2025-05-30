package com.example.demo.member.adapter.out.persistence;

import com.example.demo.member.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberPersistenceMapper {


    // Domain ↔ JPA Entity
    MemberJpaEntity toJpaEntity(Member member);

    // JPA Entity ↔ Domain
    Member toDomain(MemberJpaEntity memberJpaEntity);

}
