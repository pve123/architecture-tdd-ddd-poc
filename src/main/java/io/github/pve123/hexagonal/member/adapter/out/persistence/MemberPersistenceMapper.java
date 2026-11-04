package io.github.pve123.hexagonal.member.adapter.out.persistence;

import io.github.pve123.hexagonal.member.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberPersistenceMapper {

    MemberJpaEntity toJpaEntity(Member member);

    Member toDomain(MemberJpaEntity memberJpaEntity);

}
