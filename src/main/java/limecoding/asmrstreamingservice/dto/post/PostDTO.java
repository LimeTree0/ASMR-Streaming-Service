package limecoding.asmrstreamingservice.dto.post;

import limecoding.asmrstreamingservice.entity.ASMRFile;
import limecoding.asmrstreamingservice.entity.Post;
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
    private Long asmrFileId;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.asmrFileId = post.getAsmrFile().getId();
    }
}
