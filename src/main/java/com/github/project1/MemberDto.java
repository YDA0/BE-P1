package com.github.project1;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @NotBlank(message = "이름을 입력하세요")
    @Size(max = 50, message = "이름은 최대 50자까지 입력 가능합니다")
    private String name;

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    @Size(max = 40, message = "이메일은 최대 40자까지 입력 가능합니다")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 6, max = 20, message = "비밀번호는 6자 이상, 20자 이하로 입력하세요")
    private String password;

    @NotBlank(message = "전화번호를 입력하세요")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 잘못되었습니다")
    private String phoneNum;

    @NotBlank(message = "주소를 입력하세요")
    @Size(max = 100, message = "주소는 최대 100자까지 입력 가능합니다")
    private String address;
}
