package limecoding.asmrstreamingservice.dto.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostUpdateRequestDTO {

    private Long id;
    private String title;
    private String content;

}
