package com.example.demo.board.adapter.in.web;


import com.example.demo.board.adapter.in.web.request.CreateBoardRequest;
import com.example.demo.board.adapter.in.web.response.CreateBoardResponse;
import com.example.demo.board.domain.Board;
import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import com.example.demo.member.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardWebMapper {

    default Board toDomain(CreateBoardRequest createBoardRequest, Member member) {
        return Board.builder()
                .title(createBoardRequest.title())
                .content(createBoardRequest.content())
                .member(member)
                .build();
    }

    default CreateBoardResponse toCreateBoardResponse(Board board, GetMemberResponse member) {
        return new CreateBoardResponse(
                board.getTitle(),
                board.getContent(),
                member
        );
    }

}
