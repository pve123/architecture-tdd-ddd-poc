package io.github.pve123.hexagonal.regression;

import io.github.pve123.hexagonal.config.ApiTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 오류 응답에 내부 클래스 이름 같은 서버 내부 정보가 담기지 않는지 확인합니다. */
class ErrorMessageLeakRegressionTest extends ApiTestSupport {

    /** 예전에는 글쓴이를 요청 본문의 ID로 찾다가 JPA 엔티티 이름이 응답에 나갔습니다. 지금은 탈퇴 후 남은 토큰으로 재현합니다. */
    @Test
    void 탈퇴한_회원의_토큰으로_글을_쓰면_정해진_메시지로_응답한다() throws Exception {
        String token = signUpAndLogin("leave@example.com");
        mockMvc.perform(as(token, delete("/v1/members/me")));

        mockMvc.perform(as(token, post("/v1/boards")).contentType(MediaType.APPLICATION_JSON).content("""
                        {"title": "제목", "content": "내용"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MEMBER_002"))
                .andExpect(jsonPath("$.message").value("탈퇴한 사용자입니다."));
    }
}
