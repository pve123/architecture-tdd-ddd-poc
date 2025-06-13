package com.example.demo.board.application.service;


import com.example.demo.board.application.port.in.CreateBoardUseCase;
import com.example.demo.board.application.port.in.GetBoardUseCase;
import com.example.demo.board.application.port.in.QueryBoardUseCase;
import com.example.demo.board.application.port.in.UpdateBoardUseCase;
import com.example.demo.board.application.port.out.CreateBoardPort;
import com.example.demo.board.application.port.out.GetBoardPort;
import com.example.demo.board.application.port.out.QueryBoardPort;
import com.example.demo.board.application.port.out.UpdateBoardPort;
import com.example.demo.board.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService implements CreateBoardUseCase, GetBoardUseCase, UpdateBoardUseCase, QueryBoardUseCase {

    private final CreateBoardPort createBoardPort;
    private final GetBoardPort getBoardPort;
    private final UpdateBoardPort updateBoardPort;
    private final QueryBoardPort queryBoardPort;

    @Override
    public Board createBoard(Board board) {
        Board resultBoard = createBoardPort.save(board);
        return resultBoard;
    }

    @Override
    public Board getBoard(String id) {
        Board resultBoard = getBoardPort.findById(id);
        return resultBoard;
    }

    @Override
    public Page<Board> getBoards(Pageable pageable) {
        return queryBoardPort.findAllByPage(pageable);
    }

    @Override
    public Board updateBoard(Board board) {
        Board resultBoard = updateBoardPort.update(board);
        return resultBoard;
    }
}
