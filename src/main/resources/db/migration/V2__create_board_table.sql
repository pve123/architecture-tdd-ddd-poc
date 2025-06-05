CREATE TABLE board
(
    id                      VARCHAR(26)  NOT NULL PRIMARY KEY COMMENT 'ULID 기반 식별자',
    title                   VARCHAR(255) NOT NULL COMMENT '제목',
    content                 TEXT         NOT NULL COMMENT '내용',
    member_id               VARCHAR(26)  NOT NULL COMMENT '사용자 고유 번호',
    created_date_time       DATETIME     NOT NULL COMMENT '생성 시각',
    last_modified_date_time DATETIME     NOT NULL COMMENT '최종 수정 시각',

    CONSTRAINT fk_board_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);