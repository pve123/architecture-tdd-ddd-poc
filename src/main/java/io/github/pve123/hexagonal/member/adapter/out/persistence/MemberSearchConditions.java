package io.github.pve123.hexagonal.member.adapter.out.persistence;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import io.github.pve123.hexagonal.member.application.port.in.GetMembersUseCase.SearchCondition;
import io.github.pve123.hexagonal.member.domain.Gender;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.pve123.hexagonal.member.adapter.out.persistence.QMemberJpaEntity.memberJpaEntity;

/** 회원 검색 조건과 정렬을 QueryDSL 식으로 바꿉니다. */
final class MemberSearchConditions {

    /** 정렬할 수 있는 칸만 열어 둡니다. 목록에 없는 칸은 무시합니다. */
    private static final Map<String, ComparableExpressionBase<?>> SORTABLE = Map.of(
            "name", memberJpaEntity.name,
            "email", memberJpaEntity.email,
            "createdAt", memberJpaEntity.createdAt);

    private MemberSearchConditions() {
    }

    /** null인 조건은 QueryDSL이 where 절에서 빼 줍니다. */
    static BooleanExpression[] where(SearchCondition condition) {
        return new BooleanExpression[]{
                memberJpaEntity.isDeleted.isFalse(),
                StringUtils.hasText(condition.email()) ? memberJpaEntity.email.containsIgnoreCase(condition.email()) : null,
                StringUtils.hasText(condition.name()) ? memberJpaEntity.name.containsIgnoreCase(condition.name()) : null,
                genderEquals(condition.gender())
        };
    }

    /** 요청한 정렬 뒤에 항상 최신 가입 순을 붙여서, 같은 값끼리도 순서가 정해지게 합니다. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    static OrderSpecifier<?>[] orderBy(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            ComparableExpressionBase<?> column = SORTABLE.get(order.getProperty());
            if (column != null) {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                orders.add(new OrderSpecifier(direction, column, OrderSpecifier.NullHandling.NullsLast));
            }
        }
        orders.add(memberJpaEntity.createdAt.desc());
        return orders.toArray(OrderSpecifier[]::new);
    }

    private static BooleanExpression genderEquals(String gender) {
        Gender found = EnumUtils.getEnumIgnoreCase(Gender.class, gender);
        return found == null ? null : memberJpaEntity.gender.eq(found);
    }
}
