package io.github.pve123.hexagonal.board.application.service;

import io.github.pve123.hexagonal.board.application.port.in.CreateBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.UpdateBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.out.AuthorPort;
import io.github.pve123.hexagonal.board.domain.Board;
import io.github.pve123.hexagonal.board.domain.BoardErrorCode;
import io.github.pve123.hexagonal.common.error.BusinessException;
import io.github.pve123.hexagonal.member.domain.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardServiceTest {

    private static final String WRITER_ID = "01WRITER000000000000000000";
    private static final String OTHER_ID = "01OTHER0000000000000000000";
    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-01-01T03:00:00Z"), ZoneId.of("Asia/Seoul"));

    private final Set<String> withdrawnMembers = new HashSet<>();
    private final AuthorPort authorPort = memberId -> {
        if (withdrawnMembers.contains(memberId)) {
            throw new BusinessException(MemberErrorCode.MEMBER_WITHDRAWN);
        }
    };
    private InMemoryBoardStore store;
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        store = new InMemoryBoardStore();
        boardService = new BoardService(store, store, authorPort, FIXED_CLOCK);
    }

    @Test
    void 글을_쓰면_목록에_보인다() {
        Board board = boardService.createBoard(new CreateBoardUseCase.Command(WRITER_ID, "제목", "내용"));

        assertThat(boardService.getBoards(Pageable.unpaged()).getContent())
                .extracting(Board::getId)
                .containsExactly(board.getId());
    }

    @Test
    void 탈퇴한_회원은_글을_쓸_수_없다() {
        withdrawnMembers.add(WRITER_ID);

        assertThatThrownBy(() -> boardService.createBoard(new CreateBoardUseCase.Command(WRITER_ID, "제목", "내용")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(MemberErrorCode.MEMBER_WITHDRAWN.message());
    }

    @Test
    void 다른_회원은_글을_고칠_수_없다() {
        Board board = boardService.createBoard(new CreateBoardUseCase.Command(WRITER_ID, "제목", "내용"));

        assertThatThrownBy(() -> boardService.updateBoard(
                new UpdateBoardUseCase.Command(board.getId(), OTHER_ID, "바꾼 제목", "바꾼 내용")))
                .hasMessage(BoardErrorCode.POST_AUTHOR_MISMATCH.message());
    }

    @Test
    void 지운_글은_다시_찾을_수_없다() {
        Board board = boardService.createBoard(new CreateBoardUseCase.Command(WRITER_ID, "제목", "내용"));

        boardService.deleteBoard(board.getId(), WRITER_ID);

        assertThatThrownBy(() -> boardService.getBoard(board.getId()))
                .hasMessage(BoardErrorCode.POST_NOT_FOUND.message());
    }
}
