package com.example.demo.board.application.service;


import com.example.demo.board.application.port.in.*;
import com.example.demo.board.application.port.out.BoardCommandPort;
import com.example.demo.board.application.port.out.BoardQueryPort;
import com.example.demo.board.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService implements CreateBoardUseCase, GetBoardUseCase, UpdateBoardUseCase, DeleteBoardUseCase, QueryBoardUseCase {

    private final BoardCommandPort boardCommandPort;
    private final BoardQueryPort boardQueryPort;

    @Override
    public Page<Board> getBoards(Pageable pageable) {
        return boardQueryPort.searchBoards(pageable);
    }


    @Override
    public Board getBoard(String id) {
        Board resultBoard = boardQueryPort.findById(id);
        return resultBoard;
    }

    @Override
    public Board createBoard(Board board) {
        Board resultBoard = boardCommandPort.save(board);
        return resultBoard;
    }

    @Override
    public Board updateBoard(Board board) {
        Board resultBoard = boardCommandPort.update(board);
        return resultBoard;
    }

    @Override
    public void deleteBoard(String id) {
        boardCommandPort.softDeleteById(id);
    }
}
