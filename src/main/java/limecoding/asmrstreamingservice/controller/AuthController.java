package limecoding.asmrstreamingservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import limecoding.asmrstreamingservice.common.response.ApiResponse;
import limecoding.asmrstreamingservice.config.JwtProvider;
import limecoding.asmrstreamingservice.dto.auth.LoginRequestDTO;
import limecoding.asmrstreamingservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @Valid @RequestBody LoginRequestDTO dto,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });

            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다", errors));
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getPassword())
        );

        String accessToken = jwtProvider.createAccessToken(auth.getName());
        String refreshToken = jwtProvider.createRefreshToken(auth.getName());

        refreshTokenService.saveRefreshToken(auth.getName(), refreshToken);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 14)
                .sameSite("Lax")
                .build();

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());
        response.addHeader("Set-Cookie", accessCookie.toString());

        log.info("login user: {}", auth.getName());

        return ResponseEntity.ok(ApiResponse.success(Map.of("token", accessToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        log.info("Refresh token: {}", refreshToken);


        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다", null));
        }

        String userId = jwtProvider.getUserId(refreshToken);

        if (!refreshTokenService.isValid(userId, refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다", null));
        }

        refreshTokenService.deleteRefreshToken(userId);

        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", expiredCookie.toString());

        return ResponseEntity.ok(ApiResponse.success("로그아웃 성공"));
    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다", null));
        }

        String userId = jwtProvider.getUserId(refreshToken);

        if (!refreshTokenService.isValid(userId, refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다", null));
        }

        String newAccessToken = jwtProvider.createAccessToken(userId);
        String newRefreshToken = jwtProvider.createRefreshToken(userId);

        refreshTokenService.saveRefreshToken(userId, newRefreshToken);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 14)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok(ApiResponse.success(Map.of("token", newAccessToken)));
    }
}
