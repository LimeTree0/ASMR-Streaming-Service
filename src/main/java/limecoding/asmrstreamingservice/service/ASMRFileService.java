package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.entity.ASMRFile;
import limecoding.asmrstreamingservice.repository.ASMRFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class ASMRFileService {

    private final ASMRFileRepository asmrFileRepository;

    Path uploadDir = Path.of("uploads");

    public ASMRFileService(ASMRFileRepository asmrFileRepository) throws IOException {
        this.asmrFileRepository = asmrFileRepository;

        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
    }

    public Long uploadASMRFile(MultipartFile file) throws IOException {

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filepath = uploadDir.resolve(filename);

        try {
            Files.copy(file.getInputStream(), filepath);

            ASMRFile asmrFile = ASMRFile.builder()
                    .fileName(filename)
                    .filePath(filepath.toString())
                    .build();

            ASMRFile savedFile = asmrFileRepository.save(asmrFile);

            return savedFile.getId();

        } catch (Exception e) {
            try {
                Files.deleteIfExists(filepath);
            } catch (IOException ex) {
                log.error("Failed to delete file: {}", filepath, ex);
            }

            throw e;
        }
    }

    public ASMRFile findASMRFileById(Long asmrFileId) {
        ASMRFile asmrFile = asmrFileRepository.findById(asmrFileId).orElseThrow(
                () -> new EntityNotFoundException("해당 파일을 찾을 수 없습니다."));

        log.info("getASMRFile: {}", asmrFile.getFileName());
        return asmrFile;
    }
}
