package io.github.pve123.hexagonal.member.adapter.in.web;

import io.github.pve123.hexagonal.config.ApiTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberRestControllerTest extends ApiTestSupport {

    @Test
    void 가입하면_비밀번호_없이_회원_정보를_돌려준다() throws Exception {
        mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON).content(memberJson("user@example.com")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void 비밀번호_규칙에_맞지_않으면_가입할_수_없다() throws Exception {
        mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson("user@example.com").replace(PASSWORD, "weak")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 비밀번호가_틀리면_로그인할_수_없다() throws Exception {
        signUp("user@example.com");

        mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                        {"email": "user@example.com", "password": "wrong-password"}
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("MEMBER_004"));
    }

    @Test
    void 토큰_없이_내_정보를_보면_401로_응답한다() throws Exception {
        mockMvc.perform(get("/v1/members/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("COMMON_401"));
    }

    @Test
    void 로그인하면_내_정보를_보고_고칠_수_있다() throws Exception {
        String token = signUpAndLogin("user@example.com");

        mockMvc.perform(as(token, get("/v1/members/me")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
        mockMvc.perform(as(token, patch("/v1/members/me")).contentType(MediaType.APPLICATION_JSON).content("""
                        {"phoneNumber": "010-1234-9999"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-9999"));
    }

    @Test
    void 탈퇴하면_다시_로그인할_수_없다() throws Exception {
        String token = signUpAndLogin("user@example.com");

        mockMvc.perform(as(token, delete("/v1/members/me")))
                .andExpect(status().isNoContent());
        mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                        {"email": "user@example.com", "password": "%s"}
                        """.formatted(PASSWORD)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 일반_회원은_회원_목록을_볼_수_없다() throws Exception {
        String token = signUpAndLogin("user@example.com");

        mockMvc.perform(as(token, get("/v1/admin/members")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("COMMON_403"));
    }

    @Test
    void 관리자는_회원을_검색할_수_있다() throws Exception {
        signUp("kim@example.com");
        String adminToken = signUpAdminAndLogin("admin@example.com");

        mockMvc.perform(as(adminToken, get("/v1/admin/members")).param("email", "kim"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.content[0].email").value("kim@example.com"));
    }
}
