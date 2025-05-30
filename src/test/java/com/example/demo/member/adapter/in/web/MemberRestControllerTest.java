package com.example.demo.member.adapter.in.web;


import com.example.demo.config.test.TestContainerConfig;
import com.example.demo.member.adapter.in.web.request.CreateMemberRequest;
import com.example.demo.member.domain.GenderEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Member E2E Integration Test")
public class MemberRestControllerTest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원_생성_API_통합_테스트() throws Exception {
        // Given
        CreateMemberRequest request = new CreateMemberRequest(
                "user@example.com",
                "QWERasdf1234!",
                "홍길동",
                GenderEnum.MALE,
                "010-1234-5678",
                "서울특별시 강남구 테헤란로 123"
        );

        // When & Then
        mockMvc.perform(post("/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }
}
