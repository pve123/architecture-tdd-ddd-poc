package io.github.pve123.hexagonal.board.application.port.out;

import io.github.pve123.hexagonal.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardQueryPort {

    /** 지운 글은 빼고 찾습니다. */
    Page<Board> search(Pageable pageable);

    /** 지운 글은 찾지 않습니다. */
    Optional<Board> findActiveById(String boardId);
}
