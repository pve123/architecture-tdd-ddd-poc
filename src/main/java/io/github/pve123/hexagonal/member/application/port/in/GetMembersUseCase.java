package io.github.pve123.hexagonal.member.application.port.in;

import io.github.pve123.hexagonal.member.adapter.in.web.request.MemberSearchRequest;
import io.github.pve123.hexagonal.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetMembersUseCase {

    Page<Member> getMembers(Pageable pageable, MemberSearchRequest memberSearchRequest);

}
