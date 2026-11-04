package io.github.pve123.hexagonal.board.application.port.in;

import io.github.pve123.hexagonal.board.domain.Board;

public interface UpdateBoardUseCase {

    Board updateBoard(Command command);

    record Command(String boardId, String memberId, String title, String content) {
    }
}
