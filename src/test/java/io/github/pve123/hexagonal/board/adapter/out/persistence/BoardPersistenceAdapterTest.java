package io.github.pve123.hexagonal.board.adapter.out.persistence;

import io.github.pve123.hexagonal.board.domain.Board;
import io.github.pve123.hexagonal.common.config.JpaConfig;
import io.github.pve123.hexagonal.config.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BoardPersistenceAdapter.class, JpaConfig.class})
class BoardPersistenceAdapterTest extends TestContainerConfig {

    private static final String MEMBER_ID = "01WRITER000000000000000000";

    @Autowired
    private BoardPersistenceAdapter adapter;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /** board.member_id에 외래 키가 있어서 글쓴이 행을 먼저 넣어 둡니다. */
    @BeforeEach
    void insertWriter() {
        jdbcTemplate.update("""
                INSERT INTO member (id, email, password, name, gender, phone_number, address, is_deleted, created_at, updated_at)
                VALUES (?, 'writer@example.com', 'pw', '홍길동', 'MALE', '010-1234-5678', '서울특별시 강남구', 0, NOW(), NOW())
                """, MEMBER_ID);
    }

    @Test
    void 저장한_글을_다시_읽을_수_있다() {
        Board saved = adapter.save(Board.write(MEMBER_ID, "제목", "내용"));

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(adapter.findActiveById(saved.getId())).get()
                .extracting(Board::getTitle)
                .isEqualTo("제목");
    }

    @Test
    void 고친_내용을_저장한다() {
        Board saved = adapter.save(Board.write(MEMBER_ID, "제목", "내용"));

        saved.edit(MEMBER_ID, "바꾼 제목", "바꾼 내용");
        adapter.save(saved);

        assertThat(adapter.findActiveById(saved.getId()).orElseThrow().getTitle()).isEqualTo("바꾼 제목");
    }

    @Test
    void 지운_글은_조회와_목록과_개수에서_빠진다() {
        adapter.save(Board.write(MEMBER_ID, "남을 글", "내용"));
        Board deleted = adapter.save(Board.write(MEMBER_ID, "지울 글", "내용"));
        deleted.delete(MEMBER_ID, LocalDateTime.of(2026, 1, 1, 12, 0));
        adapter.save(deleted);

        assertThat(adapter.findActiveById(deleted.getId())).isEmpty();
        assertThat(adapter.search(PageRequest.of(0, 1)).getTotalElements()).isEqualTo(1);
    }
}
