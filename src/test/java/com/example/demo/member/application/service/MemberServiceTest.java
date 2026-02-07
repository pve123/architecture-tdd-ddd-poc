package com.example.demo.member.application.service;


import com.example.demo.config.TestDBConfig;
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
    private Member createMember;

    @BeforeEach
    void setup() {
        // given
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

        given(memberCommandPort.save(any(Member.class))).willReturn(member);

        // when
        createMember = memberService.createMember(member);
    }

    @Test
    void 회원_페이징_목록_조회_성공() {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        List<Member> memberList = List.of(createMember);
        Page<Member> members = new PageImpl<>(memberList, pageable, memberList.size());
        given(memberQueryPort.searchMembers(any(Pageable.class))).willReturn(members);


        //when
        Page<Member> result = memberService.getMembers(pageable, null);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(1),
                () -> assertThat(result.getTotalElements()).isEqualTo(1),
                () -> assertThat(result.getNumber()).isEqualTo(0),
                () -> assertThat(result.getSize()).isEqualTo(10),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(createMember.getId()),
                () -> assertThat(result.getContent().get(0).getEmail()).isEqualTo(createMember.getEmail()),
                () -> assertThat(result.getContent().get(0).getName()).isEqualTo(createMember.getName())
        );

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(memberQueryPort).searchMembers(captor.capture());
        assertAll(
                () -> assertThat(captor.getValue().getPageNumber()).isEqualTo(0),
                () -> assertThat(captor.getValue().getPageSize()).isEqualTo(10)
        );
    }

    @Test
    void 회원_조회_성공() {

        //given
        given(memberQueryPort.findById(eq(createMember.getId()))).willReturn(createMember);
        //when
        Member resultMember = memberService.getMember(createMember.getId());
        // then
        assertAll(
                () -> assertThat(resultMember.getId()).isNotNull(),
                () -> assertThat(resultMember.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(resultMember.getPassword()).isEqualTo(member.getPassword()),
                () -> assertThat(resultMember.getName()).isEqualTo(member.getName()),
                () -> assertThat(resultMember.getGender()).isEqualTo(member.getGender()),
                () -> assertThat(resultMember.getPhoneNumber()).isEqualTo(member.getPhoneNumber()),
                () -> assertThat(resultMember.getAddress()).isEqualTo(member.getAddress())
        );

        verify(memberQueryPort).findById(eq(createMember.getId()));
    }


    @Test
    void 회원_생성_성공() {

        // then
        assertAll(
                () -> assertThat(createMember.getId()).isNotNull(),
                () -> assertThat(createMember.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(createMember.getPassword()).isEqualTo(member.getPassword()),
                () -> assertThat(createMember.getName()).isEqualTo(member.getName()),
                () -> assertThat(createMember.getGender()).isEqualTo(member.getGender()),
                () -> assertThat(createMember.getPhoneNumber()).isEqualTo(member.getPhoneNumber()),
                () -> assertThat(createMember.getAddress()).isEqualTo(member.getAddress())
        );

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberCommandPort).save(captor.capture());

        Member saved = captor.getValue();
        assertAll(
                () -> assertThat(saved.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(saved.getName()).isEqualTo(member.getName())
        );
    }


    @Test
    void 회원_수정_성공() {

        //given
        Member updateRequest = createMember.toBuilder()
                .email("user77@example.com")
                .phoneNumber("010-1234-5677")
                .address("서울특별시 강남구 테헤란로 77")
                .build();

        given(memberCommandPort.update(any(Member.class))).willReturn(updateRequest);

        // when
        Member result = memberService.updateMember(updateRequest);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(updateRequest.getId()),
                () -> assertThat(result.getEmail()).isEqualTo(updateRequest.getEmail()),
                () -> assertThat(result.getPhoneNumber()).isEqualTo(updateRequest.getPhoneNumber()),
                () -> assertThat(result.getAddress()).isEqualTo(updateRequest.getAddress())
        );

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberCommandPort).update(captor.capture());
        Member updated = captor.getValue();

        assertAll(
                () -> assertThat(updated.getId()).isEqualTo(result.getId()),
                () -> assertThat(updated.getEmail()).isEqualTo(result.getEmail()),
                () -> assertThat(updated.getAddress()).isEqualTo(result.getAddress()),
                () -> assertThat(updated.getPhoneNumber()).isEqualTo(result.getPhoneNumber())
        );
    }

    @Test
    void 회원_삭제_성공() {

        // when
        memberService.deleteMember(createMember.getId());

        // then
        verify(memberCommandPort).softDeleteById(eq(createMember.getId()));
    }
}
