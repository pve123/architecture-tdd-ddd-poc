package io.github.pve123.hexagonal.board.adapter.in.web;

import io.github.pve123.hexagonal.board.application.port.in.CreateBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.UpdateBoardUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** 게시글 API가 받는 요청 모양을 모아 둡니다. 글쓴이는 요청 본문이 아니라 토큰으로 정합니다. */
final class BoardRequest {

    private BoardRequest() {
    }

    record Create(
            @Schema(example = "가입인사")
            @NotBlank(message = "제목은 필수입니다.")
            @Size(max = 255, message = "게시글은 255자 이하여야 합니다.")
            String title,

            @Schema(example = "가입인사 작성합니다. 만나서 반갑습니다.")
            @NotBlank(message = "내용은 필수입니다.")
            String content
    ) {
        CreateBoardUseCase.Command toCommand(String memberId) {
            return new CreateBoardUseCase.Command(memberId, title, content);
        }
    }

    record Update(
            @NotBlank(message = "제목은 필수입니다.")
            @Size(max = 255, message = "게시글은 255자 이하여야 합니다.")
            String title,

            @NotBlank(message = "내용은 필수입니다.")
            String content
    ) {
        UpdateBoardUseCase.Command toCommand(String boardId, String memberId) {
            return new UpdateBoardUseCase.Command(boardId, memberId, title, content);
        }
    }
}
