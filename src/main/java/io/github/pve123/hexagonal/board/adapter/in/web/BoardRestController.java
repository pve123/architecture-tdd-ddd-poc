package io.github.pve123.hexagonal.board.adapter.in.web;

import io.github.pve123.hexagonal.board.application.port.in.CreateBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.DeleteBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.GetBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.GetBoardsUseCase;
import io.github.pve123.hexagonal.board.application.port.in.UpdateBoardUseCase;
import io.github.pve123.hexagonal.board.domain.Board;
import io.github.pve123.hexagonal.member.adapter.in.web.MemberResponse;
import io.github.pve123.hexagonal.member.application.port.in.GetMemberUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** 게시글 API의 입구(입력 어댑터)입니다. */
@Tag(name = "게시글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/board")
public class BoardRestController {

    private final CreateBoardUseCase createBoardUseCase;
    private final GetBoardUseCase getBoardUseCase;
    private final GetBoardsUseCase getBoardsUseCase;
    private final UpdateBoardUseCase updateBoardUseCase;
    private final DeleteBoardUseCase deleteBoardUseCase;
    private final GetMemberUseCase getMemberUseCase;

    @Operation(summary = "게시글 목록 (페이징)")
    @GetMapping("/page")
    public Page<BoardResponse> getBoards(@ParameterObject Pageable pageable) {
        return getBoardsUseCase.getBoards(pageable).map(this::toResponse);
    }

    @Operation(summary = "게시글 한 개 보기")
    @GetMapping
    public BoardResponse getBoard(@RequestParam String id) {
        return toResponse(getBoardUseCase.getBoard(id));
    }

    @Operation(summary = "게시글 쓰기")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardResponse createBoard(@Valid @RequestBody BoardRequest.Create request) {
        return toResponse(createBoardUseCase.createBoard(request.toCommand()));
    }

    @Operation(summary = "게시글 고치기", description = "글쓴이만 고칠 수 있습니다.")
    @PutMapping("/{id}")
    public BoardResponse updateBoard(@PathVariable String id, @Valid @RequestBody BoardRequest.Update request) {
        return toResponse(updateBoardUseCase.updateBoard(request.toCommand(id)));
    }

    @Operation(summary = "게시글 지우기")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@RequestParam String id) {
        deleteBoardUseCase.deleteBoard(id);
    }

    // TODO(feat/jwt-auth): 목록에서 글마다 회원을 따로 조회한다(N+1). 응답 모양을 바꾸는 PR에서 조회 모델로 바꾼다.
    private BoardResponse toResponse(Board board) {
        return BoardResponse.of(board, MemberResponse.from(getMemberUseCase.getMember(board.getMemberId())));
    }
}
