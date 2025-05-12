package limecoding.asmrstreamingservice.dto.wishlist;

import limecoding.asmrstreamingservice.entity.wishlist.Wishlist;
import lombok.*;

@NoArgsConstructor
@ToString
@Setter
@Getter
public class WishlistDTO {
    private Long id;
    private Long userId;
    private Long postId;

    public static WishlistDTO from(Wishlist wishlist) {

        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setId(wishlist.getId());
        wishlistDTO.setUserId(wishlist.getUser().getId());
        wishlistDTO.setPostId(wishlist.getPost().getId());

        return wishlistDTO;
    }
}