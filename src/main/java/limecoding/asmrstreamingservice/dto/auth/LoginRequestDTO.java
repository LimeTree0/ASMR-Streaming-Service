package limecoding.asmrstreamingservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequestDTO {

    @Schema(description = "사용자 아이디", example = "bum9516")
    @NotNull(message = "아이디는 필수 입력값입니다.")
    private String userId;

    @Schema(description = "사용자 비밀번호", example = "hong9516!")
    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
