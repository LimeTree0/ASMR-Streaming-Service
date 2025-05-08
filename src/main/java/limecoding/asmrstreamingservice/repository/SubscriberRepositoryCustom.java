package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.entity.Subscribe;
import limecoding.asmrstreamingservice.entity.User;

import java.util.Optional;

public interface SubscriberRepositoryCustom {
    Optional<Subscribe> searchSubscribeBySubscriberAndTargetUser(User subscriber, User targetUser);
}
