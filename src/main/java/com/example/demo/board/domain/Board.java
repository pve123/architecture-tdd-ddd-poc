package com.example.demo.board.domain;


import com.example.demo.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@Schema(description = "게시글 도메인")
public class Board {

    @Schema(description = "ULID 기반 식별자", example = "01HZY74JZP5VDFKHX6D5YFRAZW")
    private String id;
    @Schema(description = "제목", example = "가입인사")
    private String title;
    @Schema(description = "내용", example = "가입인사 작성합니다. 만나서 반갑습니다.")
    private String content;
    @Schema(description = "작성자 정보", example = "01JWG8S471E52NTHD6T1G51F6M")
    private Member member;
    @Schema(description = "삭제유무", example = "false", allowableValues = {"true", "false"})
    private Boolean isDeleted;
    @Schema(description = "삭제일", example = "2025-01-01T15:55:55")
    private LocalDateTime deletedAt;
    @Schema(description = "생성일", example = "2025-01-01T15:55:55")
    private LocalDateTime createdAt;
    @Schema(description = "수정일", example = "2023-12-10T55:55:55")
    private LocalDateTime updatedAt;
}
