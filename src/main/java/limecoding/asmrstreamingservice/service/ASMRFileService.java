package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.entity.ASMRFile;
import limecoding.asmrstreamingservice.repository.ASMRFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final Path uploadDir;

    public ASMRFileService(ASMRFileRepository asmrFileRepository,
                           @Value("${upload.dir}") String uploadPath) throws IOException {
        this.asmrFileRepository = asmrFileRepository;
        this.uploadDir = Path.of(uploadPath);

        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
            log.info("upload directory created at {}", uploadDir.toAbsolutePath());
        } else {
            log.info("upload directory already exists at {}", uploadDir.toAbsolutePath());
        }
    }

    /**
     * Uploads an ASMR audio file to the server and saves its metadata to the database.
     * <p>
     * The uploaded file is stored in the local file system under a unique filename,
     * and its path and name are saved as an {@link ASMRFile} entity.
     *
     * @param file the {@link MultipartFile} to upload (must not be {@code null})
     * @return the ID of the saved {@link ASMRFile} entity
     * @throws NullPointerException if the input file is {@code null}
     * @throws IOException          if an I/O error occurs while copying the file
     */
    public Long uploadASMRFile(MultipartFile file) throws IOException {

        if (file == null) {
            throw new NullPointerException("MultipartFile은 null일 될 수 없습니다.");
        }

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
