package limecoding.asmrstreamingservice.dto.user;

import limecoding.asmrstreamingservice.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String userId;
    private String password;

    static public UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .password(user.getPassword())
                .build();
    }

    static public User toEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .userId(userDto.getUserId())
                .password(userDto.getPassword())
                .build();
    }

    static public User toEntity(CreateUserDto userDto) {
        return User.builder()
                .userId(userDto.getUserId())
                .password(userDto.getPassword())
                .build();
    }
}
