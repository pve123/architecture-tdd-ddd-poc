package io.github.pve123.hexagonal.regression;

import com.jayway.jsonpath.JsonPath;
import io.github.pve123.hexagonal.config.ApiTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 소프트 삭제와 검색에서 발견했던 버그가 다시 생기지 않는지 API 바깥에서 확인합니다. */
class SoftDeleteAndSearchRegressionTest extends ApiTestSupport {

    @Test
    void 지운_글은_단건_조회되지_않는다() throws Exception {
        String token = signUpAndLogin("writer@example.com");
        String boardId = createBoard(token, "지울 글");

        mockMvc.perform(as(token, delete("/v1/boards/{boardId}", boardId))).andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/boards/{boardId}", boardId)).andExpect(status().isNotFound());
    }

    @Test
    void 글_목록의_전체_개수에_지운_글은_세지_않는다() throws Exception {
        String token = signUpAndLogin("writer@example.com");
        createBoard(token, "남을 글");
        String deletedBoardId = createBoard(token, "지울 글");
        mockMvc.perform(as(token, delete("/v1/boards/{boardId}", deletedBoardId)));

        // 첫 페이지에 다 들어가면 Page가 개수를 스스로 맞추므로, 페이지 크기를 1로 줄여 개수 쿼리를 직접 확인합니다.
        mockMvc.perform(get("/v1/boards").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void 회원_목록의_전체_개수에_탈퇴한_회원은_세지_않는다() throws Exception {
        String adminToken = signUpAdminAndLogin("admin@example.com");
        String leavingToken = signUpAndLogin("leave@example.com");
        mockMvc.perform(as(leavingToken, delete("/v1/members/me")));

        mockMvc.perform(as(adminToken, get("/v1/admin/members")).param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void 성별을_소문자로_검색해도_찾는다() throws Exception {
        String adminToken = signUpAdminAndLogin("admin@example.com");

        mockMvc.perform(as(adminToken, get("/v1/admin/members")).param("gender", "male"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("admin@example.com"));
    }

    @Test
    void 이미_가입된_이메일로_가입하면_409로_응답한다() throws Exception {
        signUp("same@example.com");

        mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON).content(memberJson("same@example.com")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("MEMBER_003"));
    }

    private String createBoard(String token, String title) throws Exception {
        String body = mockMvc.perform(as(token, post("/v1/boards")).contentType(MediaType.APPLICATION_JSON).content("""
                        {"title": "%s", "content": "내용"}
                        """.formatted(title)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }
}
