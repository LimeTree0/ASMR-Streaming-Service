package limecoding.asmrstreamingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Notification {

    @Id @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    private LocalDateTime createAt = LocalDateTime.now();
}