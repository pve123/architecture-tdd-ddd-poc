package com.example.demo.member.application.service;


import com.example.demo.config.TestDBConfig;
import com.example.demo.member.adapter.in.web.request.MemberSearchRequest;
import com.example.demo.member.application.port.out.MemberCommandPort;
import com.example.demo.member.application.port.out.MemberQueryPort;
import com.example.demo.member.domain.GenderEnum;
import com.example.demo.member.domain.Member;
import com.github.f4b6a3.ulid.UlidCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@DisplayName("Member UseCase Unit Test")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest extends TestDBConfig {

    @Mock
    private MemberCommandPort memberCommandPort;
    @Mock
    private MemberQueryPort memberQueryPort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberService memberService;

    private Member member;

    @BeforeEach
    void setup() {

        String id = UlidCreator.getUlid().toString();
        member = Member.builder()
                .id(id)
                .email("user@example.com")
                .password("QWERasdf1234!")
                .name("홍길동")
                .gender(GenderEnum.MALE)
                .phoneNumber("010-1234-5678")
                .address("서울특별시 강남구 테헤란로 123")
                .build();
    }

    @Test
    void 회원_페이징_목록_조회_성공() {

        // given
        Pageable pageable = PageRequest.of(0, 10);
        MemberSearchRequest searchRequest = new MemberSearchRequest(
                "test@test.com",
                "홍길동",
                "MALE"
        );
        List<Member> memberList = List.of(member);
        Page<Member> members = new PageImpl<>(memberList, pageable, memberList.size());
        given(
                memberQueryPort.searchMembers(
                        any(Pageable.class),
                        any(MemberSearchRequest.class)))
                .willReturn(members);


        // when
        Page<Member> result = memberService.getMembers(pageable, searchRequest);

        // then
        // 결과 페이지 검증
        assertAll(
                () -> assertThat(result.getContent()).hasSize(1),
                () -> assertThat(result.getTotalElements()).isEqualTo(1),
                () -> assertThat(result.getNumber()).isEqualTo(0),
                () -> assertThat(result.getSize()).isEqualTo(10),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(member.getId()),
                () -> assertThat(result.getContent().get(0).getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(result.getContent().get(0).getName()).isEqualTo(member.getName())
        );

        // 포트 호출 검증 (pageable, searchRequest 그대로 전달되었는지만 확인)
        verify(memberQueryPort).searchMembers(eq(pageable), eq(searchRequest));
    }

    @Test
    @DisplayName("회원 단건 조회 성공")
    void 회원_조회_성공() {

        // given
        String id = member.getId();
        given(memberQueryPort.findById(id)).willReturn(member);
        // when
        Member resultMember = memberService.getMember(id);
        // then
        // 반환값은 포트에서 받은 객체 그대로
        assertThat(resultMember).isSameAs(member);
        // 올바른 id로 포트가 호출되었는지만 검증
        verify(memberQueryPort).findById(eq(id));
    }


    @Test
    @DisplayName("회원 생성 성공")
    void 회원_생성_성공() {
        // given
        given(memberCommandPort.save(any(Member.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        Member result = memberService.createMember(member);

        // then
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberCommandPort).save(captor.capture());

        Member saved = captor.getValue();

        assertAll(
                () -> assertThat(saved.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(result.getEmail()).isEqualTo(saved.getEmail())
        );
    }


    @Test
    @DisplayName("회원 수정 성공")
    void 회원_수정_성공() {

        // given
        Member updateMember = member.toBuilder()
                .email("user77@example.com")
                .phoneNumber("010-1234-5677")
                .address("서울특별시 강남구 테헤란로 77")
                .build();

        given(memberCommandPort.update(any(Member.class))).willReturn(updateMember);

        // when
        Member result = memberService.updateMember(updateMember);

        // then
        // 결과는 포트가 리턴한 Member 기준으로만 검증
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(updateMember.getId()),
                () -> assertThat(result.getEmail()).isEqualTo(updateMember.getEmail()),
                () -> assertThat(result.getPhoneNumber()).isEqualTo(updateMember.getPhoneNumber()),
                () -> assertThat(result.getAddress()).isEqualTo(updateMember.getAddress())
        );

        // 포트 호출 시 updateRequest 그대로 넘겼는지만 검증
        verify(memberCommandPort).update(eq(updateMember));
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void 회원_삭제_성공() {
        // when
        memberService.deleteMember(member.getId());
        // then
        verify(memberCommandPort).softDeleteById(eq(member.getId()));
    }
}
