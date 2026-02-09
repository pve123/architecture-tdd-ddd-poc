package com.example.demo.member.adapter.in.web;


import com.example.demo.config.TestContainerConfig;
import com.example.demo.member.adapter.in.web.request.CreateMemberRequest;
import com.example.demo.member.adapter.in.web.request.UpdateMemberRequest;
import com.example.demo.member.adapter.in.web.response.CreateMemberResponse;
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
@DisplayName("Member E2E Integration Test")
public class MemberRestControllerTest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 테스트용 회원 한 명 생성하는 헬퍼.
     * 각 테스트가 필요할 때마다 호출해서 자신의 픽스처를 만든다.
     */
    private CreateMemberResponse createMemberFixture() throws Exception {

        CreateMemberRequest request = new CreateMemberRequest(
                "user@example.com",
                "QWERasdf1234!",
                "홍길동",
                GenderEnum.MALE,
                "010-1234-5678",
                "서울특별시 강남구 테헤란로 123"
        );

        String responseBody = mockMvc.perform(post("/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(responseBody, CreateMemberResponse.class);
    }

    @Test
    @DisplayName("회원 페이징 목록 조회 API 통합 테스트")
    void 회원_페이징_목록_API_통합_테스트() throws Exception {


        // given
        CreateMemberResponse created = createMemberFixture();

        // when & then
        mockMvc.perform(get("/v1/member/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.content[0].id").value(created.id()))
                .andExpect(jsonPath("$.content[0].name").value("홍길동"))
                .andExpect(jsonPath("$.content[0].email").value("user@example.com"))
                .andExpect(jsonPath("$.content[0].gender").value(GenderEnum.MALE.name()));

    }

    @Test
    @DisplayName("회원 단건 조회 API 통합 테스트")
    void 회원_조회_API_통합_테스트() throws Exception {

        // given
        CreateMemberResponse created = createMemberFixture();

        // when & then
        mockMvc.perform(get("/v1/member").param("id", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id()))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.gender").value(GenderEnum.MALE.name()));

    }


    @Test
    @DisplayName("회원 생성 API 통합 테스트")
    void 회원_생성_API_통합_테스트() throws Exception {


        // when
        CreateMemberResponse created = createMemberFixture();

        // then
        assertAll(
                () -> assertThat(created.id()).isNotBlank(),
                () -> assertThat(created.name()).isEqualTo("홍길동"),
                () -> assertThat(created.email()).isEqualTo("user@example.com"),
                () -> assertThat(created.gender()).isEqualTo(GenderEnum.MALE)
        );
    }

    @Test
    @DisplayName("회원 수정 API 통합 테스트")
    void 회원_수정_API_통합_테스트() throws Exception {

        // given
        CreateMemberResponse created = createMemberFixture();

        UpdateMemberRequest request = new UpdateMemberRequest(
                "usertest@example.com",
                "010-1234-9999",
                "서울특별시 강남구 테헤란로 999"
        );

        // when & then
        mockMvc.perform(put("/v1/member/{id}", created.id())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id()))
                .andExpect(jsonPath("$.email").value("usertest@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-9999"))
                .andExpect(jsonPath("$.address").value("서울특별시 강남구 테헤란로 999"));

    }

    @Test
    @DisplayName("회원 삭제 API 통합 테스트")
    void 회원_삭제_API_통합_테스트() throws Exception {

        // given
        CreateMemberResponse created = createMemberFixture();

        // when & then
        mockMvc.perform(delete("/v1/member")
                        .param("id", created.id()))
                .andExpect(status().isNoContent());

    }

}
