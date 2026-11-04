package com.example.demo.regression;

import com.example.demo.config.TestContainerConfig;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 소프트 삭제와 검색에서 발견한 버그를 API 바깥에서 재현합니다.
 * 내부 구조를 바꿔도 같은 동작이 지켜지는지 확인하려고 HTTP 요청과 응답만 봅니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SoftDeleteAndSearchRegressionTest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 지운_글은_단건_조회되지_않는다() throws Exception {
        String memberId = createMember("writer@example.com");
        String boardId = createBoard(memberId, "지울 글");

        mockMvc.perform(delete("/v1/board").param("id", boardId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/board").param("id", boardId))
                .andExpect(status().isNotFound());
    }

    @Test
    void 글_목록의_전체_개수에_지운_글은_세지_않는다() throws Exception {
        String memberId = createMember("writer@example.com");
        createBoard(memberId, "남을 글");
        String deletedBoardId = createBoard(memberId, "지울 글");
        mockMvc.perform(delete("/v1/board").param("id", deletedBoardId));

        // 첫 페이지에 다 들어가면 Page가 개수를 스스로 맞추므로, 페이지 크기를 1로 줄여 개수 쿼리를 직접 확인합니다.
        mockMvc.perform(get("/v1/board/page").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void 회원_목록의_전체_개수에_탈퇴한_회원은_세지_않는다() throws Exception {
        createMember("stay@example.com");
        String leavingMemberId = createMember("leave@example.com");
        mockMvc.perform(delete("/v1/member").param("id", leavingMemberId));

        mockMvc.perform(get("/v1/member/page").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void 성별을_소문자로_검색해도_찾는다() throws Exception {
        createMember("male@example.com");

        mockMvc.perform(get("/v1/member/page").param("gender", "male"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("male@example.com"));
    }

    @Test
    void 이미_가입된_이메일로_가입하면_409로_응답한다() throws Exception {
        createMember("same@example.com");

        mockMvc.perform(post("/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson("same@example.com")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("MEMBER_003"));
    }

    private String createMember(String email) throws Exception {
        String body = mockMvc.perform(post("/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson(email)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private String createBoard(String memberId, String title) throws Exception {
        String body = mockMvc.perform(post("/v1/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "%s", "content": "내용", "memberId": "%s"}
                                """.formatted(title, memberId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private static String memberJson(String email) {
        return """
                {
                  "email": "%s",
                  "password": "QWERasdf1234!",
                  "name": "홍길동",
                  "gender": "MALE",
                  "phoneNumber": "010-1234-5678",
                  "address": "서울특별시 강남구 테헤란로 123"
                }
                """.formatted(email);
    }
}
