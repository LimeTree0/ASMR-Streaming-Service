package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName(value = "유저 아이디로 유저 조회시 유저를 User 객체 형태로 반환한다.")
    void findUser() {
        // Given: mock user Object and configure repository to return it when ID is 1
        User user = User.builder().id(1L).userId("test").password("test").build();

        when(userRepository.findUserByUserId("test")).thenReturn(Optional.of(user));

        // When: call findUserEntityByUserId with userId
        User findUser = userService.findUserEntityByUserId("test");

        // Then: verify that the returned findUser is correct
        assertThat(findUser).isNotNull();
        assertThat(findUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName(value = "없는 유저 아이디로 엔티티 조회시 EntityNotFoundException 예외를 던진다.")
    void findUserEntityByUserId_with_not_exist_user_throw_EntityNotFoundException() {
        // Given: userRepository returns Optional.empty() when searching for a non-existing user id
        when(userRepository.findUserByUserId("notExistUser")).thenReturn(Optional.empty());

        //When & Then: call findUserEntityByUserId with not exist user to verify EntityNotFoundException is thrown
        assertThatThrownBy(() -> userService.findUserEntityByUserId("notExistUser"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName(value ="유저 아이디가 null일 때 IllegalArgumentException 예외를 던진다.")
    void findUserEntityByUserId_with_null_throw_IllegalArgumentException() {
        // Given
        String userId = null;

        // When & Then: calling findUserEntityByUserId with null to verify IllegalArgumentException is thrown
        assertThatThrownBy(() -> userService.findUserEntityByUserId(userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName(value ="유저 아이디가 빈 문자열일 때 IllegalArgumentException 예외를 던진다.")
    void findUserEntityByUserId_with_empty_string_throw_IllegalArgumentException() {
        // Given
        String userId = "";

        // When & Then: calling findUserEntityByUserId with empty string to verify IllegalArgumentException is thrown
        assertThatThrownBy(() -> userService.findUserEntityByUserId(userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // TODO: 새로운 유저 생성시

    // TODO: 중복된 유저 생성시

    // TODO: 유저 정보 수정시

    // TODO: 없는 유저 정보 수정시

    //TODO: 유저 삭제시

    //TODO: 없는 유저 삭제시


}