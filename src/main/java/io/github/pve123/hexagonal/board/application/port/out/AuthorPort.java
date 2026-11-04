package io.github.pve123.hexagonal.board.application.port.out;

/**
 * 게시판이 회원 컨텍스트에 바라는 것입니다. 게시판은 회원 저장소나 회원 클래스를 직접 보지 않습니다.
 */
public interface AuthorPort {

    /** 글을 쓸 수 있는 회원인지 확인합니다. 없거나 탈퇴했으면 예외를 던집니다. */
    void checkCanWrite(String memberId);
}
