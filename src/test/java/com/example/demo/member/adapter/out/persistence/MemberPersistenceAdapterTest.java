package com.example.demo.member.adapter.out.persistence;


import com.example.demo.config.test.TestContainerConfig;
import com.example.demo.member.domain.GenderEnum;
import com.example.demo.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({MemberPersistenceAdapter.class, MemberPersistenceMapperImpl.class})
@DisplayName("Member JPA Persistence Integration Test")
public class MemberPersistenceAdapterTest extends TestContainerConfig {

    @Autowired
    private MemberPersistenceAdapter memberPersistenceAdapter;

    @Test
    void 회원_저장() {
        // given
        Member member = Member.builder()
                .email("user@example.com")
                .password("QWERasdf1234!")
                .name("홍길동")
                .gender(GenderEnum.MALE)
                .phoneNumber("010-1234-5678")
                .address("서울특별시 강남구 테헤란로 123")
                .build();

        // when
        Member result = memberPersistenceAdapter.save(member);

        // then
        assertAll(
                () -> assertNotNull(result.getId()),
                () -> assertEquals(member.getEmail(), result.getEmail()),
                () -> assertEquals(member.getPassword(), result.getPassword()),
                () -> assertEquals(member.getName(), result.getName()),
                () -> assertEquals(member.getGender(), result.getGender()),
                () -> assertEquals(member.getPhoneNumber(), result.getPhoneNumber()),
                () -> assertEquals(member.getAddress(), result.getAddress())
        );

    }
}
