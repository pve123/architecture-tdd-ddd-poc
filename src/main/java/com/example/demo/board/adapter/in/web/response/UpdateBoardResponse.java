package com.example.demo.board.adapter.in.web.response;

import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "게시글 작성 응답")
public record UpdateBoardResponse(

        @Schema(description = "ULID 기반 식별자", example = "01HZY74JZP5VDFKHX6D5YFRAZW")
        String id,
        @Schema(description = "제목", example = "가입인사")
        String title,
        @Schema(description = "내용", example = "가입인사 작성합니다. 만나서 반갑습니다.")
        String content,
        @Schema(description = "게시글 사용자 정보")
        GetMemberResponse member,
        @Schema(description = "생성일", example = "2025-01-01T15:55:55")
        LocalDateTime createdAt,
        @Schema(description = "수정일", example = "2023-12-10T55:55:55")
        LocalDateTime updatedAt
) {


}
