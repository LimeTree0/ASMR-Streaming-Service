package limecoding.asmrstreamingservice.service;

import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.config.JwtProvider;
import limecoding.asmrstreamingservice.dto.refreshToken.RefreshTokenDTO;
import limecoding.asmrstreamingservice.entity.RefreshToken;
import limecoding.asmrstreamingservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.token.refresh.expiredMs}")
    private Duration refreshTokenExpireMs;

    private RefreshTokenDTO getRefreshToken(String userId) {

        redisTemplate.opsForValue().get("RT:" + userId);

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰을 찾을 수 없습니다: " + userId));

        return RefreshTokenDTO.from(refreshToken);
    }

    @Transactional
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(
                "RT:" + userId,
                refreshToken,
                Duration.ofMillis(refreshTokenExpireMs.toMillis())
        );
    }

    public boolean isInvalid(String userId, String refreshToken) {
        RefreshTokenDTO refreshTokenDTO = getRefreshToken(userId);

        log.info("searched refreshToken: {}", refreshTokenDTO.getRefreshToken());
        log.info("user refreshToken: {}", refreshToken);

        return !refreshTokenDTO.getRefreshToken().equals(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String userId) {

        redisTemplate.delete("RT:" + userId);
    }

    public String generateAccessToken(String userId) {
        return jwtProvider.createAccessToken(userId);
    }

    public String generateRefreshToken(String userId) {
        return jwtProvider.createRefreshToken(userId);
    }

    public String getUserId(String token) {
        return jwtProvider.getUserId(token);
    }

    public boolean validateToken(String token) {
        return validateToken(token);
    }
}
