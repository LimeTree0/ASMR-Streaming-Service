package limecoding.asmrstreamingservice.entity;

import jakarta.persistence.*;
import limecoding.asmrstreamingservice.dto.user.UserUpdateRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ASMRUser")
public class User {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    public void updateUser(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

}