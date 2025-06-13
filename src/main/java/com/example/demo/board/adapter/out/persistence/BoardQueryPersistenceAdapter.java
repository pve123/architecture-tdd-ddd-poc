package com.example.demo.board.adapter.out.persistence;


import com.example.demo.board.application.port.out.QueryBoardPort;
import com.example.demo.board.domain.Board;
import com.example.demo.member.adapter.out.persistence.MemberPersistenceMapper;
import com.example.demo.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardQueryPersistenceAdapter implements QueryBoardPort {

    private final JPAQueryFactory queryFactory;
    private final QBoardJpaEntity qBoardJpaEntity = QBoardJpaEntity.boardJpaEntity;
    private final BoardPersistenceMapper boardPersistenceMapper;
    private final MemberPersistenceMapper memberPersistenceMapper;

    @Override
    public Page<Board> findAllByPage(Pageable pageable) {

        List<Board> content = queryFactory
                .selectFrom(qBoardJpaEntity)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(item -> {
                    Member member = memberPersistenceMapper.toDomain(item.getMember());
                    Board board = boardPersistenceMapper.toDomain(item, member);
                    return board;
                })
                .toList();

        Long total = queryFactory
                .select(qBoardJpaEntity.count())
                .from(qBoardJpaEntity)
                .fetchOne();

        return new PageImpl<>(content, pageable, ObjectUtils.isNotEmpty(total) ? total : 0);
    }
}
