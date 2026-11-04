package io.github.pve123.hexagonal.board.adapter.in.web;

import com.jayway.jsonpath.JsonPath;
import io.github.pve123.hexagonal.config.ApiTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardRestControllerTest extends ApiTestSupport {

    private String writerToken;

    @BeforeEach
    void setUp() throws Exception {
        writerToken = signUpAndLogin("writer@example.com");
    }

    @Test
    void 글쓴이는_요청_본문이_아니라_토큰으로_정해진다() throws Exception {
        String otherId = signUp("other@example.com");

        mockMvc.perform(as(writerToken, post("/v1/boards")).contentType(MediaType.APPLICATION_JSON).content("""
                        {"title": "가입인사", "content": "반갑습니다.", "memberId": "%s"}
                        """.formatted(otherId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.member.email").value("writer@example.com"));
    }

    @Test
    void 로그인하지_않으면_글을_쓸_수_없지만_읽을_수는_있다() throws Exception {
        String boardId = createBoard("가입인사");

        mockMvc.perform(post("/v1/boards").contentType(MediaType.APPLICATION_JSON).content(boardJson("제목")))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/v1/boards/{boardId}", boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("가입인사"));
        mockMvc.perform(get("/v1/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(boardId));
    }

    @Test
    void 글쓴이는_글을_고칠_수_있다() throws Exception {
        String boardId = createBoard("가입인사");

        mockMvc.perform(as(writerToken, patch("/v1/boards/{boardId}", boardId)).contentType(MediaType.APPLICATION_JSON)
                        .content(boardJson("수정된 제목입니다")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목입니다"));
    }

    @Test
    void 다른_회원은_글을_고칠_수_없다() throws Exception {
        String boardId = createBoard("가입인사");
        String otherToken = signUpAndLogin("other@example.com");

        mockMvc.perform(as(otherToken, patch("/v1/boards/{boardId}", boardId)).contentType(MediaType.APPLICATION_JSON)
                        .content(boardJson("남의 글 고치기")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("BOARD_003"));
    }

    @Test
    void 다른_회원은_글을_지울_수_없다() throws Exception {
        String boardId = createBoard("가입인사");
        String otherToken = signUpAndLogin("other@example.com");

        mockMvc.perform(as(otherToken, delete("/v1/boards/{boardId}", boardId)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("BOARD_003"));
        mockMvc.perform(get("/v1/boards/{boardId}", boardId))
                .andExpect(status().isOk());
    }

    @Test
    void 글쓴이가_지운_글은_다시_조회되지_않는다() throws Exception {
        String boardId = createBoard("가입인사");

        mockMvc.perform(as(writerToken, delete("/v1/boards/{boardId}", boardId)))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/v1/boards/{boardId}", boardId))
                .andExpect(status().isNotFound());
    }

    private String createBoard(String title) throws Exception {
        String body = mockMvc.perform(as(writerToken, post("/v1/boards")).contentType(MediaType.APPLICATION_JSON)
                        .content(boardJson(title)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private static String boardJson(String title) {
        return """
                {"title": "%s", "content": "가입인사 작성합니다. 만나서 반갑습니다."}
                """.formatted(title);
    }
}
