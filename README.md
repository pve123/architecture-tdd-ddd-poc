# architecture-tdd-ddd-poc

회원(Member)과 게시글(Board) 도메인을 대상으로 **TDD, DDD, CQRS, Hexagonal Architecture**를 함께 연습하기 위한 Spring Boot 기반 PoC 프로젝트입니다.

기본적인 CRUD를 넘어서 다음과 같은 관심사를 한 저장소에서 같이 다룹니다.

- 도메인 중심 계층 분리
- 입력/출력 포트 기반 애플리케이션 서비스 구성
- JPA + QueryDSL 기반 조회/영속화 분리
- Flyway 기반 스키마 및 더미 데이터 관리
- Swagger(OpenAPI) 기반 API 문서화
- Testcontainers 기반 통합 테스트

---

## 1. Tech Stack

- **Language / Runtime**: Java 21, Kotlin stdlib 일부 사용
- **Framework**: Spring Boot 3.5.8
- **Build**: Gradle
- **Persistence**: Spring Data JPA, QueryDSL, MariaDB
- **Migration**: Flyway
- **API Docs**: springdoc-openapi (Swagger UI)
- **Mapping**: MapStruct
- **Security**: Spring Security
- **Test**: JUnit 5, Spring Boot Test, MockMvc, Testcontainers
- **ID Strategy**: ULID

---

## 2. Architecture Overview

이 프로젝트는 전형적인 레이어드 구조보다, 다음과 같은 **포트/어댑터 중심 구조**를 지향합니다.

```text
adapter/in/web        -> HTTP 요청/응답, Controller, Request/Response DTO
application/port/in   -> 유스케이스 인터페이스
application/service   -> 유스케이스 구현
application/port/out  -> 외부 의존성 인터페이스
adapter/out/persistence -> JPA/QueryDSL 기반 영속성 구현
domain                -> 순수 도메인 모델
```

### 핵심 적용 개념

- **DDD**: `member`, `board` 도메인을 분리해 모델과 유스케이스를 구성합니다.
- **Hexagonal Architecture**: 웹/DB는 어댑터에 위치하고, 애플리케이션 서비스는 포트를 통해서만 외부와 통신합니다.
- **CQRS**: 조회와 변경 책임을 포트 단위로 분리합니다.
- **TDD 친화 구조**: 서비스 단위 테스트와 컨트롤러/영속성 통합 테스트를 함께 둘 수 있도록 구성되어 있습니다.

---

## 3. Domain

### Member

회원 도메인은 다음 정보를 관리합니다.

- `id` (ULID)
- `email`
- `password` (저장 시 BCrypt 인코딩)
- `name`
- `gender` (`MALE`, `FEMALE`)
- `phoneNumber`
- `address`
- 소프트 삭제 필드 (`isDeleted`, `deletedAt`)
- 생성/수정 시각

### Board

게시글 도메인은 다음 정보를 관리합니다.

- `id` (ULID)
- `title`
- `content`
- `memberId` (작성자 회원 ID)
- 소프트 삭제 필드 (`isDeleted`, `deletedAt`)
- 생성/수정 시각

---

## 4. Features

### Member API

- 회원 생성
- 회원 단건 조회
- 회원 페이징 목록 조회
- 회원 수정
- 회원 삭제 (soft delete)
- 조건 기반 회원 검색 지원 (`MemberSearchRequest`)

### Board API

- 게시글 생성
- 게시글 단건 조회
- 게시글 페이징 목록 조회
- 게시글 수정
- 게시글 삭제 (soft delete)
- 게시글 응답에 작성자 정보 조합

---

## 5. Project Structure

```text
src/main/java/com/example/demo
├── board
│   ├── adapter
│   │   ├── in/web
│   │   └── out/persistence
│   ├── application
│   │   ├── port/in
│   │   ├── port/out
│   │   └── service
│   └── domain
├── member
│   ├── adapter
│   │   ├── in/web
│   │   └── out/persistence
│   ├── application
│   │   ├── port/in
│   │   ├── port/out
│   │   └── service
│   └── domain
├── common
│   ├── exception
│   ├── jpa
│   └── response
└── config
```

---

## 6. Running the Application

### Prerequisites

- JDK 21
- Docker (통합 테스트 실행 시 필요)
- MariaDB 로컬 인스턴스 (로컬 프로필 실행 시 필요)

### 6.1 Local profile 설정

