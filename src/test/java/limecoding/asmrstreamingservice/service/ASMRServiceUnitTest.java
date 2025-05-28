package limecoding.asmrstreamingservice.service;

import limecoding.asmrstreamingservice.dto.asmr.ASMRSaveRequestDTO;
import limecoding.asmrstreamingservice.dto.asmr.AsmrDTO;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.repository.asmr.ASMRRepository;
import limecoding.support.testFIxture.AsmrServiceTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ASMRServiceUnitTest {

    @InjectMocks
    private ASMRService asmrService;

    @Mock
    private FileService fileService;

    @Mock
    private ASMRRepository asmrRepository;

    @Test
    @DisplayName(value = "ASMR 파일을 저장시 저장된 ASMR 객체의 DTO를 반환한다.")
    void save_asmr_file() {
        // Given
        MockMultipartFile testMockFile = AsmrServiceTestFixture.createMockMultipartFile("test");
        ASMR asmr = AsmrServiceTestFixture.createASMR(1L, testMockFile);

        ASMRSaveRequestDTO asmrSaveRequestDTO = new ASMRSaveRequestDTO();
        asmrSaveRequestDTO.setFile(testMockFile);
        asmrSaveRequestDTO.setPrice(1000);


        when(fileService.uploadFile(testMockFile)).thenReturn(asmr.getFileEntity());
        when(asmrRepository.save(any(ASMR.class))).thenReturn(asmr);

        // When: call saveASMR and get the ASMR DTO
        AsmrDTO asmrDTO = assertDoesNotThrow(() -> asmrService.saveASMR(asmrSaveRequestDTO));

        // Then: verify each field has correct data
        assertThat(asmrDTO.getPrice()).isEqualTo(asmrSaveRequestDTO.getPrice());
        assertThat(asmrDTO.getFileEntityDTO().getFileName()).isEqualTo(asmrSaveRequestDTO.getFile().getOriginalFilename());
    }
}