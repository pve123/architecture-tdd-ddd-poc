package io.github.pve123.hexagonal.member.adapter.in.web;

import com.jayway.jsonpath.JsonPath;
import io.github.pve123.hexagonal.config.TestContainerConfig;
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

/** 회원 API의 계약(경로, 상태 코드, JSON 모양)을 지킵니다. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberRestControllerTest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 가입하면_비밀번호_없이_회원_정보를_돌려준다() throws Exception {
        mockMvc.perform(post("/v1/member").contentType(MediaType.APPLICATION_JSON).content(memberJson("user@example.com")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void 비밀번호_규칙에_맞지_않으면_400으로_응답한다() throws Exception {
        mockMvc.perform(post("/v1/member").contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson("user@example.com").replace("QWERasdf1234!", "weak")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원_한_명과_목록을_볼_수_있다() throws Exception {
        String id = createMember("user@example.com");

        mockMvc.perform(get("/v1/member").param("id", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("홍길동"));
        mockMvc.perform(get("/v1/member/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(id))
                .andExpect(jsonPath("$.content[0].email").value("user@example.com"));
    }

    @Test
    void 연락처를_고칠_수_있다() throws Exception {
        String id = createMember("user@example.com");

        mockMvc.perform(put("/v1/member/{id}", id).contentType(MediaType.APPLICATION_JSON).content("""
                        {"email": "usertest@example.com", "phoneNumber": "010-1234-9999", "address": "서울특별시 강남구 테헤란로 999"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("usertest@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-9999"))
                .andExpect(jsonPath("$.address").value("서울특별시 강남구 테헤란로 999"));
    }

    @Test
    void 탈퇴하면_다시_조회되지_않는다() throws Exception {
        String id = createMember("user@example.com");

        mockMvc.perform(delete("/v1/member").param("id", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/v1/member").param("id", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("MEMBER_001"));
    }

    private String createMember(String email) throws Exception {
        String body = mockMvc.perform(post("/v1/member").contentType(MediaType.APPLICATION_JSON).content(memberJson(email)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    static String memberJson(String email) {
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
