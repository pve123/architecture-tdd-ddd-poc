package com.example.demo.board.adapter.out.persistence;


import com.example.demo.common.jpa.BaseTimeEntity;
import com.example.demo.member.adapter.out.persistence.MemberJpaEntity;
import com.github.f4b6a3.ulid.UlidCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Schema(description = "게시글 정보 Jpa Entity")
public class BoardJpaEntity extends BaseTimeEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @Schema(description = "ULID 기반 식별자", example = "01JWG8S471E52NTHD6T1G51F6M")
    private String id;
    @Column(nullable = false)
    @Schema(description = "제목", example = "가입인사")
    private String title;
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "내용", example = "가입인사 작성합니다. 만나서 반갑습니다.")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    @Schema(description = "회원 고유번호", example = "01JWG8S476E52NTUD6T1G51F6M")
    private MemberJpaEntity member;

    @Builder(toBuilder = true)
    public BoardJpaEntity(String id, String title, String content, MemberJpaEntity member, LocalDateTime createdDateTime, LocalDateTime lastModifiedDateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    @PrePersist
    public void generateId() {
        if (ObjectUtils.isEmpty(id)) this.id = UlidCreator.getUlid().toString();
    }
}
