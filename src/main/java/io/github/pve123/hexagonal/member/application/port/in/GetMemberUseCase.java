package io.github.pve123.hexagonal.member.application.port.in;

import io.github.pve123.hexagonal.member.domain.Member;

public interface GetMemberUseCase {

    /** 탈퇴하지 않은 회원을 찾습니다. 없으면 MEMBER_NOT_FOUND 예외를 던집니다. */
    Member getMember(String memberId);
}
