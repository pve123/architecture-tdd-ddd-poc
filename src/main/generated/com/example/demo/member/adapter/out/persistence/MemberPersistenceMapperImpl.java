package com.example.demo.member.adapter.out.persistence;

import com.example.demo.member.domain.Member;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-30T14:38:54+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Amazon.com Inc.)"
)
@Component
public class MemberPersistenceMapperImpl implements MemberPersistenceMapper {

    @Override
    public MemberJpaEntity toJpaEntity(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberJpaEntity.MemberJpaEntityBuilder memberJpaEntity = MemberJpaEntity.builder();

        memberJpaEntity.id( member.getId() );
        memberJpaEntity.email( member.getEmail() );
        memberJpaEntity.password( member.getPassword() );
        memberJpaEntity.name( member.getName() );
        memberJpaEntity.gender( member.getGender() );
        memberJpaEntity.phoneNumber( member.getPhoneNumber() );
        memberJpaEntity.address( member.getAddress() );
        memberJpaEntity.isDeleted( member.getIsDeleted() );
        memberJpaEntity.deletedAt( member.getDeletedAt() );
        memberJpaEntity.createdAt( member.getCreatedAt() );
        memberJpaEntity.updatedAt( member.getUpdatedAt() );

        return memberJpaEntity.build();
    }

    @Override
    public Member toDomain(MemberJpaEntity memberJpaEntity) {
        if ( memberJpaEntity == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.id( memberJpaEntity.getId() );
        member.email( memberJpaEntity.getEmail() );
        member.password( memberJpaEntity.getPassword() );
        member.name( memberJpaEntity.getName() );
        member.gender( memberJpaEntity.getGender() );
        member.phoneNumber( memberJpaEntity.getPhoneNumber() );
        member.address( memberJpaEntity.getAddress() );
        member.isDeleted( memberJpaEntity.getIsDeleted() );
        member.deletedAt( memberJpaEntity.getDeletedAt() );
        member.createdAt( memberJpaEntity.getCreatedAt() );
        member.updatedAt( memberJpaEntity.getUpdatedAt() );

        return member.build();
    }
}
