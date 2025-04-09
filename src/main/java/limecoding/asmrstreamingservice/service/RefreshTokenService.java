package limecoding.asmrstreamingservice.service;

import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.dto.refreshToken.RefreshTokenDTO;
import limecoding.asmrstreamingservice.entity.RefreshToken;
import limecoding.asmrstreamingservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private RefreshTokenDTO getRefreshToken(String userId) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰을 찾을 수 없습니다: " + userId));

        return RefreshTokenDTO.from(refreshToken);
    }

    @Transactional
    public void saveRefreshToken(String userId, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken));
    }

    public boolean isValid(String userId, String refreshToken) {
        RefreshTokenDTO refreshTokenDTO = getRefreshToken(userId);

        log.info("searched refreshToken: {}", refreshTokenDTO.getRefreshToken());
        log.info("user refreshToken: {}", refreshToken);

        return refreshTokenDTO.getRefreshToken().equals(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
