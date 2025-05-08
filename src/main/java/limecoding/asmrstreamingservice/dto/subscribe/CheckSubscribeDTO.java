package limecoding.asmrstreamingservice.dto.subscribe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class CheckSubscribeDTO {

    @JsonProperty(defaultValue = "isSubscribe")
    private boolean subscribe;

    public static CheckSubscribeDTO of(Boolean subscribe) {
        CheckSubscribeDTO checkSubscribeDTO = new CheckSubscribeDTO();
        checkSubscribeDTO.setSubscribe(subscribe);

        return checkSubscribeDTO;
    }
}
