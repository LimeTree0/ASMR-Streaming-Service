package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.entity.FileEntity;
import limecoding.asmrstreamingservice.exception.custom.FileSaveFailedException;
import limecoding.asmrstreamingservice.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
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
     * Uploads a file to the server and saves its metadata to the database.
     * <p>
     * The uploaded file is stored in the local file system under a unique filename,
     * and its path and name are saved as an {@link ASMR} entity.
     *
     * @param file the {@link MultipartFile} to upload (must not be {@code null})
     * @return the Entity of the saved {@link ASMR} entity
     * @throws NullPointerException    if the input file is {@code null}
     * @throws FileSaveFailedException throw {@link FileSaveFailedException} if an I/O error occurs while copying the file
     */
    public FileEntity uploadFile(MultipartFile file) throws FileSaveFailedException {

        if (file == null || file.isEmpty()) {
            throw new NullPointerException("MultipartFile은 null이거나 비어 있을 수 없습니다.");
        }

        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String[] separateFileNameAndExtension = separateSafeFileNameAndExtension(originalFilename);

        String sanitizedFilename = UUID.randomUUID() + "_" + separateFileNameAndExtension[0] + "." + separateFileNameAndExtension[1];
        Path filepath = uploadDir.resolve(sanitizedFilename).normalize();

        log.info("filepath.toAbsolutePath() 경로 : {}", filepath.toAbsolutePath());
        log.info("uploadDir.toAbsolutePath() 경로 : {}", uploadDir.toAbsolutePath());

        // 경로 탈출 방지 체크
        if (!filepath.toAbsolutePath().startsWith(uploadDir.toAbsolutePath())) {
            throw new SecurityException("잘못된 파일 경로입니다: " + sanitizedFilename);
        }

        if (!file.getContentType().equals("mp3/mpeg")) {
            throw new FileSaveFailedException("파일 형식이 맞지 않습니다.");
        }

        try {
            Files.copy(file.getInputStream(), filepath);

            FileEntity fileEntity = FileEntity.builder()
                    .fileName(sanitizedFilename)
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

    public FileEntity findFileById(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId).orElseThrow(
                () -> new EntityNotFoundException("해당 파일을 찾을 수 없습니다."));

        log.info("getASMRFile: {}", fileEntity.getFileName());
        return fileEntity;
    }

    private String[] separateSafeFileNameAndExtension(String originalFileName) {
        int indexOfExtension = originalFileName.lastIndexOf('.');

        String fileName = originalFileName.substring(0, indexOfExtension);
        fileName = fileName.replaceAll("[\\\\/]+", "");
        fileName = fileName.replaceAll("\\.\\.+", ".");
        fileName = fileName.replaceAll("[^\\w.-]", "_");

        String extension = originalFileName.substring(indexOfExtension + 1);
        extension = extension.replaceAll("[^a-zA-Z0-9]", ""); // 확장자 정제

        return new String[]{fileName, extension};
    }
}