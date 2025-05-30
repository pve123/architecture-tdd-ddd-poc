CREATE TABLE member
(
    id                      VARCHAR(26)  NOT NULL PRIMARY KEY COMMENT 'ULID 기반 식별자',
    email                   VARCHAR(254) NOT NULL COMMENT '이메일 (RFC 5321 표준)',
    password                VARCHAR(128) NOT NULL COMMENT '비밀번호',
    name                    VARCHAR(50)  NOT NULL COMMENT '회원 이름',
    gender                  VARCHAR(10)  NOT NULL COMMENT '성별',
    phone_number            VARCHAR(20)  NOT NULL COMMENT '전화번호',
    address                 VARCHAR(200) NOT NULL COMMENT '주소',
    created_date_time       DATETIME     NOT NULL COMMENT '생성 시각',
    last_modified_date_time DATETIME     NOT NULL COMMENT '최종 수정 시각',


    CONSTRAINT chk_gender CHECK (gender IN ('MALE', 'FEMALE'))
);


ALTER TABLE member
    ADD CONSTRAINT uq_member_email UNIQUE (email);