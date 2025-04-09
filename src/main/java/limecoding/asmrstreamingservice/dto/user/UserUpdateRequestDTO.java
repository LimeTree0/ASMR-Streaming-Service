package limecoding.asmrstreamingservice.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import limecoding.asmrstreamingservice.validator.annotation.StrongPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateRequestDTO {

    @Size(min = 5, max = 20, message = "아이디는 2~20자 사이여야 합니다")
    @Schema(description = "사용자 아이디", example = "bum9516")
    private String userId;

    @StrongPassword
    @Schema(description = "비밀번호", example = "hong9516!")
    private String password;
}
