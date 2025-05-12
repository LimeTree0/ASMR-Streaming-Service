package limecoding.asmrstreamingservice.controller;

import limecoding.asmrstreamingservice.common.response.ApiResponse;
import limecoding.asmrstreamingservice.dto.post.PostDTO;
import limecoding.asmrstreamingservice.dto.wishlist.RequestWishlistDTO;
import limecoding.asmrstreamingservice.dto.wishlist.WishlistDTO;
import limecoding.asmrstreamingservice.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> addWishlist(
            @AuthenticationPrincipal User user,
            @RequestBody RequestWishlistDTO requestWishlistDTO
            ) {

        String username = user.getUsername();

        WishlistDTO wishlistDTO = wishlistService.addWishItem(username, requestWishlistDTO);

        return ResponseEntity.ok(ApiResponse.success(wishlistDTO));
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<ApiResponse<Object>> deleteWishlistItem(
            @AuthenticationPrincipal User user,
            @PathVariable(name = "wishlistId") Long wishlistId
    ) {

        String username = user.getUsername();

        wishlistService.deleteWishItem(username, wishlistId);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> wishlistItem(
            @AuthenticationPrincipal User user,
            @RequestParam("size") int size,
            @RequestParam("page") int page
    ) {

        String username = user.getUsername();

        Page<WishlistDTO> wishItemList = wishlistService.findWishItemList(username, PageRequest.of(page - 1, size));

        return ResponseEntity.ok(ApiResponse.success(wishItemList));
    }

}
