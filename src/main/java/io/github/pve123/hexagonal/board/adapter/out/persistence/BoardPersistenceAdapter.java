package io.github.pve123.hexagonal.board.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.github.pve123.hexagonal.board.application.port.out.BoardCommandPort;
import io.github.pve123.hexagonal.board.application.port.out.BoardQueryPort;
import io.github.pve123.hexagonal.board.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static io.github.pve123.hexagonal.board.adapter.out.persistence.QBoardJpaEntity.boardJpaEntity;

/** 게시글 출구 포트를 JPA와 QueryDSL로 구현합니다. 트랜잭션은 이 클래스를 부르는 서비스가 엽니다. */
@Component
@RequiredArgsConstructor
class BoardPersistenceAdapter implements BoardQueryPort, BoardCommandPort {

    private final BoardJpaRepository boardJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Board> search(Pageable pageable) {
        List<Board> content = queryFactory
                .selectFrom(boardJpaEntity)
                .where(boardJpaEntity.isDeleted.isFalse())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(BoardJpaEntity::toDomain)
                .toList();

        return PageableExecutionUtils.getPage(content, pageable, () -> queryFactory
                .select(boardJpaEntity.count())
                .from(boardJpaEntity)
                .where(boardJpaEntity.isDeleted.isFalse())
                .fetchOne());
    }

    @Override
    public Optional<Board> findActiveById(String boardId) {
        return boardJpaRepository.findByIdAndIsDeletedFalse(boardId).map(BoardJpaEntity::toDomain);
    }

    @Override
    public Board save(Board board) {
        BoardJpaEntity entity = boardJpaRepository.findById(board.getId())
                .map(found -> {
                    found.apply(board);
                    return found;
                })
                .orElseGet(() -> BoardJpaEntity.from(board));
        // 생성·수정 시각은 DB에 쓰는 순간 채워지므로 바로 내보내서 응답에 담습니다.
        return boardJpaRepository.saveAndFlush(entity).toDomain();
    }
}
