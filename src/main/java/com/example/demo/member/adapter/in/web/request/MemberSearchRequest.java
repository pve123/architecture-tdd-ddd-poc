package com.example.demo.member.adapter.in.web.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "동적 검색 관련 사용자 객체")
public record MemberSearchRequest(

        @Parameter(description = "이메일 키워드", required = false)
        String email,
        @Parameter(description = "이름 키워드", required = false)
        String name,
        @Parameter(description = "성별 키워드", required = false)
        String gender

) {
}
