//package com.example.demo.board.adapter.out.persistence;
//
//
//import com.example.demo.board.domain.Board;
//import com.example.demo.common.jpa.BaseTimeEntity;
//import com.example.demo.member.adapter.out.persistence.MemberJpaEntity;
//import com.github.f4b6a3.ulid.UlidCreator;
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.apache.commons.lang3.ObjectUtils;
//import org.hibernate.annotations.Where;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "board")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Where(clause = "is_deleted = false")
//@Schema(description = "게시글 정보 Jpa Entity")
//public class BoardJpaEntity extends BaseTimeEntity {
//
//    @Id
//    @Column(nullable = false, updatable = false)
//    @Schema(description = "ULID 기반 식별자", example = "01JWG8S471E52NTHD6T1G51F6M")
//    private String id;
//    @Column(nullable = false)
//    @Schema(description = "제목", example = "가입인사")
//    private String title;
//    @Lob
//    @Column(nullable = false, columnDefinition = "TEXT")
//    @Schema(description = "내용", example = "가입인사 작성합니다. 만나서 반갑습니다.")
//    private String content;
//    @Column(nullable = false)
//    @Schema(description = "삭제유무", example = "true", allowableValues = {"true", "false"})
//    private Boolean isDeleted;
//    @Column
//    @Schema(description = "삭제일시", example = "2025-06-11T08:35:53.970Z")
//    private LocalDateTime deletedAt;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", nullable = false, updatable = false)
//    @Schema(description = "회원 고유번호", example = "01JWG8S476E52NTUD6T1G51F6M")
//    private MemberJpaEntity member;
//
//    @Builder(toBuilder = true)
//    public BoardJpaEntity(String id,
//                          String title,
//                          String content,
//                          Boolean isDeleted,
//                          LocalDateTime deletedAt,
//                          MemberJpaEntity member,
//                          LocalDateTime createdAt,
//                          LocalDateTime updatedAt) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//        this.isDeleted = isDeleted;
//        this.deletedAt = deletedAt;
//        this.member = member;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }
//
//    @PrePersist
//    public void generateId() {
//        if (ObjectUtils.isEmpty(id)) this.id = UlidCreator.getUlid().toString();
//        if (ObjectUtils.isEmpty(isDeleted)) this.isDeleted = Boolean.FALSE;
//    }
//
//    public void update(Board board) {
//        this.title = board.getTitle();
//        this.content = board.getContent();
//    }
//}
