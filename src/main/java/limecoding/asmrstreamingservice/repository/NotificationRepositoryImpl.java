package limecoding.asmrstreamingservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import limecoding.asmrstreamingservice.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notification> findUnreadNotificationBySubscriber(User subscriber) {

        QNotification notification = QNotification.notification;
        QSubscribe subscribe = QSubscribe.subscribe;
        QUser user = QUser.user;
        QNotificationRead notificationRead = QNotificationRead.notificationRead;

        return jpaQueryFactory
                .selectFrom(notification)
                .innerJoin(subscribe)
                .on(subscribe.targetUser.eq(notification.creator)
                        .and(subscribe.subscriber.eq(subscriber)))
                .innerJoin(notificationRead)
                .on(notificationRead.notification.eq(notification)
                        .and(notificationRead.receiver.eq(subscriber))
                        .and(notificationRead.isRead.eq(false)))
                .fetch();
    }

    public BooleanExpression notificationReadIsReadEq(boolean isRead) {
        return QNotificationRead.notificationRead.isRead.eq(isRead);
    }

    public BooleanExpression userIsEq(User subscriber) {
        return QSubscribe.subscribe.subscriber.eq(subscriber);
    }
}
