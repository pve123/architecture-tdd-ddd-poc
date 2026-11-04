package io.github.pve123.hexagonal.board.application.service;

import io.github.pve123.hexagonal.board.application.port.out.BoardCommandPort;
import io.github.pve123.hexagonal.board.application.port.out.BoardQueryPort;
import io.github.pve123.hexagonal.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** 게시글 출구 포트를 메모리로 구현한 가짜입니다. */
class InMemoryBoardStore implements BoardQueryPort, BoardCommandPort {

    private final Map<String, Board> boards = new LinkedHashMap<>();

    @Override
    public Page<Board> search(Pageable pageable) {
        List<Board> active = boards.values().stream().filter(board -> !board.isDeleted()).toList();
        return new PageImpl<>(active, pageable, active.size());
    }

    @Override
    public Optional<Board> findActiveById(String boardId) {
        return Optional.ofNullable(boards.get(boardId)).filter(board -> !board.isDeleted());
    }

    @Override
    public Board save(Board board) {
        boards.put(board.getId(), board);
        return board;
    }
}