로컬 실행 시 `application-db.yml`의 `local` 프로필 설정을 사용합니다.

기본값:

- DB URL: `jdbc:mariadb://localhost:3306/local?serverTimezone=Asia/Seoul`
- Port: `8099`

애플리케이션 실행 예시:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

또는:

```bash
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

> `application.yml`에서 `spring.profiles.active`는 외부 환경값으로 주입받도록 되어 있으므로, 실행 시 프로필을 반드시 지정하는 것이 안전합니다.

### 6.2 테스트 실행

```bash
./gradlew test
```

테스트는 `test` 프로필과 Testcontainers 기반 MariaDB 컨테이너를 사용합니다.

---

## 7. Database & Migration

Flyway 마이그레이션은 `src/main/resources/db/migration` 아래에서 관리됩니다.

- `V1__create_member_table.sql`: 회원 테이블 생성
- `V2__create_board_table.sql`: 게시글 테이블 생성
- `V3__add_member_dummy.sql`: 대량 회원 더미 데이터 삽입

### 참고 사항

`V3__add_member_dummy.sql`에는 대량의 더미 회원 데이터가 포함되어 있어, 로컬 환경이나 테스트 실행 시간/초기 적재량에 영향을 줄 수 있습니다. 대규모 조회 성능 실험이나 페이징/CQRS 검증 목적의 데이터셋으로 이해하면 됩니다.

---

## 8. API Documentation

Swagger UI는 아래 경로에서 확인할 수 있습니다.

- Swagger UI: `http://localhost:8099/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8099/v1/api-docs`

Spring Security 설정상 아래 경로는 인증 없이 접근 가능합니다.

- `/v1/api-docs/**`
- `/swagger-ui.html`
- `/swagger-ui/**`
- `/v1/member/**`
- `/v1/board/**`

---

## 9. Example API Endpoints

### Member

- `GET /v1/member/page`
- `GET /v1/member?id={memberId}`
- `POST /v1/member`
- `PUT /v1/member/{id}`
- `DELETE /v1/member?id={memberId}`

예시 요청:

```json
{
  "email": "user@example.com",
  "password": "QWERasdf1234!",
  "name": "홍길동",
  "gender": "MALE",
  "phoneNumber": "010-1234-5678",
  "address": "서울특별시 강남구 테헤란로 123"
}
```

### Board

- `GET /v1/board/page`
- `GET /v1/board?id={boardId}`
- `POST /v1/board`
- `PUT /v1/board/{id}`
- `DELETE /v1/board?id={boardId}`

예시 요청:

```json
{
  "title": "가입인사",
  "content": "가입인사 작성합니다. 만나서 반갑습니다.",
  "memberId": "01JWG8S471E52NTHD6T1G51F6M"
}
```

---

## 10. Testing Strategy

이 프로젝트는 다음 수준의 테스트를 함께 사용합니다.

- **Application Service 테스트**: 유스케이스 로직 검증
- **Persistence Adapter 테스트**: DB 연동 및 매핑 검증
- **Controller 통합 테스트**: MockMvc 기반 HTTP 레벨 검증
- **Testcontainers**: 실제 MariaDB 컨테이너를 사용해 운영 환경과 유사한 검증 수행

즉, 단순 단위 테스트뿐 아니라 **실제 인프라와 붙는 통합 테스트 흐름**을 중요하게 다루는 예제입니다.

---

## 11. Why this repository exists

이 저장소는 아래 목적에 특히 잘 맞습니다.

- Spring Boot에서 **DDD + Hexagonal Architecture** 구조를 어떻게 나눌지 실험하고 싶을 때
- JPA와 QueryDSL을 함께 쓰면서 **조회/명령 책임을 분리**해보고 싶을 때
- Flyway와 Testcontainers를 함께 사용해 **재현 가능한 개발 환경**을 만들고 싶을 때
- 회원/게시글 같은 익숙한 도메인으로 **아키텍처 중심 PoC**를 빠르게 만들고 싶을 때

---

## 12. Notes

- `src/main/generated` 아래에는 생성된 매퍼/QueryDSL 관련 코드가 포함되어 있습니다.
- `UpdateMemberResponse.kt`처럼 일부 Kotlin 파일이 공존하지만, 애플리케이션의 중심 구현은 Java입니다.
- 현재 저장소는 PoC 성격이 강하므로, 인증/인가 고도화나 운영용 설정 분리 등은 추가 확장이 필요한 영역입니다.
