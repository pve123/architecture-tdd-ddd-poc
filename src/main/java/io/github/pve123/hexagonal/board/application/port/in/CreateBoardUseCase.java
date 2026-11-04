package io.github.pve123.hexagonal.board.application.port.in;

public interface CreateBoardUseCase {

    BoardView createBoard(Command command);

    record Command(String memberId, String title, String content) {
    }
}
