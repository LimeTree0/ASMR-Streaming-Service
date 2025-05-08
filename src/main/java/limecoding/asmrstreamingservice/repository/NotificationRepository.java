package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
}
