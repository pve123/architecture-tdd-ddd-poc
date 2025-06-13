package com.example.demo.board.adapter.out.persistence;


import com.example.demo.board.application.port.out.CreateBoardPort;
import com.example.demo.board.application.port.out.GetBoardPort;
import com.example.demo.board.application.port.out.UpdateBoardPort;
import com.example.demo.board.domain.Board;
import com.example.demo.common.exception.BoardErrorCodeEnum;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.member.adapter.out.persistence.MemberJpaEntity;
import com.example.demo.member.adapter.out.persistence.MemberPersistenceAdapter;
import com.example.demo.member.adapter.out.persistence.MemberPersistenceMapper;
import com.example.demo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardPersistenceAdapter implements CreateBoardPort, GetBoardPort, UpdateBoardPort {

    private final BoardPersistenceMapper boardPersistenceMapper;
    private final MemberPersistenceMapper memberPersistenceMapper;
    private final MemberPersistenceAdapter memberPersistenceAdapter;
    private final BoardRepository boardRepository;


    @Override
    @Transactional
    public Board save(Board board) {
        Member member = memberPersistenceAdapter.findById(board.getMember().getId());
        MemberJpaEntity memberJpaEntity = memberPersistenceMapper.toJpaEntity(member);
        BoardJpaEntity boardJpaEntity = boardPersistenceMapper.toJpaEntity(board, memberJpaEntity);
        BoardJpaEntity resultBoardJpaEntity = boardRepository.save(boardJpaEntity);
        Member resultMember = memberPersistenceMapper.toDomain(boardJpaEntity.getMember());
        Board resultBoard = boardPersistenceMapper.toDomain(resultBoardJpaEntity, resultMember);
        return resultBoard;
    }

    @Override
    public Board findById(String id) {
        BoardJpaEntity boardJpaEntity = boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BoardErrorCodeEnum.NOT_FOUND_BOARD));
        Member resultMember = memberPersistenceMapper.toDomain(boardJpaEntity.getMember());
        Board resultBoard = boardPersistenceMapper.toDomain(boardJpaEntity, resultMember);
        return resultBoard;
    }

    @Override
    @Transactional
    public Board update(Board board) {
        BoardJpaEntity boardJpaEntity = boardRepository.findById(board.getId())
                .orElseThrow(() -> new BusinessException(BoardErrorCodeEnum.NOT_FOUND_BOARD));
        boardJpaEntity.update(board);
        Member resultMember = memberPersistenceMapper.toDomain(boardJpaEntity.getMember());
        Board resultBoard = boardPersistenceMapper.toDomain(boardJpaEntity, resultMember);
        return resultBoard;
    }
}
