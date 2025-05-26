package limecoding.asmrstreamingservice.dto.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestAsmrPostDTO {
    private String title;
    private String content;
    private Long asmrFileId;
}
