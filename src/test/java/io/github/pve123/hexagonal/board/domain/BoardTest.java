package io.github.pve123.hexagonal.board.domain;

import io.github.pve123.hexagonal.common.error.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardTest {

    private static final String WRITER_ID = "01WRITER000000000000000000";
    private static final String OTHER_ID = "01OTHER0000000000000000000";

    @Test
    void 글쓴이는_글을_고칠_수_있다() {
        Board board = Board.write(WRITER_ID, "제목", "내용");

        board.edit(WRITER_ID, "바꾼 제목", "바꾼 내용");

        assertThat(board.getTitle()).isEqualTo("바꾼 제목");
        assertThat(board.getContent()).isEqualTo("바꾼 내용");
    }

    @Test
    void 다른_회원은_글을_고칠_수_없다() {
        Board board = Board.write(WRITER_ID, "제목", "내용");

        assertThatThrownBy(() -> board.edit(OTHER_ID, "바꾼 제목", "바꾼 내용"))
                .isInstanceOf(BusinessException.class)
                .hasMessage(BoardErrorCode.POST_AUTHOR_MISMATCH.message());
        assertThat(board.getTitle()).isEqualTo("제목");
    }

    @Test
    void 지우면_삭제_표시와_시각이_남는다() {
        Board board = Board.write(WRITER_ID, "제목", "내용");
        LocalDateTime now = LocalDateTime.of(2026, 1, 1, 12, 0);

        board.delete(WRITER_ID, now);

        assertThat(board.isDeleted()).isTrue();
        assertThat(board.getDeletedAt()).isEqualTo(now);
    }

    @Test
    void 다른_회원은_글을_지울_수_없다() {
        Board board = Board.write(WRITER_ID, "제목", "내용");

        assertThatThrownBy(() -> board.delete(OTHER_ID, LocalDateTime.of(2026, 1, 1, 12, 0)))
                .isInstanceOf(BusinessException.class)
                .hasMessage(BoardErrorCode.POST_AUTHOR_MISMATCH.message());
        assertThat(board.isDeleted()).isFalse();
    }
}
