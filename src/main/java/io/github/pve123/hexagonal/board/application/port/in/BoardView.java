package io.github.pve123.hexagonal.board.application.port.in;

import io.github.pve123.hexagonal.board.domain.Author;
import io.github.pve123.hexagonal.board.domain.Board;

/** 화면에 보여 줄 게시글입니다. 게시글과 글쓴이를 함께 담습니다. */
public record BoardView(Board board, Author author) {
}
