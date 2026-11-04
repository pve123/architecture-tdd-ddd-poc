package io.github.pve123.hexagonal.board.adapter.out.persistence;


import io.github.pve123.hexagonal.board.application.port.out.BoardCommandPort;
import io.github.pve123.hexagonal.board.application.port.out.BoardQueryPort;
import io.github.pve123.hexagonal.board.domain.Board;
import io.github.pve123.hexagonal.common.exception.BoardErrorCodeEnum;
import io.github.pve123.hexagonal.common.exception.BusinessException;
import io.github.pve123.hexagonal.common.exception.MemberErrorCodeEnum;
import io.github.pve123.hexagonal.member.adapter.out.persistence.MemberJpaEntity;
import io.github.pve123.hexagonal.member.adapter.out.persistence.MemberRepository;
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
                .where(qBoardJpaEntity.isDeleted.eq(false))
                .fetchOne();

        return new PageImpl<>(content, pageable, ObjectUtils.isNotEmpty(total) ? total : 0);
    }

    @Override
    public Board findById(String id) {
        BoardJpaEntity boardJpaEntity = boardRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(BoardErrorCodeEnum.POST_NOT_FOUND));
        Board resultBoard = boardPersistenceMapper.toDomain(boardJpaEntity);
        return resultBoard;
    }

    @Override
    @Transactional
    public Board save(Board board) {
        MemberJpaEntity memberJpaEntity = findAuthor(board.getMemberId());
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
        MemberJpaEntity memberJpaEntity = findAuthor(board.getMemberId());
        BoardJpaEntity boardJpaEntity = boardRepository.findByIdAndIsDeletedFalse(board.getId()).orElseThrow(() -> new BusinessException(BoardErrorCodeEnum.POST_NOT_FOUND));
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
        BoardJpaEntity boardJpaEntity = boardRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(BoardErrorCodeEnum.POST_NOT_FOUND));
        boardJpaEntity.softDeleted();
    }

    /** getReferenceById는 없는 회원이어도 프록시를 돌려주고, 나중에 내부 클래스 이름이 담긴 예외를 던져서 먼저 확인합니다. */
    private MemberJpaEntity findAuthor(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCodeEnum.MEMBER_NOT_FOUND));
    }
}
