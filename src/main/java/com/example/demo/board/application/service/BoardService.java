package com.example.demo.board.application.service;


import com.example.demo.board.application.port.in.CreateBoardUseCase;
import com.example.demo.board.application.port.out.CreateBoardPort;
import com.example.demo.board.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService implements CreateBoardUseCase {

    private final CreateBoardPort createBoardPort;

    @Override
    public Board createBoard(Board board) {
        Board resultBoard = createBoardPort.save(board);
        return resultBoard;
    }
}
