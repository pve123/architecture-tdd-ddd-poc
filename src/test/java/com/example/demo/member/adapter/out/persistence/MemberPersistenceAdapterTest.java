package com.example.demo.member.adapter.out.persistence;


import com.example.demo.common.exception.BusinessException;
import com.example.demo.config.JpaAuditingConfiguration;
import com.example.demo.config.test.TestContainerConfig;
import com.example.demo.member.domain.GenderEnum;
import com.example.demo.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import({MemberPersistenceAdapter.class, MemberPersistenceMapperImpl.class, JpaAuditingConfiguration.class})
@DisplayName("Member JPA Persistence Integration Test")
public class MemberPersistenceAdapterTest extends TestContainerConfig {

    @Autowired
    private MemberPersistenceAdapter memberPersistenceAdapter;

    private Member member;
    private Member saveMember;


    @BeforeEach
    void setup() {
        // given
        member = Member.builder()
                .email("user@example.com")
                .password("QWERasdf1234!")
                .name("홍길동")
                .gender(GenderEnum.MALE)
                .phoneNumber("010-1234-5678")
                .address("서울특별시 강남구 테헤란로 123")
                .build();
        // when
        saveMember = memberPersistenceAdapter.save(member);
    }

    @Test
    void 회원_저장() {
        // then
        assertAll(
                () -> assertThat(saveMember.getId()).isNotNull(),
                () -> assertThat(member.getEmail()).isEqualTo(saveMember.getEmail()),
                () -> assertThat(member.getPassword()).isEqualTo(saveMember.getPassword()),
                () -> assertThat(member.getName()).isEqualTo(saveMember.getName()),
                () -> assertThat(member.getGender()).isEqualTo(saveMember.getGender()),
                () -> assertThat(member.getPhoneNumber()).isEqualTo(saveMember.getPhoneNumber()),
                () -> assertThat(member.getAddress()).isEqualTo(saveMember.getAddress())
        );
    }

    @Test
    void 회원_조회() {

        // when
        Member resultMember = memberPersistenceAdapter.findById(saveMember.getId());

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
    }

    @Test
    void 회원_목록() {

        // when
        List<Member> resultMemberList = memberPersistenceAdapter.findAll();

        // then
        assertAll(
                () -> assertThat(resultMemberList).isNotEmpty(),
                () -> assertThat(resultMemberList.getFirst().getId()).isNotNull(),
                () -> assertThat(member.getEmail()).isEqualTo(resultMemberList.getFirst().getEmail()),
                () -> assertThat(member.getPassword()).isEqualTo(resultMemberList.getFirst().getPassword()),
                () -> assertThat(member.getName()).isEqualTo(resultMemberList.getFirst().getName()),
                () -> assertThat(member.getGender()).isEqualTo(resultMemberList.getFirst().getGender()),
                () -> assertThat(member.getPhoneNumber()).isEqualTo(resultMemberList.getFirst().getPhoneNumber()),
                () -> assertThat(member.getAddress()).isEqualTo(resultMemberList.getFirst().getAddress())
        );
    }

    @Test
    void 회원_삭제() {

        // when
        memberPersistenceAdapter.deleteById(saveMember.getId());

        assertAll(
                () -> assertThatThrownBy(() -> memberPersistenceAdapter.findById(saveMember.getId()))
                        .isInstanceOf(BusinessException.class)
        );
    }
}
