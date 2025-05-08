package limecoding.asmrstreamingservice.dto.post.comment;

import lombok.*;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String username;
    private String comment;
    private Long postId;
}
