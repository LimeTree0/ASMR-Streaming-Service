package limecoding.asmrstreamingservice.dto.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostRequestDTO {
    private String title;
    private String content;
    private Long fileId;
}
