package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.entity.post.Post;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.entity.wishlist.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Page<Wishlist> findByUser(User user, Pageable pageable);

    Optional<Wishlist> findByUserAndPost(User user, Post post);
}
