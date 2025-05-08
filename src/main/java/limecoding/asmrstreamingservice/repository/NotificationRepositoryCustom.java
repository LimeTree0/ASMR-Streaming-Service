package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.entity.Notification;
import limecoding.asmrstreamingservice.entity.User;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> findUnreadNotificationBySubscriber(User subscriber);

}
