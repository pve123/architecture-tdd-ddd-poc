package com.example.demo.board.adapter.out.persistence;


import com.example.demo.board.domain.Board;
import com.example.demo.member.adapter.out.persistence.MemberJpaEntity;
import com.example.demo.member.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardPersistenceMapper {

    default BoardJpaEntity toJpaEntity(Board board, MemberJpaEntity memberJpaEntity) {
        return BoardJpaEntity.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .member(memberJpaEntity)
                .build();
    }

    default Board toDomain(BoardJpaEntity boardJpaEntity, Member member) {
        return Board.builder()
                .id(boardJpaEntity.getId())
                .title(boardJpaEntity.getTitle())
                .content(boardJpaEntity.getContent())
                .createdAt(boardJpaEntity.getCreatedAt())
                .updatedAt(boardJpaEntity.getUpdatedAt())
                .member(member)
                .build();
    }

}
