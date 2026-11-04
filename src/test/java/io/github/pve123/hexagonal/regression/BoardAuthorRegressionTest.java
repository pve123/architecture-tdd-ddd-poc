package io.github.pve123.hexagonal.regression;

import com.jayway.jsonpath.JsonPath;
import io.github.pve123.hexagonal.config.ApiTestSupport;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 게시글 응답의 글쓴이 정보를 확인합니다. */
class BoardAuthorRegressionTest extends ApiTestSupport {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void 글이_몇_개든_목록은_쿼리_세_번으로_가져온다() throws Exception {
        for (int i = 1; i <= 5; i++) {
            String token = signUpAndLogin("writer" + i + "@example.com");
            writeBoard(token, "글 " + i);
        }
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.clear();

        mockMvc.perform(get("/v1/boards").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5));

        // 글 목록 1번, 전체 개수 1번, 글쓴이 이름 1번
        assertThat(statistics.getPrepareStatementCount()).isLessThanOrEqualTo(3);
    }

    @Test
    void 글_응답에는_글쓴이의_연락처를_담지_않는다() throws Exception {
        String token = signUpAndLogin("writer@example.com");
        String boardId = writeBoard(token, "가입인사");

        mockMvc.perform(get("/v1/boards/{boardId}", boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author.name").value("홍길동"))
                .andExpect(jsonPath("$.author.email").doesNotExist())
                .andExpect(jsonPath("$.author.phoneNumber").doesNotExist())
                .andExpect(jsonPath("$.author.address").doesNotExist());
    }

    @Test
    void 글쓴이가_탈퇴해도_글은_볼_수_있다() throws Exception {
        String token = signUpAndLogin("writer@example.com");
        String boardId = writeBoard(token, "남는 글");
        mockMvc.perform(as(token, delete("/v1/members/me"))).andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/boards/{boardId}", boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author.name").value("탈퇴한 회원"));
    }

    private String writeBoard(String token, String title) throws Exception {
        String body = mockMvc.perform(as(token, post("/v1/boards")).contentType(MediaType.APPLICATION_JSON).content("""
                        {"title": "%s", "content": "내용"}
                        """.formatted(title)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }
}
