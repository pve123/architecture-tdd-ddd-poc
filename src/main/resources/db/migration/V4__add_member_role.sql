-- 관리자 API를 열면서 회원 권한을 둔다. 기존 회원은 모두 일반 회원(USER)이 된다.
ALTER TABLE member
    ADD COLUMN role VARCHAR(10) NOT NULL DEFAULT 'USER' COMMENT '권한' AFTER address;

ALTER TABLE member
    ADD CONSTRAINT chk_role CHECK (role IN ('USER', 'ADMIN'));
