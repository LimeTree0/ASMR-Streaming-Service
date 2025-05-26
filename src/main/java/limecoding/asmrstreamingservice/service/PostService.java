package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.dto.asmr.AsmrDTO;
import limecoding.asmrstreamingservice.dto.post.*;
import limecoding.asmrstreamingservice.dto.user.UserDto;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.entity.post.Post;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.entity.post.PostType;
import limecoding.asmrstreamingservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ASMRService asmrService;
    private final NotificationService notificationService;

    /**
     * 게시글을 추가한다.
     *
     * @param requestAsmrPostDTO
     */
    public void createPost(String userId, RequestAsmrPostDTO requestAsmrPostDTO) {

        log.info("createPost userId: {}", userId);
        log.info("createPost : {}", requestAsmrPostDTO.toString());

        User user = userService.findUserEntityByUserId(userId);
        ASMR asmrEntity = asmrService.findASMREntityById(requestAsmrPostDTO.getAsmrFileId());

        Post post = Post.builder()
                .title(requestAsmrPostDTO.getTitle())
                .content(requestAsmrPostDTO.getContent())
                .user(user)
                .postType(PostType.ASMR)
                .build();

        Post save = postRepository.save(post);

        asmrEntity.updatePost(save);

        String content = user.getUserId() + "님이 새로운 영상을 올렸습니다: " + requestAsmrPostDTO.getTitle();
        notificationService.notifyAll(userId, content);
    }

    /**
     * 게시글을 수정한다.
     *
     * @param postRequestDTO
     */
    public void updatePost(String userId, PostUpdateRequestDTO postRequestDTO) {

        UserDto userDto = userService.findUserByUserId(userId);

        // 게시글이 존재하는지 확인
        Post post = postRepository.findById(postRequestDTO.getId()).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        // 게시글을 수정할 권한이 있는지 확인
        if (!post.getUser().getId().equals(userDto.getId())) {
            throw new IllegalArgumentException("해당 게시글을 수정할 권한이 없습니다.");
        }

        post.updatePost(postRequestDTO.getTitle(), postRequestDTO.getContent());
    }

    /**
     * 게시글을 삭제한다.
     *
     * @param postId
     */
    public void deletePost(String userId, Long postId) throws AccessDeniedException {

        UserDto userDto = userService.findUserByUserId(userId);

        // 게시글이 존재하는지 확인
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));


        // 게시글을 삭제할 권한이 있는지 확인
        if (!post.getUser().getId().equals(userDto.getId())) {
            throw new AccessDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    /**
     * 아이디를 기반으로 게시글 한 건을 조회한다.
     *
     * @param postId
     * @return
     */
    public PostDTO findPostById(Long postId) {
        Post postEntity = findPostEntityById(postId);

        return new PostDTO(postEntity);
    }

    public AsmrPostDTO findAsmrPostById(Long postId) {
        Post post = findPostEntityById(postId);
        AsmrDTO asmrDTO = asmrService.findASMRById(post.getId());

        AsmrPostDTO asmrPostDTO = new AsmrPostDTO(post);
        asmrPostDTO.setAsmrDTO(asmrDTO);

        return asmrPostDTO;
    }

    public Post findPostEntityById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    /**
     * 모든 게시글을 조회한다.
     *
     * @return
     */
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostDTO::new);
    }

    public Page<PostDTO> getSearchPosts(Pageable pageable, PostSearchCondition condition) {
        return postRepository.search(condition, pageable).map(PostDTO::new);
    }

    /**
     * 주어진 ID의 게시글이 존재하는지 확인합니다.
     *
     * @param postId 확인할 게시글의 ID
     * @throws EntityNotFoundException 게시글이 존재하지 않을 경우 발생
     */
    public void checkPostExistsById(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new EntityNotFoundException("게시글이 존재하지 않습니다. ID=" + postId);
        }
    }
}
