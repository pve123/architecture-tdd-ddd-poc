package io.github.pve123.hexagonal.member.application.port.in;

/**
 * 다른 컨텍스트(게시판)가 "이 회원이 활동할 수 있는지" 물을 때 쓰는 입구입니다.
 * 게시판은 회원 저장소를 직접 보지 않고 이 유스케이스만 부릅니다.
 */
public interface CheckActiveMemberUseCase {

    /** 회원이 없으면 MEMBER_NOT_FOUND, 탈퇴했으면 MEMBER_WITHDRAWN 예외를 던집니다. */
    void checkActive(String memberId);
}
