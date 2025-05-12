package limecoding.asmrstreamingservice.service;

import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.entity.ASMRFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
@SpringBootTest
class ASMRFileServiceIntegrationTest {

    @Autowired
    private ASMRFileService asmrFileService;

    @Value("${upload.dir}")
    private Path uploadDir;

    @Test
    @DisplayName("Should upload file and retrieve it without Exception, and validate file name and path")
    void uploadASMRFile_shouldStoreFileInfoProperly() {

        // Given : a mock file to upload
        String originalFileName = "test.mp3";
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", originalFileName,
                        "audio/mpeg", "Hello, ASMR!!".getBytes());

        // When : uploading the file via service layer (should not throw exception)
        Long uploadedFileId = assertDoesNotThrow(() -> asmrFileService.uploadASMRFile(multipartFile));

        // And : retrieving the uploaded file from the service (should not throw exception)
        ASMRFile savedAsmrFile = assertDoesNotThrow(() -> asmrFileService.findASMRFileById(uploadedFileId));

        // Then : the saved file name should have UUID prefix and correct original file name
        assertThat(savedAsmrFile.getFileName())
                .endsWith(originalFileName)
                .matches("^[0-9a-fA-F\\-]{36}_.+\\.mp3$");

        // And : the saved file path should contain the file name
        assertThat(savedAsmrFile.getFilePath()).contains(savedAsmrFile.getFileName());
    }
}