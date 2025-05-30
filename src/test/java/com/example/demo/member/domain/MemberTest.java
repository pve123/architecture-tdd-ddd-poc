package com.example.demo.member.domain;


import com.example.demo.config.unit.UnitTestConfig;
import com.example.demo.member.adapter.in.web.MemberWebMapper;
import com.example.demo.member.adapter.in.web.MemberWebMapperImpl;
import com.example.demo.member.adapter.in.web.request.CreateMemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Member Domain Unit Test")
public class MemberTest extends UnitTestConfig {

    private final MemberWebMapper mapper = new MemberWebMapperImpl();

    @Test
    void 회원_정상_생성_요청에서_도메인_변환() {
        // given
        CreateMemberRequest request = new CreateMemberRequest(
                "user@example.com",
                "QWERasdf1234!",
                "홍길동",
                GenderEnum.MALE,
                "010-1234-5678",
                "서울특별시 강남구 테헤란로 123"
        );

        // when
        Member member = mapper.toDomain(request);

        // then
        assertAll(
                () -> assertEquals("user@example.com", member.getEmail()),
                () -> assertEquals("QWERasdf1234!", member.getPassword()),
                () -> assertEquals("홍길동", member.getName()),
                () -> assertEquals(GenderEnum.MALE, member.getGender()),
                () -> assertEquals("010-1234-5678", member.getPhoneNumber()),
                () -> assertEquals("서울특별시 강남구 테헤란로 123", member.getAddress())
        );

    }
}
