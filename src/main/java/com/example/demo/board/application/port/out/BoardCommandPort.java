package com.example.demo.board.application.port.out;

import com.example.demo.board.domain.Board;

public interface BoardCommandPort {

    Board save(Board board);
    Board update(Board board);
    void softDeleteById(String id);
}
