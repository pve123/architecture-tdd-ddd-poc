package io.github.pve123.hexagonal.member.application.port.in;

import io.github.pve123.hexagonal.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetMembersUseCase {

    Page<Member> getMembers(SearchCondition condition, Pageable pageable);

    /** 비어 있는 조건은 검색에 쓰지 않습니다. gender는 대소문자를 가리지 않습니다. */
    record SearchCondition(String email, String name, String gender) {
    }
}
