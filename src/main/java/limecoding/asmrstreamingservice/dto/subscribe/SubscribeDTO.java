package limecoding.asmrstreamingservice.dto.subscribe;

import limecoding.asmrstreamingservice.entity.Subscribe;
import limecoding.asmrstreamingservice.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class SubscribeDTO {
    private Long id;
    private String subscriberId;
    private String targetUSerId;

    public static SubscribeDTO toDTO(Subscribe subscribe) {
        SubscribeDTO subscribeDTO = new SubscribeDTO();
        subscribeDTO.setSubscriberId(subscribe.getSubscriber().getUserId());
        subscribeDTO.setTargetUSerId(subscribe.getTargetUser().getUserId());
        subscribeDTO.setId(subscribe.getId());

        return subscribeDTO;
    }
}