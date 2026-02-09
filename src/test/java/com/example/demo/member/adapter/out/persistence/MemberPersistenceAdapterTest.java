package com.example.demo.member.adapter.out.persistence;


import com.example.demo.common.exception.BusinessException;
import com.example.demo.config.JpaAuditingConfiguration;
import com.example.demo.config.QuerydslConfig;
import com.example.demo.config.TestContainerConfig;
import com.example.demo.member.adapter.in.web.request.MemberSearchRequest;
import com.example.demo.member.domain.GenderEnum;
import com.example.demo.member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import({
        MemberPersistenceAdapter.class,
        MemberPersistenceMapperImpl.class,
        JpaAuditingConfiguration.class,
        QuerydslConfig.class,
        MemberOrderSpecifierFactory.class,
        MemberSearchPredicateFactory.class
})
@DisplayName("Member JPA Persistence Integration Test")
public class MemberPersistenceAdapterTest extends TestContainerConfig {

    @Autowired
    private MemberPersistenceAdapter memberPersistenceAdapter;
    @PersistenceContext
    private EntityManager entityManager;
    private Member member;
    private Member savedMember;


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
        savedMember = memberPersistenceAdapter.save(member);
        // flush/clear는 각 테스트에서 필요할 때만 호출
    }

    @Test
    @DisplayName("회원 페이징 목록 조회 성공")
    void 회원_페이징_목록() {

        // when
        Pageable pageable = PageRequest.of(0, 10);
        MemberSearchRequest searchRequest = new MemberSearchRequest(
                "user@example.com",
                "홍길동",
                "MALE"
        );
        Page<Member> resultMemberList = memberPersistenceAdapter.searchMembers(pageable, searchRequest);

        // then
        assertAll(
                () -> assertThat(resultMemberList.getContent()).isNotEmpty(),
                () -> assertThat(resultMemberList.getNumber()).isEqualTo(0),
                () -> assertThat(resultMemberList.getSize()).isEqualTo(10),
                () -> assertThat(resultMemberList.getTotalElements()).isGreaterThanOrEqualTo(1)
        );
        Member firstMember = resultMemberList.getContent().getFirst();
        assertMemberEquals(firstMember, savedMember);
    }


    @Test
    @DisplayName("회원 단건 조회 성공")
    void 회원_조회() {

        // when
        Member resultMember = memberPersistenceAdapter.findById(savedMember.getId());

        // then
        assertThat(resultMember.getId()).isNotNull();
        assertMemberEquals(resultMember, savedMember);
    }


    @Test
    @DisplayName("회원 저장 성공")
    void 회원_저장() {
        // then
        assertThat(savedMember.getId()).isNotNull();
        assertMemberEquals(member, savedMember);
    }


    @Test
    @DisplayName("회원 수정 성공")
    void 회원_수정() {

        // when

        Member updateMember = Member.builder()
                .id(savedMember.getId())
                .email("usertest@example.com")
                .phoneNumber("010-1234-3333")
                .address("서울특별시 강남구 테헤란로 3333")
                .build();

        memberPersistenceAdapter.update(updateMember);
        entityManager.flush();
        entityManager.clear();

        Member resultMember = memberPersistenceAdapter.findById(updateMember.getId());

        // then
        // 변경된 필드 검증
        assertAll(
                () -> assertThat(resultMember.getId()).isEqualTo(updateMember.getId()),
                () -> assertThat(resultMember.getEmail()).isEqualTo(updateMember.getEmail()),
                () -> assertThat(resultMember.getPhoneNumber()).isEqualTo(updateMember.getPhoneNumber()),
                () -> assertThat(resultMember.getAddress()).isEqualTo(updateMember.getAddress())
        );
    }


    @Test
    @DisplayName("회원 삭제(Soft Delete) 성공")
    void 회원_삭제() {

        // when
        memberPersistenceAdapter.softDeleteById(savedMember.getId());
        entityManager.flush();
        entityManager.clear();
        // then
        // 삭제된 회원 조회 시 예외 발생
        assertAll(
                () -> assertThatThrownBy(() -> memberPersistenceAdapter.findById(savedMember.getId()))
                        .isInstanceOf(BusinessException.class)
        );
    }


    /**
     * 도메인 Member 필드 비교 헬퍼
     * 통합 테스트에서 중복되는 필드 비교를 한 곳에 모아서 관리
     */
    private void assertMemberEquals(Member actual, Member expected) {
        assertAll(
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getGender()).isEqualTo(expected.getGender()),
                () -> assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber()),
                () -> assertThat(actual.getAddress()).isEqualTo(expected.getAddress())
        );
    }
}
