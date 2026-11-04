package io.github.pve123.hexagonal.board.application.port.in;

import io.github.pve123.hexagonal.board.domain.Board;

public interface CreateBoardUseCase {

    Board createBoard(Command command);

    record Command(String memberId, String title, String content) {
    }
}
