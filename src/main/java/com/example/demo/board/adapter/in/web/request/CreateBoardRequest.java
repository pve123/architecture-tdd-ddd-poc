package com.example.demo.board.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "게시글 작성 요청")
public record CreateBoardRequest(

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 255, message = "게시글은 255자 이하여야 합니다.")
        @Schema(description = "제목", example = "가입인사")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        @Schema(description = "내용", example = "가입인사 작성합니다. 만나서 반갑습니다.")
        String content,

        @NotBlank(message = "사용자 고유 ID는 필수입니다.")
        @Size(min = 26, max = 26, message = "사용자 ID는 26자입니다.")
        @Schema(description = "사용자 고유 ID", example = "01JWG8S471E52NTHD6T1G51F6M")
        String memberId

) {

}
