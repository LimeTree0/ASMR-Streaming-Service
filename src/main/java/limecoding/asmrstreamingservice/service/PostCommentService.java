package limecoding.asmrstreamingservice.service;

import limecoding.asmrstreamingservice.dto.post.comment.CommentDTO;
import limecoding.asmrstreamingservice.dto.post.comment.CommentRequestDTO;
import limecoding.asmrstreamingservice.entity.Post;
import limecoding.asmrstreamingservice.entity.PostComment;
import limecoding.asmrstreamingservice.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostCommentService {

    private final PostService postService;
    private final PostCommentRepository postCommentRepository;

    public CommentDTO writeCommentByPostId(Long postId, String username, CommentRequestDTO commentRequestDTO) {

        Post postEntity = postService.findPostEntityById(postId);

        PostComment postComment = PostComment.builder()
                .post(postEntity)
                .username(username)
                .comment(commentRequestDTO.getComment())
                .build();

        PostComment save = postCommentRepository.save(postComment);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment(save.getComment());
        commentDTO.setId(save.getId());
        commentDTO.setPostId(postEntity.getId());

        return commentDTO;
    }

    public Page<CommentDTO> getCommentListByPostId(Long postId, Pageable pageable) {

        // 게시글 존재 여부 확인
        postService.checkPostExistsById(postId);
        Page<PostComment> postCommentList = postCommentRepository.findPostCommentByPostId(postId, pageable);

        return postCommentList.map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setComment(comment.getComment());
            commentDTO.setUsername(comment.getUsername());
            commentDTO.setPostId(comment.getPost().getId());

            return commentDTO;
        });
    }
}
