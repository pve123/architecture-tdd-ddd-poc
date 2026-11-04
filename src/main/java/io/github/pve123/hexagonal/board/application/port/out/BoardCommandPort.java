package io.github.pve123.hexagonal.board.application.port.out;

import io.github.pve123.hexagonal.board.domain.Board;

public interface BoardCommandPort {

    /** 새 글이면 추가하고, 있는 글이면 바뀐 값을 저장합니다. */
    Board save(Board board);
}
