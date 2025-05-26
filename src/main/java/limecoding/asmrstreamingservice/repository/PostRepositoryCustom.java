package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.dto.post.PostSearchCondition;
import limecoding.asmrstreamingservice.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> search(PostSearchCondition condition, Pageable pageable);
}
