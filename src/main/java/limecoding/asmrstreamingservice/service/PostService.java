package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.dto.post.PostDTO;
import limecoding.asmrstreamingservice.dto.post.PostRequestDTO;
import limecoding.asmrstreamingservice.dto.post.PostSearchCondition;
import limecoding.asmrstreamingservice.dto.post.PostUpdateRequestDTO;
import limecoding.asmrstreamingservice.dto.user.UserDto;
import limecoding.asmrstreamingservice.entity.ASMRFile;
import limecoding.asmrstreamingservice.entity.Post;
import limecoding.asmrstreamingservice.entity.User;
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
    private final ASMRFileService asmrFileService;

    /**
     * 게시글을 추가한다.
     *
     * @param postRequestDTO
     */
    public void createPost(String userId, PostRequestDTO postRequestDTO) {

        log.info("createPost : {}", postRequestDTO.toString());

        User user = userService.findUserEntityByUserId(userId);
        ASMRFile asmrFile = asmrFileService.findASMRFileById(postRequestDTO.getFileId());

        Post post = Post.builder()
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .user(user)
                .asmrFile(asmrFile)
                .build();

        postRepository.save(post);
    }

    /**
     * 게시글을 수정한다.
     *
     * @param postRequestDTO
     */
    public void updatePost(String userId, PostUpdateRequestDTO postRequestDTO) {

        UserDto userDto = userService.getUserByUserId(userId);

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

        UserDto userDto = userService.getUserByUserId(userId);

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
    public PostDTO getPostById(Long postId) {
        Post postEntity = getPostEntityById(postId);

        return new PostDTO(postEntity);
    }

    public Post getPostEntityById(Long postId) {
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
}
