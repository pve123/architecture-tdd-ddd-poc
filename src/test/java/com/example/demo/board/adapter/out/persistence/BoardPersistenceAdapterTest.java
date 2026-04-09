package com.example.demo.board.adapter.out.persistence;

import com.example.demo.board.domain.Board;
import com.example.demo.config.JpaAuditingConfiguration;
import com.example.demo.config.QuerydslConfig;
import com.example.demo.config.TestContainerConfig;
import com.example.demo.member.adapter.out.persistence.MemberOrderSpecifierFactory;
import com.example.demo.member.adapter.out.persistence.MemberPersistenceAdapter;
import com.example.demo.member.adapter.out.persistence.MemberPersistenceMapperImpl;
import com.example.demo.member.adapter.out.persistence.MemberSearchPredicateFactory;
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
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import({
        BoardPersistenceAdapter.class,
        BoardPersistenceMapperImpl.class,
        MemberPersistenceAdapter.class,
        MemberPersistenceMapperImpl.class,
        MemberOrderSpecifierFactory.class,
        MemberSearchPredicateFactory.class,
        JpaAuditingConfiguration.class,
        QuerydslConfig.class
})
@DisplayName("Board JPA Persistence Integration Test")
class BoardPersistenceAdapterTest extends TestContainerConfig {

    @Autowired
    private MemberPersistenceAdapter memberPersistenceAdapter;

    @Autowired
    private BoardPersistenceAdapter boardPersistenceAdapter;

    @PersistenceContext
    private EntityManager entityManager;

    private Member savedMember;
    private Board savedBoard;

    @BeforeEach
    void setup() {
        Member member = Member.builder()
                .email("user@example.com")
                .password("QWERasdf1234!")
                .name("홍길동")
                .gender(GenderEnum.MALE)
                .phoneNumber("010-1234-5678")
                .address("서울특별시 강남구 테헤란로 123")
                .build();
        savedMember = memberPersistenceAdapter.save(member);

        Board board = Board.builder()
                .title("제목 테스트 중입니다.")
                .content("내용 테스트 중입니다.")
                .memberId(savedMember.getId())
                .build();
        savedBoard = boardPersistenceAdapter.save(board);
    }

    @Test
    @DisplayName("게시글 저장 성공")
    void 게시글_저장() {
        assertAll(
                () -> assertThat(savedBoard.getId()).isNotBlank(),
                () -> assertThat(savedBoard.getTitle()).isEqualTo("제목 테스트 중입니다."),
                () -> assertThat(savedBoard.getContent()).isEqualTo("내용 테스트 중입니다."),
                () -> assertThat(savedBoard.getMemberId()).isEqualTo(savedMember.getId())
        );
    }

    @Test
    @DisplayName("게시글 단건 조회 성공")
    void 게시글_조회() {
        Board resultBoard = boardPersistenceAdapter.findById(savedBoard.getId());

        assertAll(
                () -> assertThat(resultBoard.getId()).isEqualTo(savedBoard.getId()),
                () -> assertThat(resultBoard.getTitle()).isEqualTo(savedBoard.getTitle()),
                () -> assertThat(resultBoard.getContent()).isEqualTo(savedBoard.getContent()),
                () -> assertThat(resultBoard.getMemberId()).isEqualTo(savedBoard.getMemberId())
        );
    }

    @Test
    @DisplayName("게시글 페이징 목록 조회 성공")
    void 게시글_페이징_목록() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Board> resultBoardList = boardPersistenceAdapter.searchBoards(pageable);

        assertAll(
                () -> assertThat(resultBoardList.getContent()).isNotEmpty(),
                () -> assertThat(resultBoardList.getNumber()).isEqualTo(0),
                () -> assertThat(resultBoardList.getSize()).isEqualTo(10),
                () -> assertThat(resultBoardList.getTotalElements()).isGreaterThanOrEqualTo(1)
        );

        Board firstBoard = resultBoardList.getContent().getFirst();
        assertAll(
                () -> assertThat(firstBoard.getTitle()).isEqualTo(savedBoard.getTitle()),
                () -> assertThat(firstBoard.getContent()).isEqualTo(savedBoard.getContent()),
                () -> assertThat(firstBoard.getMemberId()).isEqualTo(savedBoard.getMemberId())
        );
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void 게시글_수정() {
        Board updateBoard = Board.builder()
                .id(savedBoard.getId())
                .title("수정된 제목")
                .content("수정된 내용")
                .memberId(savedMember.getId())
                .build();

        boardPersistenceAdapter.update(updateBoard);
        entityManager.flush();
        entityManager.clear();

        Board resultBoard = boardPersistenceAdapter.findById(updateBoard.getId());

        assertAll(
                () -> assertThat(resultBoard.getId()).isEqualTo(updateBoard.getId()),
                () -> assertThat(resultBoard.getTitle()).isEqualTo(updateBoard.getTitle()),
                () -> assertThat(resultBoard.getContent()).isEqualTo(updateBoard.getContent()),
                () -> assertThat(resultBoard.getMemberId()).isEqualTo(updateBoard.getMemberId())
        );
    }

    @Test
    @DisplayName("게시글 삭제(Soft Delete) 성공")
    void 게시글_삭제() {
        boardPersistenceAdapter.softDeleteById(savedBoard.getId());
        entityManager.flush();
        entityManager.clear();

        Page<Board> resultBoardList = boardPersistenceAdapter.searchBoards(PageRequest.of(0, 10));

        assertThat(resultBoardList.getContent())
                .noneMatch(item -> item.getId().equals(savedBoard.getId()));
    }
}
