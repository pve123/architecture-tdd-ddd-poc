package com.example.demo.member.adapter.in.web;


import com.example.demo.common.response.CommonResponse;
import com.example.demo.config.test.TestContainerConfig;
import com.example.demo.member.adapter.in.web.request.CreateMemberRequest;
import com.example.demo.member.adapter.in.web.response.CreateMemberResponse;
import com.example.demo.member.domain.GenderEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
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

    private CreateMemberResponse createMemberResponse;


    @BeforeEach
    void setup() throws Exception {
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

        CommonResponse commonResponse = objectMapper.readValue(responseBody, CommonResponse.class);
        createMemberResponse = objectMapper.convertValue(commonResponse.data(), CreateMemberResponse.class);
    }

    @Test
    void 회원_생성_API_통합_테스트() {
        //When & Then
        assertAll(
                () -> assertThat(createMemberResponse.name()).isEqualTo("홍길동"),
                () -> assertThat(createMemberResponse.email()).isEqualTo("user@example.com"),
                () -> assertThat(createMemberResponse.gender()).isEqualTo(GenderEnum.MALE)
        );
    }

    @Test
    void 회원_조회_API_통합_테스트() throws Exception {

        //When & Then
        mockMvc.perform(get("/v1/member")
                        .param("id", createMemberResponse.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createMemberResponse.id()))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.gender").value(GenderEnum.MALE.name()));

    }

    @Test
    void 회원_페이징_목록_API_통합_테스트() throws Exception {

        //When & Then
        mockMvc.perform(get("/v1/member/page")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(greaterThan(0)))
                .andExpect(jsonPath("$.content[0].id").value(createMemberResponse.id()))
                .andExpect(jsonPath("$.content[0].name").value("홍길동"))
                .andExpect(jsonPath("$.content[0].email").value("user@example.com"))
                .andExpect(jsonPath("$.content[0].gender").value(GenderEnum.MALE.name()));

    }

    @Test
    void 회원_삭제_API_통합_테스트() throws Exception {

        //When & Then
        mockMvc.perform(delete("/v1/member")
                        .param("id", createMemberResponse.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

}
