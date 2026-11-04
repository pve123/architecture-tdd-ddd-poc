package io.github.pve123.hexagonal.board.adapter.in.web;

import io.github.pve123.hexagonal.board.application.port.in.BoardView;

import java.time.LocalDateTime;

/** 게시글 응답입니다. 글쓴이는 ID와 이름만 담습니다. */
public record BoardResponse(
        String id,
        String title,
        String content,
        AuthorResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    static BoardResponse from(BoardView view) {
        return new BoardResponse(
                view.board().getId(),
                view.board().getTitle(),
                view.board().getContent(),
                new AuthorResponse(view.author().id(), view.author().name()),
                view.board().getCreatedAt(),
                view.board().getUpdatedAt());
    }

    public record AuthorResponse(String id, String name) {
    }
}
