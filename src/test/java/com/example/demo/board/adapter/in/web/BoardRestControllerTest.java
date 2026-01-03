package com.example.demo.board.adapter.in.web;


import com.example.demo.board.adapter.in.web.request.CreateBoardRequest;
import com.example.demo.board.adapter.in.web.request.UpdateBoardRequest;
import com.example.demo.board.adapter.in.web.response.CreateBoardResponse;
import com.example.demo.common.response.CommonResponse;
import com.example.demo.config.TestContainerConfig;
import com.example.demo.member.adapter.in.web.request.CreateMemberRequest;
import com.example.demo.member.adapter.in.web.response.CreateMemberResponse;
import com.example.demo.member.domain.GenderEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.greaterThan;
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

    @PersistenceContext
    private EntityManager entityManager;

    private CreateBoardResponse createBoardResponse;
    private CreateMemberResponse createMemberResponse;


    @BeforeEach
    void setup() throws Exception {


        //회원 생성후 저장
        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                "user@example.com",
                "QWERasdf1234!",
                "홍길동",
                GenderEnum.MALE,
                "010-1234-5678",
                "서울특별시 강남구 테헤란로 123"
        );

        String memberResponseBody = mockMvc.perform(post("/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CommonResponse memberCommonResponse = objectMapper.readValue(memberResponseBody, CommonResponse.class);
        createMemberResponse = objectMapper.convertValue(memberCommonResponse.data(), CreateMemberResponse.class);


        entityManager.flush();
        //저장한 회원으로 게시글 작성
        CreateBoardRequest createBoardRequest = new CreateBoardRequest(
                "제목 테스트 하려고 작성합니다.",
                "내용 테스트 하려고 작성합니다.",
                createMemberResponse.id
        );

        String BoardResponseBody = mockMvc.perform(post("/v1/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBoardRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CommonResponse boardCommonResponse = objectMapper.readValue(BoardResponseBody, CommonResponse.class);
        createBoardResponse = objectMapper.convertValue(boardCommonResponse.data(), CreateBoardResponse.class);
    }

    @Test
    void 게시글_생성_API_통합_테스트() {
        //When & Then
        assertAll(
                () -> assertThat(createBoardResponse.title()).isEqualTo("제목 테스트 하려고 작성합니다."),
                () -> assertThat(createBoardResponse.content()).isEqualTo("내용 테스트 하려고 작성합니다."),
                () -> assertThat(createBoardResponse.member().id).isEqualTo(createBoardResponse.member().id)
        );
    }

    @Test
    void 게시글_조회_API_통합_테스트() throws Exception {

        //When & Then
        mockMvc.perform(get("/v1/board")
                        .param("id", createBoardResponse.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createBoardResponse.id()))
                .andExpect(jsonPath("$.title").value("제목 테스트 하려고 작성합니다."))
                .andExpect(jsonPath("$.content").value("내용 테스트 하려고 작성합니다."))
                .andExpect(jsonPath("$.member.id").value(createBoardResponse.member().id));

    }

    @Test
    void 게시글_페이징_목록_API_통합_테스트() throws Exception {

        //When & Then
        mockMvc.perform(get("/v1/board/page")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(greaterThan(0)))
                .andExpect(jsonPath("$.content[0].id").value(createBoardResponse.id()))
                .andExpect(jsonPath("$.content[0].title").value("제목 테스트 하려고 작성합니다."))
                .andExpect(jsonPath("$.content[0].content").value("내용 테스트 하려고 작성합니다."))
                .andExpect(jsonPath("$.content[0].member.id").value(createBoardResponse.member().id));

    }

    @Test
    void 게시글_수정_API_통합_테스트() throws Exception {

        //When
        UpdateBoardRequest updateBoardRequest = new UpdateBoardRequest(
                "제목 테스트 하려고 작성합니다.@@@@@",
                "내용 테스트 하려고 작성합니다.@@@@@",
                createMemberResponse.id
        );

        //Then
        mockMvc.perform(put("/v1/board/{id}", createBoardResponse.id())
                        .content(objectMapper.writeValueAsString(updateBoardRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createBoardResponse.id()))
                .andExpect(jsonPath("$.title").value("제목 테스트 하려고 작성합니다.@@@@@"))
                .andExpect(jsonPath("$.content").value("내용 테스트 하려고 작성합니다.@@@@@"))
                .andExpect(jsonPath("$.member.id").value(createBoardResponse.member().id));

    }
}
