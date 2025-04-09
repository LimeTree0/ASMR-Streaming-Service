package limecoding.asmrstreamingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class RefreshToken {

    @Id
    private String userId;

    @Column(nullable = false)
    private String token;

    public RefreshToken() {}

    public RefreshToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public void update(String newToken) {
        this.token = newToken;
    }
}
