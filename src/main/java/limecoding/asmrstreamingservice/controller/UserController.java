package limecoding.asmrstreamingservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import limecoding.asmrstreamingservice.common.response.ApiResponse;
import limecoding.asmrstreamingservice.dto.user.CreateUserDto;
import limecoding.asmrstreamingservice.dto.user.UserDto;
import limecoding.asmrstreamingservice.dto.user.UserResponseDTO;
import limecoding.asmrstreamingservice.dto.user.UserUpdateRequestDTO;
import limecoding.asmrstreamingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
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

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }

    //TODO: 유저 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable String userId,
                                                     @Valid @RequestBody UserUpdateRequestDTO userUpdateDto) {
        userService.updateUser(userId, userUpdateDto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> me(@AuthenticationPrincipal User user) {
        String username = user.getUsername();

        return ResponseEntity.ok(ApiResponse.success(username));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<?>> getUserList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<UserDto> users = userService.getUsers(PageRequest.of(page, size, Sort.by(direction, sort)));

        return ResponseEntity.ok(ApiResponse.success(users));
    }
}
