package io.github.pve123.hexagonal.member.adapter.out.persistence;

import io.github.pve123.hexagonal.common.config.JpaConfig;
import io.github.pve123.hexagonal.config.TestContainerConfig;
import io.github.pve123.hexagonal.member.application.port.in.GetMembersUseCase.SearchCondition;
import io.github.pve123.hexagonal.member.domain.Gender;
import io.github.pve123.hexagonal.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({MemberPersistenceAdapter.class, JpaConfig.class})
class MemberPersistenceAdapterTest extends TestContainerConfig {

    private static final SearchCondition NO_CONDITION = new SearchCondition(null, null, null);

    @Autowired
    private MemberPersistenceAdapter adapter;

    @Test
    void 저장하면_생성_시각이_채워지고_다시_읽을_수_있다() {
        Member saved = adapter.save(member("user@example.com", "홍길동", Gender.MALE));

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(adapter.findActiveById(saved.getId())).get()
                .extracting(Member::getEmail)
                .isEqualTo("user@example.com");
    }

    @Test
    void 바뀐_연락처를_저장한다() {
        Member saved = adapter.save(member("user@example.com", "홍길동", Gender.MALE));

        saved.changeContact(null, "010-9999-9999", null);
        adapter.save(saved);

        assertThat(adapter.findActiveById(saved.getId()).orElseThrow().getPhoneNumber()).isEqualTo("010-9999-9999");
    }

    @Test
    void 탈퇴한_회원은_조회와_검색에서_빠지지만_탈퇴_여부는_확인할_수_있다() {
        Member member = adapter.save(member("leave@example.com", "홍길동", Gender.MALE));
        member.withdraw(LocalDateTime.of(2026, 1, 1, 12, 0));
        adapter.save(member);

        assertThat(adapter.findActiveById(member.getId())).isEmpty();
        assertThat(adapter.findById(member.getId())).get().extracting(Member::isDeleted).isEqualTo(true);
        assertThat(adapter.search(NO_CONDITION, PageRequest.of(0, 10)).getTotalElements()).isZero();
    }

    @Test
    void 이메일_이름_성별로_검색한다() {
        adapter.save(member("kim@example.com", "김철수", Gender.MALE));
        adapter.save(member("lee@example.com", "이영희", Gender.FEMALE));

        assertThat(adapter.search(new SearchCondition("kim", null, null), PageRequest.of(0, 10)).getContent())
                .extracting(Member::getName).containsExactly("김철수");
        assertThat(adapter.search(new SearchCondition(null, "영희", null), PageRequest.of(0, 10)).getContent())
                .extracting(Member::getName).containsExactly("이영희");
        assertThat(adapter.search(new SearchCondition(null, null, "female"), PageRequest.of(0, 10)).getContent())
                .extracting(Member::getName).containsExactly("이영희");
    }

    @Test
    void 이름순으로_정렬한다() {
        adapter.save(member("b@example.com", "나영", Gender.FEMALE));
        adapter.save(member("a@example.com", "가영", Gender.FEMALE));

        assertThat(adapter.search(NO_CONDITION, PageRequest.of(0, 10, Sort.by("name"))).getContent())
                .extracting(Member::getName).containsExactly("가영", "나영");
    }

    private static Member member(String email, String name, Gender gender) {
        return Member.register(email, "encoded-password", name, gender, "010-1234-5678", "서울특별시 강남구 테헤란로 123");
    }
}
