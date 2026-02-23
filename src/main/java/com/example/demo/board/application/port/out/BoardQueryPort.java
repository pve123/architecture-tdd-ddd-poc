package com.example.demo.board.application.port.out;

import com.example.demo.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardQueryPort {

    Page<Board> searchBoards(Pageable pageable);
    Board findById(String id);
}
