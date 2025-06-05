package com.example.demo.board.adapter.out.persistence;


import com.example.demo.board.application.port.out.CreateBoardPort;
import com.example.demo.board.domain.Board;
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
public class BoardPersistenceAdapter implements CreateBoardPort {

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
}
