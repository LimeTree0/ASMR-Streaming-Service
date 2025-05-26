package limecoding.asmrstreamingservice.dto.post;

import limecoding.asmrstreamingservice.entity.post.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostDTO {
    private Long id;
    private String title;
    private String content;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
