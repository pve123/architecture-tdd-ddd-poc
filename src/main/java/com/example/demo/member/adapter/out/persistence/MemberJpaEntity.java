package com.example.demo.member.adapter.out.persistence;

import com.example.demo.board.adapter.out.persistence.BoardJpaEntity;
import com.example.demo.common.jpa.BaseTimeEntity;
import com.example.demo.member.domain.GenderEnum;
import com.github.f4b6a3.ulid.UlidCreator;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Schema(description = "회원 정보 Jpa Entity")
public class MemberJpaEntity extends BaseTimeEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @Schema(description = "ULID 기반 식별자", example = "01HZY74JZP5VDFKHX6D5YFRAZW")
    private String id;
    @Column(nullable = false, updatable = false, length = 100)
    @Schema(description = "이메일", example = "user@example.com")
    private String email;
    @Column(nullable = false)
    @Schema(description = "비밀번호", example = "qwer123456789!")
    private String password;
    @Column(nullable = false, length = 25)
    @Schema(description = "회원 이름", example = "홍길동")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 25)
    @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE"})
    private GenderEnum gender;
    @Column(nullable = false, length = 20)
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;
    @Column(nullable = false, length = 200)
    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardJpaEntity> boardList = Lists.newArrayList();

    @Builder(toBuilder = true)
    public MemberJpaEntity(String id, String email, String password, String name, GenderEnum gender, String phoneNumber, String address, List<BoardJpaEntity> boardList, LocalDateTime createdDateTime, LocalDateTime lastModifiedDateTime) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.boardList = boardList;
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    @PrePersist
    public void generateId() {
        if (ObjectUtils.isEmpty(id)) this.id = UlidCreator.getUlid().toString();
    }
}