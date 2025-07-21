package com.example.demo.board.adapter.out.persistence;


import com.example.demo.board.domain.Board;
import com.example.demo.config.JpaAuditingConfiguration;
import com.example.demo.config.QuerydslConfig;
import com.example.demo.config.TestContainerConfig;
import com.example.demo.member.adapter.out.persistence.MemberPersistenceAdapter;
import com.example.demo.member.adapter.out.persistence.MemberPersistenceMapperImpl;
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
        BoardQueryPersistenceAdapter.class,
        BoardPersistenceMapperImpl.class,
        MemberPersistenceAdapter.class,
        MemberPersistenceMapperImpl.class,
        JpaAuditingConfiguration.class,
        QuerydslConfig.class
})
@DisplayName("Board JPA Persistence Integration Test")
public class BoardPersistenceAdapterTest extends TestContainerConfig {

    @Autowired
    private MemberPersistenceAdapter memberPersistenceAdapter;
    @Autowired
    private BoardPersistenceAdapter boardPersistenceAdapter;
    @Autowired
    private BoardQueryPersistenceAdapter boardQueryPersistenceAdapter;
    @PersistenceContext
    private EntityManager entityManager;
    private Member member;
    private Member saveMember;
    private Board board;
    private Board saveBoard;


    @BeforeEach
    void setup() {
        //given
        member = Member.builder()
                .email("user@example.com")
                .password("QWERasdf1234!")
                .name("홍길동")
                .gender(GenderEnum.MALE)
                .phoneNumber("010-1234-5678")
                .address("서울특별시 강남구 테헤란로 123")
                .build();
        saveMember = memberPersistenceAdapter.save(member);

        entityManager.flush();

        board = Board.builder()
                .title("제목 테스트 중입니다.")
                .content("내용 테스트 중입니다.")
                .member(saveMember)
                .build();
        //then
        saveBoard = boardPersistenceAdapter.save(board);
    }

    @Test
    void 게시물_저장() {
        // then
        assertAll(
                () -> assertThat(saveBoard.getId()).isNotNull(),
                () -> assertThat(board.getTitle()).isEqualTo(saveBoard.getTitle()),
                () -> assertThat(board.getContent()).isEqualTo(saveBoard.getContent()),
                () -> assertThat(board.getMember().getId()).isEqualTo(saveBoard.getMember().getId())
        );
    }

    @Test
    void 게시물_조회() {
        //when
        Board resultBoard = boardPersistenceAdapter.findById(saveBoard.getId());
        // then
        assertAll(
                () -> assertThat(resultBoard.getId()).isNotNull(),
                () -> assertThat(saveBoard.getTitle()).isEqualTo(resultBoard.getTitle()),
                () -> assertThat(saveBoard.getContent()).isEqualTo(resultBoard.getContent()),
                () -> assertThat(saveBoard.getMember().getId()).isEqualTo(resultBoard.getMember().getId())
        );
    }

    @Test
    void 게시물_페이징_목록() {
        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> resultBoardList = boardQueryPersistenceAdapter.findAllByPage(pageable);
        // then
        assertAll(
                () -> assertThat(resultBoardList.getContent()).isNotEmpty(),
                () -> assertThat(resultBoardList.getContent().getFirst().getId()).isNotNull(),
                () -> assertThat(saveBoard.getTitle()).isEqualTo(resultBoardList.getContent().getFirst().getTitle()),
                () -> assertThat(saveBoard.getContent()).isEqualTo(resultBoardList.getContent().getFirst().getContent()),
                () -> assertThat(saveBoard.getMember().getId()).isEqualTo(resultBoardList.getContent().getFirst().getMember().getId())
        );
    }

    @Test
    void 게시물_수정() {

        //when
        Board updateBoard = Board.builder()
                .id(saveBoard.getId())
                .title("제목 테스트 중입니다.@@@@@")
                .content("내용 테스트 중입니다.@@@@@")
                .member(saveMember)
                .build();

        Board resultBoard = boardPersistenceAdapter.update(updateBoard);
        entityManager.flush();
        entityManager.clear();
        // then
        assertAll(
                () -> assertThat(updateBoard.getId()).isNotNull(),
                () -> assertThat(resultBoard.getTitle()).isEqualTo(updateBoard.getTitle()),
                () -> assertThat(resultBoard.getContent()).isEqualTo(updateBoard.getContent()),
                () -> assertThat(resultBoard.getMember().getId()).isEqualTo(updateBoard.getMember().getId())
        );
    }

}
