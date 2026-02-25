package com.example.demo.board.adapter.in.web;


import com.example.demo.board.adapter.in.web.request.CreateBoardRequest;
import com.example.demo.board.adapter.in.web.request.UpdateBoardRequest;
import com.example.demo.board.adapter.in.web.response.CreateBoardResponse;
import com.example.demo.board.adapter.in.web.response.GetBoardResponse;
import com.example.demo.board.domain.Board;
import com.example.demo.member.adapter.in.web.response.CreateMemberResponse;
import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardWebMapper {

    default Board toDomain(CreateBoardRequest createBoardRequest) {
        return Board.builder()
                .title(createBoardRequest.title())
                .content(createBoardRequest.content())
                .memberId(createBoardRequest.memberId())
                .build();
    }

    default Board toDomain(UpdateBoardRequest updateBoardRequest, String boardId) {
        return Board.builder()
                .id(boardId)
                .title(updateBoardRequest.title())
                .content(updateBoardRequest.content())
                .memberId(updateBoardRequest.memberId())
                .build();
    }

    default CreateBoardResponse toCreateBoardResponse(Board board, CreateMemberResponse member) {
        return new CreateBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                member,
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }

    default GetBoardResponse toGetBoardResponse(Board board, GetMemberResponse member) {
        return new GetBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                member,
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }

}
