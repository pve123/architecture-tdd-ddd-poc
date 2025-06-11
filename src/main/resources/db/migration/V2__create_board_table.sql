CREATE TABLE board
(
    id         VARCHAR(26)  NOT NULL PRIMARY KEY COMMENT 'ULID 기반 식별자',
    title      VARCHAR(255) NOT NULL COMMENT '제목',
    content    TEXT         NOT NULL COMMENT '내용',
    member_id  VARCHAR(26)  NOT NULL COMMENT '사용자 고유 번호',
    is_deleted TINYINT(1)   NOT NULL COMMENT '삭제 유무',
    deleted_at DATETIME     NULL COMMENT '삭제 일시',
    created_at DATETIME     NOT NULL COMMENT '생성 일시',
    updated_at DATETIME     NOT NULL COMMENT '수정 일시',

    CONSTRAINT fk_board_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);