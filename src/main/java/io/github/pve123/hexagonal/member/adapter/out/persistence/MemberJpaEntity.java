package io.github.pve123.hexagonal.member.adapter.out.persistence;

import io.github.pve123.hexagonal.common.jpa.BaseTimeEntity;
import io.github.pve123.hexagonal.member.domain.Gender;
import io.github.pve123.hexagonal.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

/**
 * member 테이블과 1:1로 맞춘 저장용 클래스입니다. 도메인 모델(Member)과 따로 둬서,
 * 테이블이 바뀌어도 도메인은 영향을 받지 않습니다.
 */
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class MemberJpaEntity extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(nullable = false, updatable = false, length = 26)
    private String id;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(nullable = false, length = 128)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 10)
    private Gender gender;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedAt;

    static MemberJpaEntity from(Member member) {
        MemberJpaEntity entity = new MemberJpaEntity();
        entity.id = member.getId();
        entity.password = member.getPassword();
        entity.name = member.getName();
        entity.gender = member.getGender();
        entity.apply(member);
        return entity;
    }

    /** 도메인에서 바뀔 수 있는 값만 옮겨 담습니다. */
    void apply(Member member) {
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.address = member.getAddress();
        this.isDeleted = member.isDeleted();
        this.deletedAt = member.getDeletedAt();
    }

    Member toDomain() {
        return Member.restore(id, email, password, name, gender, phoneNumber, address,
                isDeleted, deletedAt, createdAt, updatedAt);
    }

    @Override
    public String getId() {
        return id;
    }

    /** ID를 도메인이 미리 만들어 오므로, 생성 시각이 없으면 새 회원으로 봅니다. 그래야 save가 SELECT 없이 INSERT합니다. */
    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
