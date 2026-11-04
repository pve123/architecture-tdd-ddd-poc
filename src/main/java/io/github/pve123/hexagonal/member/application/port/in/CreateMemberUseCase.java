package io.github.pve123.hexagonal.member.application.port.in;

import io.github.pve123.hexagonal.member.domain.Gender;
import io.github.pve123.hexagonal.member.domain.Member;

public interface CreateMemberUseCase {

    Member createMember(Command command);

    /** @param rawPassword 아직 암호화하지 않은 비밀번호. 서비스가 암호화합니다. */
    record Command(String email, String rawPassword, String name, Gender gender, String phoneNumber, String address) {
    }
}
