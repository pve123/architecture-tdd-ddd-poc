package io.github.pve123.hexagonal.board.domain;

import com.github.f4b6a3.ulid.UlidCreator;
import io.github.pve123.hexagonal.common.error.BusinessException;

import java.time.LocalDateTime;

/**
 * 게시글 도메인 모델입니다. 스프링과 JPA를 모르는 순수 자바 클래스입니다.
 *
 * <p>글쓴이는 회원 객체가 아니라 회원 ID(memberId)로만 가리킵니다.
 * 게시판이 회원의 내부 구조를 알 필요가 없기 때문입니다.
 */
public class Board {

    private final String id;
    private String title;
    private String content;
    private final String memberId;
    private boolean deleted;
    private LocalDateTime deletedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Board(String id, String title, String content, String memberId, boolean deleted,
                  LocalDateTime deletedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Board write(String memberId, String title, String content) {
        return new Board(UlidCreator.getUlid().toString(), title, content, memberId, false, null, null, null);
    }

    /** 저장소에서 읽은 값으로 게시글을 되살립니다. 어댑터만 씁니다. */
    public static Board restore(String id, String title, String content, String memberId, boolean deleted,
                                LocalDateTime deletedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Board(id, title, content, memberId, deleted, deletedAt, createdAt, updatedAt);
    }

    /** 글쓴이만 고칠 수 있습니다. */
    public void edit(String editorId, String title, String content) {
        checkAuthor(editorId);
        this.title = title;
        this.content = content;
    }

    /** 글쓴이만 지울 수 있습니다. 기록은 지우지 않고 삭제 표시만 남깁니다. */
    public void delete(String requesterId, LocalDateTime now) {
        checkAuthor(requesterId);
        this.deleted = true;
        this.deletedAt = now;
    }

    public boolean isWrittenBy(String memberId) {
        return this.memberId.equals(memberId);
    }

    private void checkAuthor(String memberId) {
        if (!isWrittenBy(memberId)) {
            throw new BusinessException(BoardErrorCode.POST_AUTHOR_MISMATCH);
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getMemberId() {
        return memberId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
