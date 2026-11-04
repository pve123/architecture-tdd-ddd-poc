package io.github.pve123.hexagonal.board.application.port.out;

import io.github.pve123.hexagonal.board.domain.Board;

public interface BoardCommandPort {

    Board save(Board board);
    Board update(Board board);
    void softDeleteById(String id);
}
