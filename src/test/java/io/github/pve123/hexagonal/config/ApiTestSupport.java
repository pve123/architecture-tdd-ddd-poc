package io.github.pve123.hexagonal.config;

import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.AbstractMockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** API를 호출해 보는 테스트의 공통 준비물입니다. 회원가입, 로그인, 토큰 붙이기를 한 줄로 합니다. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class ApiTestSupport extends TestContainerConfig {

    protected static final String PASSWORD = "QWERasdf1234!";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    protected String signUp(String email) throws Exception {
        String body = mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON).content(memberJson(email)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    protected String login(String email) throws Exception {
        String body = mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                        {"email": "%s", "password": "%s"}
                        """.formatted(email, PASSWORD)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.accessToken");
    }

    protected String signUpAndLogin(String email) throws Exception {
        signUp(email);
        return login(email);
    }

    protected String signUpAdminAndLogin(String email) throws Exception {
        String memberId = signUp(email);
        jdbcTemplate.update("UPDATE member SET role = 'ADMIN' WHERE id = ?", memberId);
        // 관리자로 바꾼 값을 다시 읽도록, JPA가 기억하고 있던 회원을 비웁니다.
        entityManager.clear();
        return login(email);
    }

    protected static <T extends AbstractMockHttpServletRequestBuilder<?>> T as(String token, T request) {
        request.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return request;
    }

    protected static String memberJson(String email) {
        return """
                {
                  "email": "%s",
                  "password": "%s",
                  "name": "홍길동",
                  "gender": "MALE",
                  "phoneNumber": "010-1234-5678",
                  "address": "서울특별시 강남구 테헤란로 123"
                }
                """.formatted(email, PASSWORD);
    }
}
