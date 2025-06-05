package com.example.demo.board.adapter.in.web.response;

import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 작성 응답")
public record CreateBoardResponse(

        @Schema(description = "제목", example = "가입인사")
        String title,
        @Schema(description = "내용", example = "가입인사 작성합니다. 만나서 반갑습니다.")
        String content,
        @Schema(description = "게시글 사용자 정보")
        GetMemberResponse member
) {


}
