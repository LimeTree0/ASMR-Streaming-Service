package limecoding.asmrstreamingservice.repository;

import limecoding.asmrstreamingservice.entity.ASMRFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ASMRFileRepository extends JpaRepository<ASMRFile, Long> {
    // Custom query methods can be defined here if needed
}
