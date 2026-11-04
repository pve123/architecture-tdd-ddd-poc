package io.github.pve123.hexagonal.board.application.port.in;

import io.github.pve123.hexagonal.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetBoardsUseCase {

    Page<Board> getBoards(Pageable pageable);
}
