package limecoding.asmrstreamingservice.dto.notification;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import limecoding.asmrstreamingservice.entity.Notification;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.repository.NotificationReadRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class NotificationDTO {
    private Long id;
    private String content;
    private String userId;
    private LocalDateTime createAt = LocalDateTime.now();

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.userId = notification.getCreator().getUserId();
        this.createAt = notification.getCreateAt();
    }
}
