package com.example.demo.member.adapter.out.persistence;

import com.example.demo.member.domain.GenderEnum;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MemberSearchPredicateFactory {

    public BooleanExpression searchByEmailKeyword(QMemberJpaEntity qMemberJpaEntity, String email) {
        return StringUtils.hasText(email) ? qMemberJpaEntity.email.containsIgnoreCase(email) : null;
    }

    public BooleanExpression searchByNameKeyword(QMemberJpaEntity qMemberJpaEntity, String name) {
        return StringUtils.hasText(name) ? qMemberJpaEntity.name.containsIgnoreCase(name) : null;
    }

    public BooleanExpression searchByGenderKeyword(QMemberJpaEntity qMemberJpaEntity, String gender) {
        return StringUtils.hasText(gender) && EnumUtils.isValidEnumIgnoreCase(GenderEnum.class, gender) ?
                qMemberJpaEntity.gender.eq(GenderEnum.valueOf(gender)) :
                null;
    }

}
