package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import limecoding.asmrstreamingservice.dto.subscribe.CheckSubscribeDTO;
import limecoding.asmrstreamingservice.dto.subscribe.RequestSubscribeDTO;
import limecoding.asmrstreamingservice.dto.subscribe.SubscribeDTO;
import limecoding.asmrstreamingservice.entity.Subscribe;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserService userService;

    // 구독 기능
    public SubscribeDTO subscribe(String subscriberUserId, RequestSubscribeDTO requestSubscribeDTO) {

        User subscriber = userService.findUserEntityByUserId(subscriberUserId);
        User targetUser = userService.findUserEntityByUserId(requestSubscribeDTO.getTargetUserId());

        Subscribe subscribe = Subscribe.builder()
                .subscriber(subscriber)
                .targetUser(targetUser)
                .build();

        Subscribe savedSubscribe = subscribeRepository.save(subscribe);

        return SubscribeDTO.toDTO(savedSubscribe);
    }

    // 구독 취소 기능
    public void cancel(String subscriberUserId, RequestSubscribeDTO requestSubscribeDTO) {

        Subscribe subscribe = findSubscribeEntityBySubscriberAndTargetUser(subscriberUserId, requestSubscribeDTO)
                .orElseThrow(() -> new EntityNotFoundException("해당 구독 정보가 존재하지 않습니다. " + "구독자 : " + subscriberUserId + "크리에이터 : " + requestSubscribeDTO.getTargetUserId()));

        log.info("subscribe : {}", subscribe);

        subscribeRepository.delete(subscribe);

    }

    // 구독 리스트
    @Transactional(readOnly = true)
    public List<SubscribeDTO> getSubscribeList(String userId) {

        return subscribeRepository.findAllBySubscriberUserId(userId)
                .stream().map(SubscribeDTO::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<Subscribe> getSubscriberList(String userId) {

        User user = userService.findUserEntityByUserId(userId);

        return subscribeRepository.findAllByTargetUser(user);
    }

    @Transactional(readOnly = true)
    public CheckSubscribeDTO isSubscribe(String subscriberId, String targetUserId) {

        User subscriber = userService.findUserEntityByUserId(subscriberId);
        User targetUser = userService.findUserEntityByUserId(targetUserId);
        boolean isSubscribed = subscribeRepository.existsSubscribeBySubscriberAndTargetUser(subscriber, targetUser);

        return CheckSubscribeDTO.of(isSubscribed);
    }

    // 특정 사용자 구독 정보 찾기
    private Optional<Subscribe> findSubscribeEntityBySubscriberAndTargetUser(String subscriberUserId, RequestSubscribeDTO requestSubscribeDTO) {
        User subscriber = userService.findUserEntityByUserId(subscriberUserId);
        User targetUser = userService.findUserEntityByUserId(requestSubscribeDTO.getTargetUserId());

        return subscribeRepository.searchSubscribeBySubscriberAndTargetUser(subscriber, targetUser);
    }

}