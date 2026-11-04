package io.github.pve123.hexagonal.board.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetBoardsUseCase {

    /** 글쓴이는 한 페이지에 한 번만 모아서 찾습니다. */
    Page<BoardView> getBoards(Pageable pageable);
}
