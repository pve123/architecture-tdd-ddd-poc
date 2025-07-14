package com.example.demo.member.application.service;


import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.MemberErrorCodeEnum;
import com.example.demo.config.unit.UnitTestConfig;
import com.example.demo.member.application.port.out.CreateMemberPort;
import com.example.demo.member.application.port.out.DeleteMemberPort;
import com.example.demo.member.application.port.out.GetMemberPort;
import com.example.demo.member.application.port.out.QueryMemberPort;
import com.example.demo.member.domain.GenderEnum;
import com.example.demo.member.domain.Member;
import com.github.f4b6a3.ulid.UlidCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


@DisplayName("Member UseCase Unit Test")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest extends UnitTestConfig {

    @Mock
    private CreateMemberPort createMemberPort;
    @Mock
    private GetMemberPort getMemberPort;
    @Mock
    private DeleteMemberPort deleteMemberPort;
    @Mock
    private QueryMemberPort queryMemberPort;
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

        given(createMemberPort.save(any(Member.class))).willReturn(member);

        // when
        createMember = memberService.createMember(member);
    }

    @Test
    void 회원_생성_성공() {

        // then
        assertAll(
                () -> assertThat(createMember.getId()).isNotNull(),
                () -> assertThat(member.getEmail()).isEqualTo(createMember.getEmail()),
                () -> assertThat(member.getPassword()).isEqualTo(createMember.getPassword()),
                () -> assertThat(member.getName()).isEqualTo(createMember.getName()),
                () -> assertThat(member.getGender()).isEqualTo(createMember.getGender()),
                () -> assertThat(member.getPhoneNumber()).isEqualTo(createMember.getPhoneNumber()),
                () -> assertThat(member.getAddress()).isEqualTo(createMember.getAddress())
        );

        verify(createMemberPort).save(any(Member.class));
    }

    @Test
    void 회원_조회_성공() {

        //when
        given(getMemberPort.findById(anyString())).willReturn(createMember);
        Member resultMember = memberService.getMember(createMember.getId());
        // then
        assertAll(
                () -> assertThat(resultMember.getId()).isNotNull(),
                () -> assertThat(member.getEmail()).isEqualTo(resultMember.getEmail()),
                () -> assertThat(member.getPassword()).isEqualTo(resultMember.getPassword()),
                () -> assertThat(member.getName()).isEqualTo(resultMember.getName()),
                () -> assertThat(member.getGender()).isEqualTo(resultMember.getGender()),
                () -> assertThat(member.getPhoneNumber()).isEqualTo(resultMember.getPhoneNumber()),
                () -> assertThat(member.getAddress()).isEqualTo(resultMember.getAddress())
        );

        verify(getMemberPort).findById(anyString());
    }

    @Test
    void 회원_페이징_목록_조회_성공() {

        //when
        Pageable pageable = PageRequest.of(0, 10);
        List<Member> memberList = List.of(createMember);
        Page<Member> members = new PageImpl<>(memberList, pageable, memberList.size());
        given(queryMemberPort.findAllByPage(pageable)).willReturn(members);
        Page<Member> resultMemberList = memberService.getMembers(pageable);
        // then
        assertAll(
                () -> assertThat(resultMemberList.getContent()).isNotEmpty(),
                () -> assertThat(resultMemberList.getContent().getFirst().getId()).isNotNull(),
                () -> assertThat(member.getEmail()).isEqualTo(resultMemberList.getContent().getFirst().getEmail()),
                () -> assertThat(member.getPassword()).isEqualTo(resultMemberList.getContent().getFirst().getPassword()),
                () -> assertThat(member.getName()).isEqualTo(resultMemberList.getContent().getFirst().getName()),
                () -> assertThat(member.getGender()).isEqualTo(resultMemberList.getContent().getFirst().getGender()),
                () -> assertThat(member.getPhoneNumber()).isEqualTo(resultMemberList.getContent().getFirst().getPhoneNumber()),
                () -> assertThat(member.getAddress()).isEqualTo(resultMemberList.getContent().getFirst().getAddress())
        );

        verify(queryMemberPort).findAllByPage(pageable);
    }


    @Test
    void 회원_삭제_성공() {

        //when
        doNothing().when(deleteMemberPort).softDeleteById(eq(createMember.getId()));
        memberService.deleteMember(createMember.getId());
        // 삭제 후 조회 시 예외 발생하도록 설정
        given(getMemberPort.findById(eq(createMember.getId())))
                .willThrow(new BusinessException(MemberErrorCodeEnum.NOT_FOUND_MEMBER));
        // then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.getMember(createMember.getId()))
                        .isInstanceOf(BusinessException.class)
        );

        verify(deleteMemberPort).softDeleteById(eq(createMember.getId()));
        verify(getMemberPort).findById(eq(createMember.getId()));
    }
}
