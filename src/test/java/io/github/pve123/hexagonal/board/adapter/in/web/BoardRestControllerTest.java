package io.github.pve123.hexagonal.board.adapter.in.web;

import com.jayway.jsonpath.JsonPath;
import io.github.pve123.hexagonal.config.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 게시글 API의 계약(경로, 상태 코드, JSON 모양)을 지킵니다. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BoardRestControllerTest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    private String writerId;

    @BeforeEach
    void setUp() throws Exception {
        writerId = createMember("writer@example.com");
    }

    @Test
    void 글을_쓰면_글쓴이_정보와_함께_돌려준다() throws Exception {
        mockMvc.perform(post("/v1/board").contentType(MediaType.APPLICATION_JSON).content(boardJson("가입인사", writerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("가입인사"))
                .andExpect(jsonPath("$.member.id").value(writerId))
                .andExpect(jsonPath("$.member.name").value("홍길동"));
    }

    @Test
    void 글_한_개와_목록을_볼_수_있다() throws Exception {
        String boardId = createBoard("가입인사");

        mockMvc.perform(get("/v1/board").param("id", boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(boardId))
                .andExpect(jsonPath("$.content").value("가입인사 작성합니다. 만나서 반갑습니다."))
                .andExpect(jsonPath("$.member.id").value(writerId));
        mockMvc.perform(get("/v1/board/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(boardId))
                .andExpect(jsonPath("$.content[0].title").value("가입인사"));
    }

    @Test
    void 글쓴이는_글을_고칠_수_있다() throws Exception {
        String boardId = createBoard("가입인사");

        mockMvc.perform(put("/v1/board/{id}", boardId).contentType(MediaType.APPLICATION_JSON).content("""
                        {"title": "수정된 제목입니다", "content": "수정된 내용입니다.", "memberId": "%s"}
                        """.formatted(writerId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목입니다"))
                .andExpect(jsonPath("$.content").value("수정된 내용입니다."));
    }

    @Test
    void 다른_회원은_글을_고칠_수_없다() throws Exception {
        String boardId = createBoard("가입인사");
        String otherId = createMember("other@example.com");

        mockMvc.perform(put("/v1/board/{id}", boardId).contentType(MediaType.APPLICATION_JSON).content("""
                        {"title": "남의 글 고치기", "content": "내용", "memberId": "%s"}
                        """.formatted(otherId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BOARD_003"));
    }

    @Test
    void 지운_글은_204로_응답하고_다시_조회되지_않는다() throws Exception {
        String boardId = createBoard("가입인사");

        mockMvc.perform(delete("/v1/board").param("id", boardId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/v1/board").param("id", boardId))
                .andExpect(status().isNotFound());
    }

    private String createBoard(String title) throws Exception {
        String body = mockMvc.perform(post("/v1/board").contentType(MediaType.APPLICATION_JSON).content(boardJson(title, writerId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private String createMember(String email) throws Exception {
        String body = mockMvc.perform(post("/v1/member").contentType(MediaType.APPLICATION_JSON).content("""
                        {"email": "%s", "password": "QWERasdf1234!", "name": "홍길동", "gender": "MALE",
                         "phoneNumber": "010-1234-5678", "address": "서울특별시 강남구 테헤란로 123"}
                        """.formatted(email)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private static String boardJson(String title, String memberId) {
        return """
                {"title": "%s", "content": "가입인사 작성합니다. 만나서 반갑습니다.", "memberId": "%s"}
                """.formatted(title, memberId);
    }
}
