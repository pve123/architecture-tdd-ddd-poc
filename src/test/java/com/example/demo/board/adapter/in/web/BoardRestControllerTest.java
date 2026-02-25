package com.example.demo.board.adapter.in.web;


import com.example.demo.board.adapter.in.web.request.CreateBoardRequest;
import com.example.demo.board.adapter.in.web.request.UpdateBoardRequest;
import com.example.demo.board.adapter.in.web.response.CreateBoardResponse;
import com.example.demo.config.TestContainerConfig;
import com.example.demo.member.adapter.out.persistence.MemberJpaEntity;
import com.example.demo.member.adapter.out.persistence.MemberRepository;
import com.example.demo.member.domain.GenderEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Board E2E Integration Test")
public class BoardRestControllerTest extends TestContainerConfig {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 테스트용 Member 엔티티를 직접 DB에 저장해서 선행 데이터로 사용.
     * → Board 테스트가 Member 컨트롤러에 의존하지 않음.
     */
    private MemberJpaEntity createMemberFixture() {
        MemberJpaEntity member = MemberJpaEntity.builder()
                .email("user@example.com")
                .password("QWERasdf1234!")
                .name("홍길동")
                .gender(GenderEnum.MALE)
                .phoneNumber("010-1234-5678")
                .address("서울특별시 강남구 테헤란로 123")
                .build();

        return memberRepository.save(member);
    }

    /**
     * Board 생성 API를 호출해서 게시글 하나 만들고, 생성된 게시글 ID를 반환.
     * Board 흐름만 HTTP로 검증하고, Member는 이미 DB에 있으므로 member.getId()만 사용.
     **/
    private CreateBoardResponse createBoardFixture(MemberJpaEntity member) throws Exception {
        CreateBoardRequest request = new CreateBoardRequest(
                "가입인사",
                "가입인사 작성합니다. 만나서 반갑습니다.",
                member.getId()
        );

        String responseBody = mockMvc.perform(post("/v1/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(responseBody, CreateBoardResponse.class);
    }


    @Test
    @DisplayName("게시글 페이징 목록 조회 API 통합 테스트")
    void 게시글_페이징_목록_API_통합_테스트() throws Exception {

        // given
        MemberJpaEntity member = createMemberFixture();
        CreateBoardResponse createBoardResponse = createBoardFixture(member);

        // when & then
        mockMvc.perform(get("/v1/board/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.content[0].id").value(createBoardResponse.id()))
                .andExpect(jsonPath("$.content[0].title").value("가입인사"))
                .andExpect(jsonPath("$.content[0].content").value("가입인사 작성합니다. 만나서 반갑습니다."));
    }
//
//    @Test
//    @DisplayName("게시글 단건 조회 API 통합 테스트")
//    void 게시글_조회_API_통합_테스트() throws Exception {
//
//        // given
//        CreateBoardResponse created = createBoardFixture();
//
//        // when & then
//        mockMvc.perform(get("/v1/board").param("id", created.id()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(created.id()))
//                .andExpect(jsonPath("$.title").value("게시글 제목입니다"))
//                .andExpect(jsonPath("$.content").value("게시글 내용입니다."))
//                .andExpect(jsonPath("$.writer").value("작성자이름"));
//    }
//
//    @Test
//    @DisplayName("게시글 생성 API 통합 테스트")
//    void 게시글_생성_API_통합_테스트() throws Exception {
//
//        // when
//        CreateBoardResponse created = createBoardFixture();
//
//        // then
//        assertAll(
//                () -> assertThat(created.id()).isNotBlank(),
//                () -> assertThat(created.title()).isEqualTo("게시글 제목입니다"),
//                () -> assertThat(created.content()).isEqualTo("게시글 내용입니다."),
//                () -> assertThat(created.member().name()).isEqualTo("작성자이름")
//        );
//    }
//
//    @Test
//    @DisplayName("게시글 수정 API 통합 테스트")
//    void 게시글_수정_API_통합_테스트() throws Exception {
//
//        // given
//        CreateBoardResponse created = createBoardFixture();
//
//        // ★ 실제 UpdateBoardRequest 필드에 맞게 수정하세요
//        UpdateBoardRequest request = new UpdateBoardRequest(
//                "수정된 제목입니다",
//                "수정된 내용입니다.",
//                "01JWG8S471E52NTHD6T1G51F6M"
//        );
//
//        // when & then
//        mockMvc.perform(put("/v1/board/{id}", created.id())
//                        .content(objectMapper.writeValueAsString(request))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(created.id()))
//                .andExpect(jsonPath("$.title").value("수정된 제목입니다"))
//                .andExpect(jsonPath("$.content").value("수정된 내용입니다."));
//    }
//
//    @Test
//    @DisplayName("게시글 삭제 API 통합 테스트")
//    void 게시글_삭제_API_통합_테스트() throws Exception {
//
//        // given
//        CreateBoardResponse created = createBoardFixture();
//
//        // when & then
//        mockMvc.perform(delete("/v1/board")
//                        .param("id", created.id()))
//                .andExpect(status().isNoContent());
//    }
}
