package io.github.pve123.hexagonal.board.application.port.in;

public interface DeleteBoardUseCase {

    /** 글쓴이만 지울 수 있습니다. 다른 회원이면 POST_AUTHOR_MISMATCH 예외를 던집니다. */
    void deleteBoard(String boardId, String requesterId);
}
