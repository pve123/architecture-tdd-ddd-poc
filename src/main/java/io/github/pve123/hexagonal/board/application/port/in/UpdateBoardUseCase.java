package io.github.pve123.hexagonal.board.application.port.in;

import io.github.pve123.hexagonal.board.domain.Board;

public interface UpdateBoardUseCase {
    Board updateBoard(Board board);
}
