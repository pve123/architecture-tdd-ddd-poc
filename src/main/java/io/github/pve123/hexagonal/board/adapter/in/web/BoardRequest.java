package io.github.pve123.hexagonal.board.adapter.in.web;

import io.github.pve123.hexagonal.board.application.port.in.CreateBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.UpdateBoardUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** 게시글 API가 받는 요청 모양을 모아 둡니다. */
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
            String content,

            @NotBlank(message = "사용자 고유 ID는 필수입니다.")
            @Size(min = 26, max = 26, message = "사용자 ID는 26자입니다.")
            String memberId
    ) {
        CreateBoardUseCase.Command toCommand() {
            return new CreateBoardUseCase.Command(memberId, title, content);
        }
    }

    record Update(
            @NotBlank(message = "제목은 필수입니다.")
            @Size(max = 255, message = "게시글은 255자 이하여야 합니다.")
            String title,

            @NotBlank(message = "내용은 필수입니다.")
            String content,

            @NotBlank(message = "사용자 고유 ID는 필수입니다.")
            @Size(min = 26, max = 26, message = "사용자 ID는 26자입니다.")
            String memberId
    ) {
        UpdateBoardUseCase.Command toCommand(String boardId) {
            return new UpdateBoardUseCase.Command(boardId, memberId, title, content);
        }
    }
}
