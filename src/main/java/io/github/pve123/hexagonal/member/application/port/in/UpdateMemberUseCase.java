package io.github.pve123.hexagonal.member.application.port.in;

import io.github.pve123.hexagonal.member.domain.Member;

public interface UpdateMemberUseCase {

    Member updateMember(Command command);

    /** 비어 있는 값은 바꾸지 않습니다. */
    record Command(String memberId, String email, String phoneNumber, String address) {
    }
}
