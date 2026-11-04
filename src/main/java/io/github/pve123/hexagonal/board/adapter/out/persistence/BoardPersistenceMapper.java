package io.github.pve123.hexagonal.board.adapter.out.persistence;


import io.github.pve123.hexagonal.board.domain.Board;
import io.github.pve123.hexagonal.member.adapter.out.persistence.MemberJpaEntity;
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

    default Board toDomain(BoardJpaEntity boardJpaEntity) {
        return Board.builder()
                .id(boardJpaEntity.getId())
                .title(boardJpaEntity.getTitle())
                .content(boardJpaEntity.getContent())
                .createdAt(boardJpaEntity.getCreatedAt())
                .updatedAt(boardJpaEntity.getUpdatedAt())
                .memberId(boardJpaEntity.getMember().getId())
                .build();
    }

}
