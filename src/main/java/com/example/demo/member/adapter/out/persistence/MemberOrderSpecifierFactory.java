package com.example.demo.member.adapter.out.persistence;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MemberOrderSpecifierFactory {

    public OrderSpecifier<?>[] toOrderSpecifiers(QMemberJpaEntity qMemberJpaEntity, Sort sort) {


        List<OrderSpecifier<?>> orders = Lists.newArrayList();


        //정렬 안하는 경우 생성일 기준으로 내림차순
        if (sort.isUnsorted()) {
            orders.add(qMemberJpaEntity.createdAt.desc());
            return orders.toArray(new OrderSpecifier[0]);
        }

        for (Sort.Order order : sort) {
            //정렬순서
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            //정렬필드
            String prop = order.getProperty();

            switch (prop) {
                case "name" -> orders.add(new OrderSpecifier<>(
                        direction,
                        qMemberJpaEntity.name,
                        OrderSpecifier.NullHandling.NullsLast
                ));
                case "email" -> orders.add(new OrderSpecifier<>(
                        direction,
                        qMemberJpaEntity.email,
                        OrderSpecifier.NullHandling.NullsLast
                ));
                case "createdAt" -> orders.add(new OrderSpecifier<>(
                        direction,
                        qMemberJpaEntity.createdAt,
                        OrderSpecifier.NullHandling.NullsLast
                ));
            }
        }
        orders.add(qMemberJpaEntity.createdAt.desc());
        return orders.toArray(new OrderSpecifier[0]);
    }
}
