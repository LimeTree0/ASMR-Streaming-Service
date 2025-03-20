package limecoding.asmrstreamingservice.service.user;

import jakarta.persistence.EntityNotFoundException;
import limecoding.asmrstreamingservice.dto.user.CreateUserDto;
import limecoding.asmrstreamingservice.dto.user.UserDto;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 유저 아이디로 유저 검색
    public UserDto getUserByUserId(String userId) {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));

        return UserDto.from(user);
    }

    // 유저 생성
    public void createUser(CreateUserDto userDto) {

        // 사용자 아이디 중복 체크
        if (!isUserIdAvailable(userDto.getUserId())) {
            throw new DuplicateKeyException("이미 사용중인 아이디입니다.");
        }

        User user = UserDto.toEntity(userDto);
        userRepository.save(user);
    }

    // 아이디 중복 여부 확인
    public boolean isUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }
}
