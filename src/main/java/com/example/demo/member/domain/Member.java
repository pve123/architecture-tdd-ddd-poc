package com.example.demo.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;


@Builder(toBuilder = true)
@Getter
@Schema(description = "회원 정보 Domain")
public class Member {


    @Schema(description = "ULID 기반 식별자", example = "01HZY74JZP5VDFKHX6D5YFRAZW")
    private String id;
    @Schema(description = "이메일", example = "user@example.com")
    private String email;
    @Schema(description = "비밀번호", example = "qwer123456789!")
    private String password;
    @Schema(description = "회원 이름", example = "홍길동")
    private String name;
    @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE"})
    private GenderEnum gender;
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;
    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;

}
