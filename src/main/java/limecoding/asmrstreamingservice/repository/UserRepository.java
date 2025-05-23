package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId(String userId);

    boolean existsByUserId(String userId);

    void deleteUserByUserId(String userId);
}
