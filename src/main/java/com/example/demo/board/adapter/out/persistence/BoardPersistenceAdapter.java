package com.example.demo.board.adapter.out.persistence;


import com.example.demo.board.application.port.out.BoardCommandPort;
import com.example.demo.board.application.port.out.BoardQueryPort;
import com.example.demo.board.domain.Board;
import com.example.demo.common.exception.BoardErrorCodeEnum;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.MemberErrorCodeEnum;
import com.example.demo.member.adapter.out.persistence.MemberJpaEntity;
import com.example.demo.member.adapter.out.persistence.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardPersistenceAdapter implements BoardQueryPort, BoardCommandPort {

    private final BoardPersistenceMapper boardPersistenceMapper;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final JPAQueryFactory queryFactory;
    private final QBoardJpaEntity qBoardJpaEntity = QBoardJpaEntity.boardJpaEntity;

    @Override
    public Page<Board> searchBoards(Pageable pageable) {
        List<Board> content = queryFactory
                .selectFrom(qBoardJpaEntity)
                .where(qBoardJpaEntity.isDeleted.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(item -> {
                    Board board = boardPersistenceMapper.toDomain(item);
                    return board;
                })
                .toList();

        Long total = queryFactory
                .select(qBoardJpaEntity.count())
                .from(qBoardJpaEntity)
                .fetchOne();

        return new PageImpl<>(content, pageable, ObjectUtils.isNotEmpty(total) ? total : 0);
    }

    @Override
    public Board findById(String id) {
        BoardJpaEntity boardJpaEntity = boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BoardErrorCodeEnum.POST_NOT_FOUND));
        Board resultBoard = boardPersistenceMapper.toDomain(boardJpaEntity);
        return resultBoard;
    }

    @Override
    @Transactional
    public Board save(Board board) {
        MemberJpaEntity memberJpaEntity = memberRepository.getReferenceById(board.getMemberId());
        if (memberJpaEntity.getIsDeleted().equals(Boolean.TRUE)) {
            throw new BusinessException(MemberErrorCodeEnum.MEMBER_WITHDRAWN);
        }
        BoardJpaEntity boardJpaEntity = boardPersistenceMapper.toJpaEntity(board, memberJpaEntity);
        BoardJpaEntity resultBoardJpaEntity = boardRepository.save(boardJpaEntity);
        Board resultBoard = boardPersistenceMapper.toDomain(resultBoardJpaEntity);
        return resultBoard;
    }


    @Override
    @Transactional
    public Board update(Board board) {
        MemberJpaEntity memberJpaEntity = memberRepository.getReferenceById(board.getMemberId());
        BoardJpaEntity boardJpaEntity = boardRepository.findById(board.getId()).orElseThrow(() -> new BusinessException(BoardErrorCodeEnum.POST_NOT_FOUND));
        if (memberJpaEntity.getIsDeleted().equals(Boolean.TRUE)) {
            throw new BusinessException(MemberErrorCodeEnum.MEMBER_WITHDRAWN);
        } else if (!memberJpaEntity.getId().equals(boardJpaEntity.getMember().getId())) {
            throw new BusinessException(BoardErrorCodeEnum.POST_AUTHOR_MISMATCH);
        }
        boardJpaEntity.update(board);
        Board resultBoard = boardPersistenceMapper.toDomain(boardJpaEntity);
        return resultBoard;
    }

    @Override
    @Transactional
    public void softDeleteById(String id) {
        BoardJpaEntity boardJpaEntity = boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BoardErrorCodeEnum.POST_NOT_FOUND));
        boardJpaEntity.softDeleted();
    }
}
