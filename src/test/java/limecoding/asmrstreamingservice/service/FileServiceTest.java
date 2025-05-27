package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import limecoding.asmrstreamingservice.entity.FileEntity;
import limecoding.asmrstreamingservice.exception.custom.FileSaveFailedException;
import limecoding.asmrstreamingservice.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @TempDir
    Path tempDIr;

    @BeforeEach
    void setUp() throws IOException {
        String uploadDir = tempDIr.toString();

        fileService = new FileService(
                fileRepository,
                uploadDir
        );
    }

    @Test
    @DisplayName("파일 아이디로 파일 조회시 해당 파일을 ASMRFile 객체 형태로 반환한다.")
    void findASMRFileById_shouldReturnASMRFile_WhenASMRFileExist() {
        // Given: mock ASMRFile object and configure repository to return it when ID is 1
        FileEntity fileEntity = FileEntity.builder()
                .id(1L)
                .fileName("test.mp3")
                .filePath("/upload/test.mp3")
                .build();

        when(fileRepository.findById(1L)).thenReturn(Optional.of(fileEntity));

        // When: call findASMRFileById with the ID
        FileEntity fileById = fileService.findFileById(1L);

        // Then: verify that the returned  ASMRFile is correct
        assertNotNull(fileById);
        assertThat(fileById.getId()).isEqualTo(1L);
        assertThat(fileById.getFileName()).isEqualTo("test.mp3");
        assertThat(fileById.getFilePath()).isEqualTo("/upload/test.mp3");
    }

    @Test
    @DisplayName("없는 파일 아이디로 파일 조회시 EntityNotFoundException을 반환한다.")
    void findASMRFileById_shouldThrowEntityNotFoundException_whenFileDoesNotExist() {
        // Given: mock the repository to return Optional.empty() (i.e., the file does not exist)
        when(fileRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then: calling findASMRFileById shold throw an EntityNotFoundException
        // The Exception message must be "해당 파일을 찾을 수 없습니다."
        assertThatThrownBy(() -> fileService.findFileById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 파일을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("업로드 성공 시 파일 ID를 반환한다.")
    void uploadASMRFile_shouldReturnFileId_whenUploadSucceeds() {
        // Given: create a mock File and configure the repository to return it when saving any ASMRFile
        MockMultipartFile file =
                new MockMultipartFile("file", "test.mp3",
                        "audio/mpeg", "sample content".getBytes());

        FileEntity mockFile = FileEntity.builder()
                .id(1L)
                .fileName("test.mp3")
                .filePath("/uploads/test.mp3")
                .build();

        when(fileRepository.save(any(FileEntity.class))).thenReturn(mockFile);

        // When: call uploadASMRFile and get the returned file ID
        FileEntity found = assertDoesNotThrow(() -> fileService.uploadFile(file));

        // Then: verify that the returned file ID is not null and matches the expected value
        assertNotNull(found);
        assertEquals(1L, found.getId().longValue());
    }

    @Test
    @DisplayName("MultipartFile이 NULL 인경우 예외를 던진다.")
    void uploadASMRFile_shouldThrowException_whenMultipartFileIsNull() {
        // Given: assign null to the file
        MockMultipartFile file = null;

        // When & Then: verify that uploadASMRFile throws NullPointerException when given a null file
        assertThatThrownBy(() -> fileService.uploadFile(file))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("MultipartFile은 null일 될 수 없습니다.");
    }

    @Test
    @DisplayName("파일 복사 중 IOException이 발생하면 예외를 던진다.")
    void uploadASMRFile_shouldThrowIOException_whenFileUploadFails() throws IOException {
        // Given: mock a MultipartFile that throws IOException when getInputStream() is called
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.mp3");
        when(mockFile.getInputStream()).thenThrow(new IOException("Stream failure"));

        // When & Then: verify that uploadASMRFile throws IOException
        // when failing to read file input stream
        assertThatThrownBy(() -> fileService.uploadFile(mockFile))
                .isInstanceOf(FileSaveFailedException.class)
                .hasMessage("파일 저장을 실패했습니다.");
    }
}