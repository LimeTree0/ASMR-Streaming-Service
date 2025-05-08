package limecoding.asmrstreamingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class NotificationRead {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Notification notification;

    @Column(nullable = false)
    private boolean isRead = false;
}
