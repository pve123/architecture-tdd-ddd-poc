package io.github.pve123.hexagonal.member.application.port.out;

import io.github.pve123.hexagonal.member.application.port.in.GetMembersUseCase.SearchCondition;
import io.github.pve123.hexagonal.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/** 회원을 읽는 쪽 출구입니다. 바꾸는 쪽(MemberCommandPort)과 나눠서, 읽기 전용 구현을 따로 둘 수 있게 했습니다. */
public interface MemberQueryPort {

    /** 탈퇴한 회원은 빼고 찾습니다. */
    Page<Member> search(SearchCondition condition, Pageable pageable);

    /** 탈퇴한 회원은 찾지 않습니다. */
    Optional<Member> findActiveById(String memberId);

    /** 탈퇴한 회원도 찾습니다. 탈퇴 여부를 구분해야 할 때 씁니다. */
    Optional<Member> findById(String memberId);

    boolean existsByEmail(String email);

    boolean existsByEmailOfOtherMember(String email, String memberId);
}
