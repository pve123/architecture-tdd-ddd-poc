package com.example.demo.regression;

import com.example.demo.config.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 오류 응답에 내부 클래스 이름 같은 서버 내부 정보가 담기지 않는지 확인합니다. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ErrorMessageLeakRegressionTest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 없는_회원으로_글을_쓰면_404와_정해진_메시지로_응답한다() throws Exception {
        mockMvc.perform(post("/v1/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "제목", "content": "내용", "memberId": "01AAAAAAAAAAAAAAAAAAAAAAAA"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("MEMBER_001"))
                .andExpect(jsonPath("$.message").value("해당 사용자를 찾을수 없습니다."));
    }
}
