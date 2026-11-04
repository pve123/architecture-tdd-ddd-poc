package io.github.pve123.hexagonal.board.application.service;

import io.github.pve123.hexagonal.board.application.port.in.CreateBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.DeleteBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.GetBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.in.GetBoardsUseCase;
import io.github.pve123.hexagonal.board.application.port.in.UpdateBoardUseCase;
import io.github.pve123.hexagonal.board.application.port.out.AuthorPort;
import io.github.pve123.hexagonal.board.application.port.out.BoardCommandPort;
import io.github.pve123.hexagonal.board.application.port.out.BoardQueryPort;
import io.github.pve123.hexagonal.board.domain.Board;
import io.github.pve123.hexagonal.board.domain.BoardErrorCode;
import io.github.pve123.hexagonal.common.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

/** 게시글 유스케이스를 처리합니다. 글쓴이 확인 같은 규칙은 Board가 지킵니다. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService implements CreateBoardUseCase, GetBoardUseCase, GetBoardsUseCase,
        UpdateBoardUseCase, DeleteBoardUseCase {

    private final BoardQueryPort boardQueryPort;
    private final BoardCommandPort boardCommandPort;
    private final AuthorPort authorPort;
    private final Clock clock;

    @Override
    @Transactional
    public Board createBoard(CreateBoardUseCase.Command command) {
        authorPort.checkCanWrite(command.memberId());
        return boardCommandPort.save(Board.write(command.memberId(), command.title(), command.content()));
    }

    @Override
    public Board getBoard(String boardId) {
        return findActiveBoard(boardId);
    }

    @Override
    public Page<Board> getBoards(Pageable pageable) {
        return boardQueryPort.search(pageable);
    }

    @Override
    @Transactional
    public Board updateBoard(UpdateBoardUseCase.Command command) {
        authorPort.checkCanWrite(command.memberId());
        Board board = findActiveBoard(command.boardId());
        board.edit(command.memberId(), command.title(), command.content());
        return boardCommandPort.save(board);
    }

    @Override
    @Transactional
    public void deleteBoard(String boardId, String requesterId) {
        Board board = findActiveBoard(boardId);
        board.delete(requesterId, LocalDateTime.now(clock));
        boardCommandPort.save(board);
    }

    private Board findActiveBoard(String boardId) {
        return boardQueryPort.findActiveById(boardId)
                .orElseThrow(() -> new BusinessException(BoardErrorCode.POST_NOT_FOUND));
    }
}
