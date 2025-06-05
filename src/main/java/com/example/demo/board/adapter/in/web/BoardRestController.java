package com.example.demo.board.adapter.in.web;


import com.example.demo.board.adapter.in.web.request.CreateBoardRequest;
import com.example.demo.board.adapter.in.web.response.CreateBoardResponse;
import com.example.demo.board.application.port.in.CreateBoardUseCase;
import com.example.demo.board.domain.Board;
import com.example.demo.common.response.CommonResponse;
import com.example.demo.member.adapter.in.web.MemberWebMapper;
import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import com.example.demo.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시글", description = "게시글 작성/조회/수정/삭제")
@RestController
@RequestMapping("/v1/board")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardWebMapper boardWebMapper;
    private final MemberWebMapper memberWebMapper;
    private final CreateBoardUseCase createBoardUseCase;


    @Operation(
            summary = "게시글 작성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateBoardRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "게시글 작성 완료",
                            responseCode = "201",
                            content = {
                                    @Content(schema = @Schema(implementation = CommonResponse.class)),
                            }
                    ),
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse createBoard(@Valid @RequestBody CreateBoardRequest createBoardRequest) {
        Member member = memberWebMapper.toDomain(createBoardRequest.memberId());
        Board board = boardWebMapper.toDomain(createBoardRequest, member);
        Board resultBoard = createBoardUseCase.createBoard(board);
        GetMemberResponse resultMember = memberWebMapper.toGetMemberResponse(resultBoard.getMember());
        CreateBoardResponse createBoardResponse = boardWebMapper.toCreateBoardResponse(resultBoard, resultMember);
        CommonResponse commonResponse = new CommonResponse<>("게시글 작성이 정상적으로 처리됐습니다", createBoardResponse);
        return commonResponse;
    }
}
