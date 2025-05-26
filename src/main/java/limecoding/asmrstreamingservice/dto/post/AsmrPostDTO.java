package limecoding.asmrstreamingservice.dto.post;

import limecoding.asmrstreamingservice.dto.asmr.AsmrDTO;
import limecoding.asmrstreamingservice.entity.post.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Setter
@Getter
public class AsmrPostDTO extends PostDTO {

    private AsmrDTO asmrDTO;

    public AsmrPostDTO(Post post) {
        super(post);
    }
}
