CREATE TABLE member
(
    id           VARCHAR(26)  NOT NULL PRIMARY KEY COMMENT 'ULID 기반 식별자',
    email        VARCHAR(254) NOT NULL COMMENT '이메일 (RFC 5321 표준)',
    password     VARCHAR(128) NOT NULL COMMENT '비밀번호',
    name         VARCHAR(50)  NOT NULL COMMENT '회원 이름',
    gender       VARCHAR(10)  NOT NULL COMMENT '성별',
    phone_number VARCHAR(20)  NOT NULL COMMENT '전화번호',
    address      VARCHAR(200) NOT NULL COMMENT '주소',
    is_deleted   TINYINT(1)   NOT NULL COMMENT '삭제 유무',
    deleted_at   DATETIME     NULL COMMENT '삭제 일시',
    created_at   DATETIME     NOT NULL COMMENT '생성 일시',
    updated_at   DATETIME     NOT NULL COMMENT '수정 일시',


    CONSTRAINT chk_gender CHECK (gender IN ('MALE', 'FEMALE'))
);


ALTER TABLE member
    ADD CONSTRAINT uq_member_email UNIQUE (email);