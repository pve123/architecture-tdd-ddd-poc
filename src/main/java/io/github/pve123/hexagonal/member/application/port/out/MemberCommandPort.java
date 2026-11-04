package io.github.pve123.hexagonal.member.application.port.out;

import io.github.pve123.hexagonal.member.domain.Member;

public interface MemberCommandPort {


    void softDeleteById(String id);

    Member save(Member member);

    Member update(Member member);
}
