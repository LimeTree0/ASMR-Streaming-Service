package limecoding.asmrstreamingservice.dto.refreshToken;

import limecoding.asmrstreamingservice.entity.RefreshToken;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefreshTokenDTO {
    String userId;
    String refreshToken;

    public static RefreshTokenDTO from(RefreshToken refreshToken) {
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setUserId(refreshToken.getUserId());
        refreshTokenDTO.setRefreshToken(refreshToken.getToken());
        return refreshTokenDTO;
    }
}