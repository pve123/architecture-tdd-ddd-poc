package io.github.pve123.hexagonal.member.application.port.in;

import io.github.pve123.hexagonal.member.domain.Member;

public interface UpdateMemberUseCase {

    Member updateMember(Member member);
}
