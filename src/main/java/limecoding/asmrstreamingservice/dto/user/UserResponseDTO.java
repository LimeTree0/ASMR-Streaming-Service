package limecoding.asmrstreamingservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserResponseDTO {
    private String userId;
    private String password;
}
