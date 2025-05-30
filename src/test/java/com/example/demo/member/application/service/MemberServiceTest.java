package com.example.demo.member.application.service;


import com.example.demo.config.unit.UnitTestConfig;
import com.example.demo.member.application.port.out.GetMemberPort;
import com.example.demo.member.application.port.out.CreateMemberPort;
import com.example.demo.member.domain.GenderEnum;
import com.example.demo.member.domain.Member;
import com.github.f4b6a3.ulid.UlidCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@DisplayName("Member UseCase Unit Test")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest extends UnitTestConfig {

    @Mock
    private CreateMemberPort createMemberPort;
    @Mock
    private GetMemberPort getMemberPort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberService memberService;

    @Test
    void 회원_생성_성공() {
        // given
        String id = UlidCreator.getUlid().toString();
        Member member = Member.builder()
                .id(id)
                .email("user@example.com")
                .password("QWERasdf1234!")
                .name("홍길동")
                .gender(GenderEnum.MALE)
                .phoneNumber("010-1234-5678")
                .address("서울특별시 강남구 테헤란로 123")
                .build();

        given(createMemberPort.save(any())).willReturn(member);

        // when
        Member result = memberService.createMember(member);

        // then
        assertAll(
                () -> assertEquals(member.getId(), result.getId()),
                () -> assertEquals(member.getEmail(), result.getEmail()),
                () -> assertEquals(member.getPassword(), result.getPassword()),
                () -> assertEquals(member.getName(), result.getName()),
                () -> assertEquals(member.getGender(), result.getGender()),
                () -> assertEquals(member.getPhoneNumber(), result.getPhoneNumber()),
                () -> assertEquals(member.getAddress(), result.getAddress())
        );

        verify(createMemberPort).save(any(Member.class));
    }
}
