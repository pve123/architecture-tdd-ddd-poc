//package com.example.demo.member.adapter.out.persistence;
//
//
//import com.example.demo.common.exception.BusinessException;
//import com.example.demo.config.JpaAuditingConfiguration;
//import com.example.demo.config.QuerydslConfig;
//import com.example.demo.config.TestContainerConfig;
//import com.example.demo.member.domain.GenderEnum;
//import com.example.demo.member.domain.Member;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//@DataJpaTest
//@Import({
//        MemberPersistenceAdapter1323.class,
//        MemberQueryPersistenceAdapter123.class,
//        MemberPersistenceMapperImpl.class,
//        JpaAuditingConfiguration.class,
//        QuerydslConfig.class
//
//})
//@DisplayName("Member JPA Persistence Integration Test")
//public class MemberPersistenceAdapterTest extends TestContainerConfig {
//
//    @Autowired
//    private MemberPersistenceAdapter1323 memberPersistenceAdapter1323;
//    @Autowired
//    private MemberQueryPersistenceAdapter123 memberQueryPersistenceAdapter123;
//    @PersistenceContext
//    private EntityManager entityManager;
//    private Member member;
//    private Member saveMember;
//
//
//    @BeforeEach
//    void setup() {
//        // given
//        member = Member.builder()
//                .email("user@example.com")
//                .password("QWERasdf1234!")
//                .name("홍길동")
//                .gender(GenderEnum.MALE)
//                .phoneNumber("010-1234-5678")
//                .address("서울특별시 강남구 테헤란로 123")
//                .build();
//        // when
//        saveMember = memberPersistenceAdapter1323.save(member);
//    }
//
//    @Test
//    void 회원_저장() {
//        // then
//        assertAll(
//                () -> assertThat(saveMember.getId()).isNotNull(),
//                () -> assertThat(member.getEmail()).isEqualTo(saveMember.getEmail()),
//                () -> assertThat(member.getPassword()).isEqualTo(saveMember.getPassword()),
//                () -> assertThat(member.getName()).isEqualTo(saveMember.getName()),
//                () -> assertThat(member.getGender()).isEqualTo(saveMember.getGender()),
//                () -> assertThat(member.getPhoneNumber()).isEqualTo(saveMember.getPhoneNumber()),
//                () -> assertThat(member.getAddress()).isEqualTo(saveMember.getAddress())
//        );
//    }
//
//    @Test
//    void 회원_조회() {
//
//        // when
//        Member resultMember = memberPersistenceAdapter1323.findById(saveMember.getId());
//
//        // then
//        assertAll(
//                () -> assertThat(resultMember.getId()).isNotNull(),
//                () -> assertThat(member.getEmail()).isEqualTo(resultMember.getEmail()),
//                () -> assertThat(member.getPassword()).isEqualTo(resultMember.getPassword()),
//                () -> assertThat(member.getName()).isEqualTo(resultMember.getName()),
//                () -> assertThat(member.getGender()).isEqualTo(resultMember.getGender()),
//                () -> assertThat(member.getPhoneNumber()).isEqualTo(resultMember.getPhoneNumber()),
//                () -> assertThat(member.getAddress()).isEqualTo(resultMember.getAddress())
//        );
//    }
//
//    @Test
//    void 회원_페이징_목록() {
//
//        // when
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Member> resultMemberList = memberQueryPersistenceAdapter123.findAllByPage(pageable);
//
//        // then
//        assertAll(
//                () -> assertThat(resultMemberList.getContent()).isNotEmpty(),
//                () -> assertThat(resultMemberList.getContent().getFirst().getId()).isNotNull(),
//                () -> assertThat(member.getEmail()).isEqualTo(resultMemberList.getContent().getFirst().getEmail()),
//                () -> assertThat(member.getPassword()).isEqualTo(resultMemberList.getContent().getFirst().getPassword()),
//                () -> assertThat(member.getName()).isEqualTo(resultMemberList.getContent().getFirst().getName()),
//                () -> assertThat(member.getGender()).isEqualTo(resultMemberList.getContent().getFirst().getGender()),
//                () -> assertThat(member.getPhoneNumber()).isEqualTo(resultMemberList.getContent().getFirst().getPhoneNumber()),
//                () -> assertThat(member.getAddress()).isEqualTo(resultMemberList.getContent().getFirst().getAddress())
//        );
//    }
//
//    @Test
//    void 회원_삭제() {
//
//        // when
//        memberPersistenceAdapter1323.softDeleteById(saveMember.getId());
//        entityManager.flush();
//        entityManager.clear();
//        // then
//        assertAll(
//                () -> assertThatThrownBy(() -> memberPersistenceAdapter1323.findById(saveMember.getId()))
//                        .isInstanceOf(BusinessException.class)
//        );
//    }
//
//    @Test
//    void 회원_수정() {
//
//        // when
//
//        Member updateMember = Member.builder()
//                .id(saveMember.getId())
//                .email("user333@example.com")
//                .phoneNumber("010-1234-3333")
//                .address("서울특별시 강남구 테헤란로 3333")
//                .build();
//
//        memberPersistenceAdapter1323.update(updateMember);
//        entityManager.flush();
//        entityManager.clear();
//
//        Member resultMember = memberPersistenceAdapter1323.findById(updateMember.getId());
//
//        // then
//        assertAll(
//                () -> assertThat(resultMember.getId()).isNotNull(),
//                () -> assertThat(resultMember.getId()).isEqualTo(updateMember.getId()),
//                () -> assertThat(resultMember.getEmail()).isEqualTo(updateMember.getEmail()),
//                () -> assertThat(resultMember.getPhoneNumber()).isEqualTo(updateMember.getPhoneNumber()),
//                () -> assertThat(resultMember.getAddress()).isEqualTo(updateMember.getAddress())
//        );
//    }
//}
