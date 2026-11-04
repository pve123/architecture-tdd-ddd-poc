package io.github.pve123.hexagonal.member.application.service;

import io.github.pve123.hexagonal.member.application.port.in.GetMembersUseCase.SearchCondition;
import io.github.pve123.hexagonal.member.application.port.out.MemberCommandPort;
import io.github.pve123.hexagonal.member.application.port.out.MemberQueryPort;
import io.github.pve123.hexagonal.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 회원 출구 포트를 메모리로 구현한 가짜입니다.
 * 서비스 테스트가 DB 없이 돌고, "포트를 불렀는지"가 아니라 "결과가 맞는지"를 확인할 수 있습니다.
 */
class InMemoryMemberStore implements MemberQueryPort, MemberCommandPort {

    private final Map<String, Member> members = new LinkedHashMap<>();

    @Override
    public Page<Member> search(SearchCondition condition, Pageable pageable) {
        List<Member> active = members.values().stream().filter(member -> !member.isDeleted()).toList();
        return new PageImpl<>(active, pageable, active.size());
    }

    @Override
    public Optional<Member> findActiveById(String memberId) {
        return findById(memberId).filter(member -> !member.isDeleted());
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    @Override
    public List<Member> findAllByIds(Collection<String> memberIds) {
        return memberIds.stream().map(members::get).filter(Objects::nonNull).toList();
    }

    @Override
    public Optional<Member> findActiveByEmail(String email) {
        return members.values().stream()
                .filter(member -> member.getEmail().equals(email) && !member.isDeleted())
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return members.values().stream().anyMatch(member -> member.getEmail().equals(email));
    }

    @Override
    public boolean existsByEmailOfOtherMember(String email, String memberId) {
        return members.values().stream()
                .anyMatch(member -> member.getEmail().equals(email) && !member.getId().equals(memberId));
    }

    @Override
    public Member save(Member member) {
        members.put(member.getId(), member);
        return member;
    }
}
