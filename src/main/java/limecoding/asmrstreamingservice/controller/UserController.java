package limecoding.asmrstreamingservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import limecoding.asmrstreamingservice.common.response.ApiResponse;
import limecoding.asmrstreamingservice.dto.user.CreateUserDto;
import limecoding.asmrstreamingservice.dto.user.UserDto;
import limecoding.asmrstreamingservice.dto.user.UserResponseDTO;
import limecoding.asmrstreamingservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;
    private final View error;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody CreateUserDto createUserDto, BindingResult bindingResult) {

        // 파라미터 검증
        if (bindingResult.hasErrors()) {

            Map<String, String> errors = new HashMap<>();

            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });

            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다", errors));
        }

        // 유저 생성
        userService.createUser(createUserDto);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.NO_CONTENT, "사용자 생성 성공", null));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByUserId(@PathVariable String userId) {

        // 아이디로 유저 검색
        UserDto userDto = userService.getUserByUserId(userId);

        // 응답 DTO 변환
        UserResponseDTO userResponseDTO = new UserResponseDTO(userDto.getUserId(), userDto.getPassword());
        userResponseDTO.setUserId(userId);
        userResponseDTO.setPassword(userDto.getPassword());

        return ResponseEntity.ok(ApiResponse.success(userResponseDTO));
    }

    //TODO: 유저 삭제

    //TODO: 유저 수정
}
