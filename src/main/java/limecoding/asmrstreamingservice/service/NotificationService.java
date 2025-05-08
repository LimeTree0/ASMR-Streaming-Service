package limecoding.asmrstreamingservice.service;

import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.dto.notification.NotificationDTO;
import limecoding.asmrstreamingservice.entity.Notification;
import limecoding.asmrstreamingservice.entity.NotificationRead;
import limecoding.asmrstreamingservice.entity.Subscribe;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.repository.EmitterRepository;
import limecoding.asmrstreamingservice.repository.NotificationReadRepository;
import limecoding.asmrstreamingservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationReadRepository notificationReadRepository;
    private final EmitterRepository emitterRepository;
    private final SubscribeService subscribeService;
    private final UserService userService;

    public void notifyAll(User receiver, String content) {
        emitterRepository.get(receiver.getId()).ifPresent(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(content));
            } catch (IOException e) {
                emitterRepository.remove(receiver.getId());
            }
        });
    }

    public void notifyAll(String targetUserId, String message) {
        List<Subscribe> subscriberList = subscribeService.getSubscriberList(targetUserId);
        User targetUser = userService.findUserEntityByUserId(targetUserId);

        // 알림 저장
        Notification notification = new Notification();
        notification.setCreator(targetUser);
        notification.setContent(message);
        notificationRepository.save(notification);

        log.info("subscriberList : {}", subscriberList);

        for (Subscribe subscribe : subscriberList) {

            // 알림 안 읽었음으로 저장
            NotificationRead notificationRead = NotificationRead.builder()
                    .notification(notification)
                    .receiver(subscribe.getSubscriber())
                    .isRead(false)
                    .build();
            notificationReadRepository.save(notificationRead);
            notifyAll(subscribe.getSubscriber(), message);
        }
    }

    public List<Notification> findNotificationUnRead(User subscriber) {
        return notificationRepository.findUnreadNotificationBySubscriber(subscriber);
    }

    public List<NotificationDTO> findNotificationUnRead(String subscriber) {
        User subscriberEntity = userService.findUserEntityByUserId(subscriber);
        List<Notification> notificationUnRead = findNotificationUnRead(subscriberEntity);

        return notificationUnRead.stream().map(NotificationDTO::new).toList();
    }
}
