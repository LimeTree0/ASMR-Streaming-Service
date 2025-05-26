package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    // Custom query methods can be defined here if needed
}
