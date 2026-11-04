package io.github.pve123.hexagonal.member.adapter.in.web;

import io.github.pve123.hexagonal.member.application.port.in.CreateMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMembersUseCase;
import io.github.pve123.hexagonal.member.application.port.in.UpdateMemberUseCase;
import io.github.pve123.hexagonal.member.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** 회원 API가 받는 요청 모양을 모아 둡니다. 요청은 검증한 뒤 유스케이스의 Command로 바꿔 넘깁니다. */
final class MemberRequest {

    private MemberRequest() {
    }

    record Create(
            @Schema(example = "user@example.com")
            @NotBlank(message = "이메일은 필수입니다.")
            @Size(min = 5, max = 254, message = "이메일은 5자 이상 254자 이하여야 합니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            String email,

            @Schema(example = "QWERasdf1234!")
            @NotBlank(message = "비밀번호는 필수입니다.")
            @Size(min = 8, max = 128, message = "비밀번호는 8자 이상 128자 이하여야 합니다.")
            @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,128}$",
                    message = "최소 하나의 대문자, 소문자, 숫자, 특수문자 포함")
            String password,

            @Schema(example = "홍길동")
            @NotBlank(message = "이름은 필수입니다.")
            @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다.")
            @Pattern(regexp = "^[가-힣a-zA-Z\\s]{2,50}$", message = "한글, 영문, 공백 허용")
            String name,

            @NotNull(message = "성별은 필수입니다.")
            Gender gender,

            @Schema(example = "010-1234-5678")
            @NotBlank(message = "전화번호는 필수입니다.")
            @Size(min = 10, max = 20, message = "전화번호는 10자 이상 15자 이하여야 합니다.")
            @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "한국 형식")
            String phoneNumber,

            @Schema(example = "서울특별시 강남구 테헤란로 123")
            @NotBlank(message = "주소는 필수입니다.")
            @Size(min = 10, max = 200, message = "주소는 10자 이상 200자 이하여야 합니다.")
            @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s,.\\-()#]{10,200}$", message = "한글, 영문, 숫자, 일반 구두점 허용")
            String address
    ) {
        CreateMemberUseCase.Command toCommand() {
            return new CreateMemberUseCase.Command(email, password, name, gender, phoneNumber, address);
        }
    }

    /** 바꾸지 않을 값은 비워 둡니다. */
    record Update(
            @Size(min = 5, max = 254, message = "이메일은 5자 이상 254자 이하여야 합니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            String email,

            @Size(min = 10, max = 20, message = "전화번호는 10자 이상 15자 이하여야 합니다.")
            @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "한국 형식")
            String phoneNumber,

            @Size(min = 10, max = 200, message = "주소는 10자 이상 200자 이하여야 합니다.")
            @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s,.\\-()#]{10,200}$", message = "한글, 영문, 숫자, 일반 구두점 허용")
            String address
    ) {
        UpdateMemberUseCase.Command toCommand(String memberId) {
            return new UpdateMemberUseCase.Command(memberId, email, phoneNumber, address);
        }
    }

    record Search(String email, String name, String gender) {
        GetMembersUseCase.SearchCondition toCondition() {
            return new GetMembersUseCase.SearchCondition(email, name, gender);
        }
    }
}
