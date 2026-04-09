package com.example.demo.board.application.service;

import com.example.demo.board.application.port.out.BoardCommandPort;
import com.example.demo.board.application.port.out.BoardQueryPort;
import com.example.demo.board.domain.Board;
import com.github.f4b6a3.ulid.UlidCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("Board UseCase Unit Test")
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardCommandPort boardCommandPort;

    @Mock
    private BoardQueryPort boardQueryPort;

    @InjectMocks
    private BoardService boardService;

    private Board board;

    @BeforeEach
    void setup() {
        board = Board.builder()
                .id(UlidCreator.getUlid().toString())
                .title("가입인사")
                .content("가입인사 작성합니다. 만나서 반갑습니다.")
                .memberId(UlidCreator.getUlid().toString())
                .build();
    }

    @Test
    @DisplayName("게시글 페이징 목록 조회 성공")
    void 게시글_페이징_목록_조회_성공() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> boards = new PageImpl<>(List.of(board), pageable, 1);
        given(boardQueryPort.searchBoards(any(Pageable.class))).willReturn(boards);

        Page<Board> result = boardService.getBoards(pageable);

        assertAll(
                () -> assertThat(result.getContent()).hasSize(1),
                () -> assertThat(result.getTotalElements()).isEqualTo(1),
                () -> assertThat(result.getNumber()).isEqualTo(0),
                () -> assertThat(result.getSize()).isEqualTo(10),
                () -> assertThat(result.getContent().getFirst().getId()).isEqualTo(board.getId()),
                () -> assertThat(result.getContent().getFirst().getTitle()).isEqualTo(board.getTitle())
        );

        verify(boardQueryPort).searchBoards(eq(pageable));
    }

    @Test
    @DisplayName("게시글 단건 조회 성공")
    void 게시글_단건_조회_성공() {
        given(boardQueryPort.findById(board.getId())).willReturn(board);

        Board resultBoard = boardService.getBoard(board.getId());

        assertThat(resultBoard).isSameAs(board);
        verify(boardQueryPort).findById(eq(board.getId()));
    }

    @Test
    @DisplayName("게시글 생성 성공")
    void 게시글_생성_성공() {
        given(boardCommandPort.save(any(Board.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        Board result = boardService.createBoard(board);

        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
        verify(boardCommandPort).save(captor.capture());
        Board saved = captor.getValue();

        assertAll(
                () -> assertThat(saved.getTitle()).isEqualTo(board.getTitle()),
                () -> assertThat(saved.getContent()).isEqualTo(board.getContent()),
                () -> assertThat(result.getMemberId()).isEqualTo(saved.getMemberId())
        );
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void 게시글_수정_성공() {
        Board updateBoard = board.toBuilder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        given(boardCommandPort.update(any(Board.class))).willReturn(updateBoard);

        Board result = boardService.updateBoard(updateBoard);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(updateBoard.getId()),
                () -> assertThat(result.getTitle()).isEqualTo(updateBoard.getTitle()),
                () -> assertThat(result.getContent()).isEqualTo(updateBoard.getContent())
        );

        verify(boardCommandPort).update(eq(updateBoard));
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void 게시글_삭제_성공() {
        boardService.deleteBoard(board.getId());

        verify(boardCommandPort).softDeleteById(eq(board.getId()));
    }
}
