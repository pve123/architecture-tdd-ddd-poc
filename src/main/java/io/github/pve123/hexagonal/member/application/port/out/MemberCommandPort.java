package io.github.pve123.hexagonal.member.application.port.out;

import io.github.pve123.hexagonal.member.domain.Member;

public interface MemberCommandPort {

    /** 새 회원이면 추가하고, 있는 회원이면 바뀐 값을 저장합니다. */
    Member save(Member member);
}
