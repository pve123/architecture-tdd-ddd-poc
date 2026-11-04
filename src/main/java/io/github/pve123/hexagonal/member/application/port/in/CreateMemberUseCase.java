package io.github.pve123.hexagonal.member.application.port.in;

import io.github.pve123.hexagonal.member.domain.Member;

public interface CreateMemberUseCase {

    Member createMember(Member member);
}
