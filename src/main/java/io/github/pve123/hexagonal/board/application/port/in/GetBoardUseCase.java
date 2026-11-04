package io.github.pve123.hexagonal.board.application.port.in;

public interface GetBoardUseCase {

    /** 지운 글은 찾지 않습니다. 없으면 POST_NOT_FOUND 예외를 던집니다. */
    BoardView getBoard(String boardId);
}
