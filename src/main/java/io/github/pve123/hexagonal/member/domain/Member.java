package io.github.pve123.hexagonal.member.domain;

import com.github.f4b6a3.ulid.UlidCreator;
import io.github.pve123.hexagonal.common.error.BusinessException;

import java.time.LocalDateTime;

/**
 * 회원 도메인 모델입니다. 스프링과 JPA를 모르는 순수 자바 클래스입니다.
 *
 * <p>회원이 지켜야 할 규칙(탈퇴하면 다시 탈퇴할 수 없다, 탈퇴한 회원은 활동할 수 없다)은 여기서 지킵니다.
 * 비밀번호는 암호화된 값만 받습니다.
 */
public class Member {

    private final String id;
    private String email;
    private final String password;
    private final String name;
    private final Gender gender;
    private String phoneNumber;
    private String address;
    private boolean deleted;
    private LocalDateTime deletedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Member(String id, String email, String password, String name, Gender gender,
                   String phoneNumber, String address, boolean deleted, LocalDateTime deletedAt,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** 새 회원을 만듭니다. ID는 시간 순서로 정렬되는 ULID입니다. */
    public static Member register(String email, String encodedPassword, String name, Gender gender,
                                  String phoneNumber, String address) {
        return new Member(UlidCreator.getUlid().toString(), email, encodedPassword, name, gender,
                phoneNumber, address, false, null, null, null);
    }

    /** 저장소에서 읽은 값으로 회원을 되살립니다. 어댑터만 씁니다. */
    public static Member restore(String id, String email, String password, String name, Gender gender,
                                 String phoneNumber, String address, boolean deleted, LocalDateTime deletedAt,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Member(id, email, password, name, gender, phoneNumber, address,
                deleted, deletedAt, createdAt, updatedAt);
    }

    /** 연락처를 바꿉니다. 비어 있는 값은 바꾸지 않고 그대로 둡니다. */
    public void changeContact(String email, String phoneNumber, String address) {
        checkActive();
        if (email != null) {
            this.email = email;
        }
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
        if (address != null) {
            this.address = address;
        }
    }

    /** 탈퇴합니다. 기록은 지우지 않고 탈퇴 표시만 남깁니다. */
    public void withdraw(LocalDateTime now) {
        checkActive();
        this.deleted = true;
        this.deletedAt = now;
    }

    /** 탈퇴한 회원이면 예외를 던집니다. 글쓰기처럼 활동하기 전에 확인합니다. */
    public void checkActive() {
        if (deleted) {
            throw new BusinessException(MemberErrorCode.MEMBER_WITHDRAWN);
        }
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
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
