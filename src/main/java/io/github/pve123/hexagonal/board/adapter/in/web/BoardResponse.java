package io.github.pve123.hexagonal.board.adapter.in.web;

import io.github.pve123.hexagonal.board.domain.Board;
import io.github.pve123.hexagonal.member.adapter.in.web.MemberResponse;

import java.time.LocalDateTime;

public record BoardResponse(
        String id,
        String title,
        String content,
        MemberResponse member,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    static BoardResponse of(Board board, MemberResponse member) {
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                member,
                board.getCreatedAt(),
                board.getUpdatedAt());
    }
}
