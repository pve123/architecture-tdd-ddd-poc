package io.github.pve123.hexagonal.board.adapter.out.member;

import io.github.pve123.hexagonal.board.application.port.out.AuthorPort;
import io.github.pve123.hexagonal.member.application.port.in.CheckActiveMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 게시판의 출구(AuthorPort)를 회원 컨텍스트의 입구(CheckActiveMemberUseCase)에 잇습니다.
 * 두 컨텍스트가 만나는 곳은 이 클래스 하나뿐입니다.
 */
@Component
@RequiredArgsConstructor
class MemberAuthorAdapter implements AuthorPort {

    private final CheckActiveMemberUseCase checkActiveMemberUseCase;

    @Override
    public void checkCanWrite(String memberId) {
        checkActiveMemberUseCase.checkActive(memberId);
    }
}
