package io.github.pve123.hexagonal.member.domain;

import io.github.pve123.hexagonal.common.error.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 1, 1, 12, 0);

    @Test
    void 가입하면_26자리_ID가_생기고_탈퇴하지_않은_상태다() {
        Member member = newMember();

        assertThat(member.getId()).hasSize(26);
        assertThat(member.isDeleted()).isFalse();
    }

    @Test
    void 연락처를_바꿀_때_비어_있는_값은_그대로_둔다() {
        Member member = newMember();

        member.changeContact(null, "010-9999-9999", null);

        assertThat(member.getEmail()).isEqualTo("user@example.com");
        assertThat(member.getPhoneNumber()).isEqualTo("010-9999-9999");
        assertThat(member.getAddress()).isEqualTo("서울특별시 강남구 테헤란로 123");
    }

    @Test
    void 탈퇴하면_탈퇴_시각이_남는다() {
        Member member = newMember();

        member.withdraw(NOW);

        assertThat(member.isDeleted()).isTrue();
        assertThat(member.getDeletedAt()).isEqualTo(NOW);
    }

    @Test
    void 탈퇴한_회원은_다시_탈퇴하거나_연락처를_바꿀_수_없다() {
        Member member = newMember();
        member.withdraw(NOW);

        assertThatThrownBy(() -> member.withdraw(NOW))
                .isInstanceOf(BusinessException.class)
                .hasMessage(MemberErrorCode.MEMBER_WITHDRAWN.message());
        assertThatThrownBy(() -> member.changeContact("new@example.com", null, null))
                .isInstanceOf(BusinessException.class);
    }

    static Member newMember() {
        return Member.register("user@example.com", "encoded-password", "홍길동", Gender.MALE,
                "010-1234-5678", "서울특별시 강남구 테헤란로 123");
    }
}
