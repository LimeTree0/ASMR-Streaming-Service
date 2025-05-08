package limecoding.asmrstreamingservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import limecoding.asmrstreamingservice.entity.QSubscribe;
import limecoding.asmrstreamingservice.entity.Subscribe;
import limecoding.asmrstreamingservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubscriberRepositoryImpl implements SubscriberRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<Subscribe> searchSubscribeBySubscriberAndTargetUser(User subscriber, User targetUser) {
        QSubscribe subscribe = QSubscribe.subscribe;
        Subscribe fetChOneSubscribe = queryFactory
                .selectFrom(subscribe)
                .where(
                        subscribe.subscriber.eq(subscriber),
                        subscribe.targetUser.eq(targetUser)
                ).fetchOne();

        return Optional.ofNullable(fetChOneSubscribe);
    }
}
