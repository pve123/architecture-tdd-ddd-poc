package io.github.pve123.hexagonal.board.application.port.in;

public interface UpdateBoardUseCase {

    BoardView updateBoard(Command command);

    record Command(String boardId, String memberId, String title, String content) {
    }
}
