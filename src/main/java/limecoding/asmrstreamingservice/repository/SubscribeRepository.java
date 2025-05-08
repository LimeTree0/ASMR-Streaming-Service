package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.entity.Subscribe;
import limecoding.asmrstreamingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long>, SubscriberRepositoryCustom {

    List<Subscribe> findAllByTargetUser(User targetUser);
    List<Subscribe> findAllBySubscriberUserId(String subscriberUserId);
    boolean existsSubscribeBySubscriberAndTargetUser(User subscriber, User targetUser);
}
