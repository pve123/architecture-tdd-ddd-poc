package com.example.demo.board.application.port.out;

import com.example.demo.board.domain.Board;

public interface GetBoardPort {

    Board findById(String id);
}
