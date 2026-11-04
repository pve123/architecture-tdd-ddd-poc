package io.github.pve123.hexagonal.board.domain;

/**
 * 게시판에서 바라본 글쓴이입니다. 회원 컨텍스트의 Member와는 다른, 게시판이 필요한 만큼만 담은 값입니다.
 * 이름만 담고 연락처는 담지 않습니다.
 */
public record Author(String id, String name) {

    private static final String WITHDRAWN_NAME = "탈퇴한 회원";

    /** 글쓴이가 탈퇴했어도 글은 남으므로, 이름 대신 탈퇴했다는 표시를 보여 줍니다. */
    public static Author withdrawn(String id) {
        return new Author(id, WITHDRAWN_NAME);
    }
}
