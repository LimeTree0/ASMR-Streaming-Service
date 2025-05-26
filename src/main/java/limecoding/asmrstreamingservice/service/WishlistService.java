package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import limecoding.asmrstreamingservice.dto.wishlist.RequestWishlistDTO;
import limecoding.asmrstreamingservice.dto.wishlist.WishlistDTO;
import limecoding.asmrstreamingservice.entity.post.Post;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.entity.wishlist.Wishlist;
import limecoding.asmrstreamingservice.exception.custom.WishlistUserNotMatchException;
import limecoding.asmrstreamingservice.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserService userService;
    private final PostService postService;

    // 선택한 포스팅을 찜하기
    public WishlistDTO addWishItem(String username, RequestWishlistDTO wishlistDTO) {

        User requestUser = userService.findUserEntityByUserId(username);
        Post wishPost = postService.findPostEntityById(wishlistDTO.getPostId());

        Wishlist wishlist = Wishlist.builder()
                .post(wishPost)
                .user(requestUser)
                .build();

        Wishlist save = wishlistRepository.save(wishlist);

        return WishlistDTO.from(save);
    }

    // 찜 취소
    public void deleteWishItem(String username, Long wishlistId) {

        User user = userService.findUserEntityByUserId(username);
        Wishlist wishlist = findWishlistEntityById(wishlistId);

        if (isOwner(user, wishlist)) {
            wishlistRepository.deleteById(wishlistId);
        } else {
            throw new WishlistUserNotMatchException("사용자의 위시리스트가 아닙니다");
        }
    }

    // 찜 리스트
    @Transactional(readOnly = true)
    public Page<WishlistDTO> findWishItemList(String username, Pageable pageable) {
        User user = userService.findUserEntityByUserId(username);
        Page<Wishlist> wishlist = wishlistRepository.findByUser(user, pageable);

        return wishlist.map(WishlistDTO::from);
    }

    @Transactional(readOnly = true)
    public Wishlist findWishlistEntityByUserAndPost(User user, Post post) {
        return wishlistRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new EntityNotFoundException("위시리스트를 찾을 수 없습니다"));
    }

    @Transactional(readOnly = true)
    public Wishlist findWishlistEntityById(Long wishlistId) {
        return wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("위시리스트를 찾을 수 없습니다. wishistId : " + wishlistId));
    }

    private boolean isOwner(User user, Wishlist wishlist) {
        return Objects.equals(user, wishlist.getUser());
    }
}
