package com.example.demo.board.application.port.out;

import com.example.demo.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryBoardPort {
    Page<Board> findAllByPage(Pageable pageable);
}
