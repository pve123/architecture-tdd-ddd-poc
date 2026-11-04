package io.github.pve123.hexagonal.board.application.port.out;

import io.github.pve123.hexagonal.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardQueryPort {

    Page<Board> searchBoards(Pageable pageable);
    Board findById(String id);
}
