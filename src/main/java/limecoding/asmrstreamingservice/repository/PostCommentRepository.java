package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Page<PostComment> findPostCommentByPostId(Long postId, Pageable pageable);

}
