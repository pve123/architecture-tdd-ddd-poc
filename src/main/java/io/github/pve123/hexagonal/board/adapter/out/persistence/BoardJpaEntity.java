package io.github.pve123.hexagonal.board.adapter.out.persistence;

import io.github.pve123.hexagonal.board.domain.Board;
import io.github.pve123.hexagonal.common.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

/**
 * board 테이블과 1:1로 맞춘 저장용 클래스입니다.
 * 회원 엔티티를 직접 참조하지 않고 member_id 값만 들고 있습니다.
 */
@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class BoardJpaEntity extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(nullable = false, updatable = false, length = 26)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false, length = 26)
    private String memberId;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedAt;

    static BoardJpaEntity from(Board board) {
        BoardJpaEntity entity = new BoardJpaEntity();
        entity.id = board.getId();
        entity.memberId = board.getMemberId();
        entity.apply(board);
        return entity;
    }

    /** 도메인에서 바뀔 수 있는 값만 옮겨 담습니다. */
    void apply(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.isDeleted = board.isDeleted();
        this.deletedAt = board.getDeletedAt();
    }

    Board toDomain() {
        return Board.restore(id, title, content, memberId, isDeleted, deletedAt, createdAt, updatedAt);
    }

    @Override
    public String getId() {
        return id;
    }

    /** ID를 도메인이 미리 만들어 오므로, 생성 시각이 없으면 새 글로 봅니다. */
    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
