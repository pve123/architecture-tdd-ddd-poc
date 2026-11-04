package io.github.pve123.hexagonal.member.application.service;

import io.github.pve123.hexagonal.common.error.BusinessException;
import io.github.pve123.hexagonal.member.application.port.in.CreateMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.UpdateMemberUseCase;
import io.github.pve123.hexagonal.member.domain.Gender;
import io.github.pve123.hexagonal.member.domain.Member;
import io.github.pve123.hexagonal.member.domain.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberServiceTest {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-01-01T03:00:00Z"), SEOUL);

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private InMemoryMemberStore store;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        store = new InMemoryMemberStore();
        memberService = new MemberService(store, store, passwordEncoder, FIXED_CLOCK);
    }

    @Test
    void 가입하면_비밀번호를_암호화해서_저장한다() {
        Member member = memberService.createMember(createCommand("user@example.com"));

        assertThat(member.getPassword()).isNotEqualTo("QWERasdf1234!");
        assertThat(passwordEncoder.matches("QWERasdf1234!", member.getPassword())).isTrue();
    }

    @Test
    void 이미_가입된_이메일로는_가입할_수_없다() {
        memberService.createMember(createCommand("user@example.com"));

        assertThatThrownBy(() -> memberService.createMember(createCommand("user@example.com")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(MemberErrorCode.EMAIL_DUPLICATED.message());
    }

    @Test
    void 다른_회원이_쓰는_이메일로는_바꿀_수_없다() {
        memberService.createMember(createCommand("taken@example.com"));
        Member member = memberService.createMember(createCommand("user@example.com"));

        assertThatThrownBy(() -> memberService.updateMember(
                new UpdateMemberUseCase.Command(member.getId(), "taken@example.com", null, null)))
                .isInstanceOf(BusinessException.class)
                .hasMessage(MemberErrorCode.EMAIL_DUPLICATED.message());
    }

    @Test
    void 탈퇴하면_고정한_시각으로_탈퇴_기록이_남고_조회되지_않는다() {
        Member member = memberService.createMember(createCommand("user@example.com"));

        memberService.deleteMember(member.getId());

        assertThat(store.findById(member.getId()).orElseThrow().getDeletedAt())
                .isEqualTo(LocalDateTime.of(2026, 1, 1, 12, 0));
        assertThatThrownBy(() -> memberService.getMember(member.getId()))
                .hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.message());
    }

    @Test
    void 탈퇴한_회원인지_없는_회원인지_구분해서_알려_준다() {
        Member member = memberService.createMember(createCommand("user@example.com"));
        memberService.deleteMember(member.getId());

        assertThatThrownBy(() -> memberService.checkActive(member.getId()))
                .hasMessage(MemberErrorCode.MEMBER_WITHDRAWN.message());
        assertThatThrownBy(() -> memberService.checkActive("01NOBODY000000000000000000"))
                .hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.message());
    }

    private static CreateMemberUseCase.Command createCommand(String email) {
        return new CreateMemberUseCase.Command(email, "QWERasdf1234!", "홍길동", Gender.MALE,
                "010-1234-5678", "서울특별시 강남구 테헤란로 123");
    }
}
