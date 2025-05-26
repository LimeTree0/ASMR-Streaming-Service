package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.entity.FileEntity;
import limecoding.asmrstreamingservice.exception.custom.FileDeleteFailedException;
import limecoding.asmrstreamingservice.exception.custom.FileSaveFailedException;
import limecoding.asmrstreamingservice.repository.FileRepository;
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
public class FileService {

    private final FileRepository fileRepository;
    private final Path uploadDir;

    public FileService(FileRepository fileRepository,
                       @Value("${upload.dir}") String uploadPath) throws IOException {
        this.fileRepository = fileRepository;
        this.uploadDir = Path.of(uploadPath);

        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
            log.info("upload directory created at {}", uploadDir.toAbsolutePath());
        } else {
            log.info("upload directory already exists at {}", uploadDir.toAbsolutePath());
        }
    }

    /**
     * Uploads an file to the server and saves its metadata to the database.
     * <p>
     * The uploaded file is stored in the local file system under a unique filename,
     * and its path and name are saved as an {@link ASMR} entity.
     *
     * @param file the {@link MultipartFile} to upload (must not be {@code null})
     * @return the Entity of the saved {@link ASMR} entity
     * @throws NullPointerException if the input file is {@code null}
     * @throws {@link FileSaveFailedException}          if an I/O error occurs while copying the file
     */
    public FileEntity uploadFile(MultipartFile file) throws FileSaveFailedException {

        // if file is null, throw Null PointException.
        if (file == null) {
            throw new NullPointerException("MultipartFile은 null일 될 수 없습니다.");
        }

        // make uuid and file path.
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filepath = uploadDir.resolve(filename);

        // trying to save file
        try {
            Files.copy(file.getInputStream(), filepath);

            FileEntity fileEntity = FileEntity.builder()
                    .fileName(filename)
                    .filePath(filepath.toString())
                    .build();

            return fileRepository.save(fileEntity);

        } catch (Exception e) {
            try {
                Files.deleteIfExists(filepath);
            } catch (IOException ex) {
                log.error("Failed to delete file: {}", filepath, ex);
            }

            throw new FileSaveFailedException("파일 저장을 실패했습니다.");
        }
    }

    /**
     * find file entity by id from database.
     * @param fileId file id used in database
     * @return entity saved in data base {@link FileEntity}
     */
    public FileEntity findFileById(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId).orElseThrow(
                () -> new EntityNotFoundException("해당 파일을 찾을 수 없습니다."));

        log.info("getASMRFile: {}", fileEntity.getFileName());
        return fileEntity;
    }
}
