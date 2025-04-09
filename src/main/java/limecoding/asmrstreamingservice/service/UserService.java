package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import limecoding.asmrstreamingservice.dto.user.CreateUserDto;
import limecoding.asmrstreamingservice.dto.user.UserDto;
import limecoding.asmrstreamingservice.dto.user.UserUpdateRequestDTO;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 아이디로 유저 검색
    @Transactional(readOnly = true)
    public UserDto getUserByUserId(String userId) {
        User user = findUserEntityByUserId(userId);

        return UserDto.from(user);
    }

    @Transactional(readOnly = true)
    public User findUserEntityByUserId(String userId) {

        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("userId는 반드시 값이 존재해야 합니다.");
        }

        return userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));
    }

    // 유저 생성
    public void createUser(CreateUserDto userDto) {

        // 사용자 아이디 중복 체크
        if (!isUserIdAvailable(userDto.getUserId())) {
            throw new DuplicateKeyException("이미 사용중인 아이디입니다.");
        }

        String rawPassword = userDto.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        userDto.setPassword(encodedPassword);

        User user = UserDto.toEntity(userDto);
        userRepository.save(user);
    }

    // 유저 삭제
    public void deleteUser(String userId) {
        User user = findUserEntityByUserId(userId);

        userRepository.delete(user);
    }

    // 유저 수정
    public void updateUser(String userId, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = findUserEntityByUserId(userId);

        user.updateUser(userUpdateRequestDTO.getUserId(), userUpdateRequestDTO.getPassword());
    }

    // 아이디 중복 여부 확인
    @Transactional(readOnly = true)
    protected boolean isUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    public Page<UserDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDto::from);
    }
}
