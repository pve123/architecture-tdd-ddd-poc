package io.github.pve123.hexagonal.board.adapter.in.web;

import io.github.pve123.hexagonal.board.application.port.in.CreateBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.DeleteBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.GetBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.GetBoardsUseCase;
import io.github.pve123.hexagonal.board.application.port.in.UpdateBoardUseCase;
import io.github.pve123.hexagonal.board.domain.Board;
import io.github.pve123.hexagonal.common.web.PageResponse;
import io.github.pve123.hexagonal.member.adapter.in.web.MemberResponse;
import io.github.pve123.hexagonal.member.application.port.in.GetMemberUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** 게시글 API의 입구(입력 어댑터)입니다. 읽기는 누구나, 쓰기는 로그인한 회원만 할 수 있습니다. */
@Tag(name = "게시글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
public class BoardRestController {

    private final CreateBoardUseCase createBoardUseCase;
    private final GetBoardUseCase getBoardUseCase;
    private final GetBoardsUseCase getBoardsUseCase;
    private final UpdateBoardUseCase updateBoardUseCase;
    private final DeleteBoardUseCase deleteBoardUseCase;
    private final GetMemberUseCase getMemberUseCase;

    @Operation(summary = "게시글 목록")
    @GetMapping
    public PageResponse<BoardResponse> getBoards(@ParameterObject Pageable pageable) {
        return PageResponse.from(getBoardsUseCase.getBoards(pageable).map(this::toResponse));
    }

    @Operation(summary = "게시글 한 개 보기")
    @GetMapping("/{boardId}")
    public BoardResponse getBoard(@PathVariable String boardId) {
        return toResponse(getBoardUseCase.getBoard(boardId));
    }

    @Operation(summary = "게시글 쓰기")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardResponse createBoard(@AuthenticationPrincipal String memberId,
                                     @Valid @RequestBody BoardRequest.Create request) {
        return toResponse(createBoardUseCase.createBoard(request.toCommand(memberId)));
    }

    @Operation(summary = "게시글 고치기", description = "글쓴이만 고칠 수 있습니다.")
    @PatchMapping("/{boardId}")
    public BoardResponse updateBoard(@AuthenticationPrincipal String memberId,
                                     @PathVariable String boardId,
                                     @Valid @RequestBody BoardRequest.Update request) {
        return toResponse(updateBoardUseCase.updateBoard(request.toCommand(boardId, memberId)));
    }

    @Operation(summary = "게시글 지우기", description = "글쓴이만 지울 수 있습니다.")
    @DeleteMapping("/{boardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@AuthenticationPrincipal String memberId, @PathVariable String boardId) {
        deleteBoardUseCase.deleteBoard(boardId, memberId);
    }

    // TODO: 목록에서 글마다 회원을 따로 조회한다(N+1). 다음 커밋에서 글쓴이를 한 번에 모아 조회하도록 바꾼다.
    private BoardResponse toResponse(Board board) {
        return BoardResponse.of(board, MemberResponse.from(getMemberUseCase.getMember(board.getMemberId())));
    }
}
