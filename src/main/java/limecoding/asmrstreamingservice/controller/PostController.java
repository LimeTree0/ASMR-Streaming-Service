package limecoding.asmrstreamingservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import limecoding.asmrstreamingservice.common.response.ApiResponse;
import limecoding.asmrstreamingservice.dto.post.PostDTO;
import limecoding.asmrstreamingservice.dto.post.PostRequestDTO;
import limecoding.asmrstreamingservice.dto.post.PostSearchCondition;
import limecoding.asmrstreamingservice.dto.post.PostUpdateRequestDTO;
import limecoding.asmrstreamingservice.dto.post.comment.CommentDTO;
import limecoding.asmrstreamingservice.dto.post.comment.CommentRequestDTO;
import limecoding.asmrstreamingservice.service.PostCommentService;
import limecoding.asmrstreamingservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "게시글 API", description = "게시글 API")
public class PostController {

    private final PostService postService;
    private final PostCommentService postCommentService;

    /**
     * 게시글을 추가한다.
     *
     * @param requestDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPost(
            @AuthenticationPrincipal User user,
            @RequestBody PostRequestDTO requestDTO) {

        String userId = user.getUsername();

        log.info("userId: {}", userId);

        postService.createPost(userId, requestDTO);

        return ResponseEntity.ok().build();
    }

    /**
     * 게시글을 수정한다.
     *
     * @param user
     * @param updateRequestDTO
     * @return
     */
    @PutMapping
    public ResponseEntity<ApiResponse<?>> updatePost(
            @AuthenticationPrincipal User user,
            @RequestBody PostUpdateRequestDTO updateRequestDTO) {


        String userId = user.getUsername();

        postService.updatePost(userId, updateRequestDTO);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.NO_CONTENT, "게시글이 수정되었습니다", null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deletePost(
            @AuthenticationPrincipal User user,
            @RequestParam Long postId
    ) throws AccessDeniedException {

        String userId = user.getUsername();

        postService.deletePost(userId, postId);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.NO_CONTENT, "게시글이 삭제되었습니다", null));
    }


    /**
     * 모든 게시글을 조회한다.
     *
     * @param page
     * @param size
     * @param sortBy
     * @param order
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<?>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<PostDTO> posts = postService.getAllPosts(PageRequest.of(page, size, Sort.by(direction, sortBy)));

        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> getSearchPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ParameterObject @ModelAttribute PostSearchCondition postSearchCondition
    ) {
        Sort.Direction desc = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Page<PostDTO> searchPosts = postService.getSearchPosts(PageRequest.of(page - 1, size, Sort.by(desc, sortBy)), postSearchCondition);

        return ResponseEntity.ok(ApiResponse.success(searchPosts));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<?>> getPostById(
            @PathVariable Long postId
    ) {
        PostDTO postDTO = postService.getPostById(postId);

        return ResponseEntity.ok(ApiResponse.success(postDTO));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<?>> getCommentsByPostId(
            @PathVariable("postId") Long postId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<CommentDTO> commentListByPostId = postCommentService.getCommentListByPostId(postId, PageRequest.of(page - 1, size));

        return ResponseEntity.ok(ApiResponse.success(commentListByPostId));
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<ApiResponse<?>> writeCommentsByPostId(
            @AuthenticationPrincipal User user,
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequestDTO commentRequestDTO) {

        String username = user.getUsername();

        log.info("comment user : {}", username);

        CommentDTO commentDTO = postCommentService.writeCommentByPostId(postId, username,commentRequestDTO);

        return ResponseEntity.ok(ApiResponse.success(commentDTO));
    }
}
