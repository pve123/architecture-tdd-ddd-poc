package com.example.demo.board.adapter.in.web;


import com.example.demo.board.adapter.in.web.request.CreateBoardRequest;
import com.example.demo.board.adapter.in.web.request.UpdateBoardRequest;
import com.example.demo.board.adapter.in.web.response.CreateBoardResponse;
import com.example.demo.board.adapter.in.web.response.GetBoardResponse;
import com.example.demo.board.application.port.in.CreateBoardUseCase;
import com.example.demo.board.application.port.in.GetBoardUseCase;
import com.example.demo.board.application.port.in.QueryBoardUseCase;
import com.example.demo.board.application.port.in.UpdateBoardUseCase;
import com.example.demo.board.domain.Board;
import com.example.demo.common.response.CommonResponse;
import com.example.demo.member.adapter.in.web.MemberWebMapper;
import com.example.demo.member.adapter.in.web.response.CreateMemberResponse;
import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import com.example.demo.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시글", description = "게시글 작성/조회/수정/삭제")
@RestController
@RequestMapping("/v1/board")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardWebMapper boardWebMapper;
    private final MemberWebMapper memberWebMapper;
    private final GetBoardUseCase getBoardUseCase;
    private final CreateBoardUseCase createBoardUseCase;
    private final UpdateBoardUseCase updateBoardUseCase;
    private final QueryBoardUseCase queryBoardUseCase;

    @Operation(
            summary = "게시글 작성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateBoardRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "게시글 작성 완료",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = CommonResponse.class)
                                    ),
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
        CreateMemberResponse createMemberResponse = memberWebMapper.toCreateMemberResponse(resultBoard.getMember());
        CreateBoardResponse createBoardResponse = boardWebMapper.toCreateBoardResponse(resultBoard, createMemberResponse);
        CommonResponse commonResponse = new CommonResponse<>("게시글 작성이 정상적으로 처리됐습니다", createBoardResponse);
        return commonResponse;
    }

    @Operation(
            summary = "게시글 목록 조회 (페이징)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시글 목록 조회 (페이징) 완료",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = GetBoardResponse.class))
                                    ),
                            }
                    ),
            }
    )
    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetBoardResponse> getBoards(Pageable pageable) {
        Page<Board> resultBoardList = queryBoardUseCase.getBoards(pageable);
        Page<GetBoardResponse> resultBoardResponseList = resultBoardList
                .map(item -> {
                    GetMemberResponse getMemberResponse = memberWebMapper.toGetMemberResponse(item.getMember());
                    return boardWebMapper.toGetBoardResponse(item, getMemberResponse);
                });
        return resultBoardResponseList;
    }


    @Operation(
            summary = "게시글 단일 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시글 단일 조회 완료",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GetBoardResponse.class)
                                    ),
                            }
                    ),
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GetBoardResponse getBoard(@Parameter(name = "id", description = "게시판 고유 ID", example = "01HZY74JZP5VDFKHX6D5YFRAZW")
                                     @RequestParam String id) {

        Board resultBoard = getBoardUseCase.getBoard(id);
        GetMemberResponse getMemberResponse = memberWebMapper.toGetMemberResponse(resultBoard.getMember());
        GetBoardResponse getBoardResponse = boardWebMapper.toGetBoardResponse(resultBoard, getMemberResponse);
        return getBoardResponse;
    }


    @Operation(
            summary = "게시글 수정",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateBoardRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시글 수정 완료",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CommonResponse.class)
                                    ),
                            }
                    ),
            }
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetBoardResponse updateBoard(@Parameter(description = "수정할 게시글 ID", example = "01HZY74JZP5VDFKHX6D5YFRAZW") @PathVariable String id,
                                        @Valid @RequestBody UpdateBoardRequest updateBoardRequest) {
        Member member = memberWebMapper.toDomain(updateBoardRequest.memberId());
        Board board = boardWebMapper.toDomain(updateBoardRequest, member, id);
        Board resultBoard = updateBoardUseCase.updateBoard(board);
        GetMemberResponse getMemberResponse = memberWebMapper.toGetMemberResponse(resultBoard.getMember());
        GetBoardResponse getBoardResponse = boardWebMapper.toGetBoardResponse(resultBoard, getMemberResponse);
        return getBoardResponse;
    }


}
