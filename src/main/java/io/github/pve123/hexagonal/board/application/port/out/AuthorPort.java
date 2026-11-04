package io.github.pve123.hexagonal.board.application.port.out;

import io.github.pve123.hexagonal.board.domain.Author;

import java.util.Collection;
import java.util.Map;

/**
 * 게시판이 회원 컨텍스트에 바라는 것입니다. 게시판은 회원 저장소나 회원 클래스를 직접 보지 않습니다.
 */
public interface AuthorPort {

    /** 글을 쓸 수 있는 회원인지 확인합니다. 없거나 탈퇴했으면 예외를 던집니다. */
    void checkCanWrite(String memberId);

    /** 여러 글쓴이를 한 번에 찾습니다. 탈퇴한 회원은 Author.withdrawn으로 돌려줍니다. */
    Map<String, Author> findAuthors(Collection<String> memberIds);
}
