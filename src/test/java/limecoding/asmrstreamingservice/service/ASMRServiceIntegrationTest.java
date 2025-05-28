package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.dto.asmr.ASMRSaveRequestDTO;
import limecoding.asmrstreamingservice.dto.asmr.AsmrDTO;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.exception.custom.business.PriceException;
import limecoding.support.testFIxture.AsmrServiceTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
@SpringBootTest
class ASMRServiceIntegrationTest {
    @Autowired
    private ASMRService asmrService;

    @Test
    @DisplayName("ASMR 파일을 상품으로 등록한다.")
    void register_asmr_file_as_merchandise() {

        // Given
        // 테스트용 파일
        MockMultipartFile mockMultipartFile = AsmrServiceTestFixture.createMockMultipartFile("test");

        ASMRSaveRequestDTO asmrSaveRequestDTO = new ASMRSaveRequestDTO();
        asmrSaveRequestDTO.setPrice(1000);
        asmrSaveRequestDTO.setFile(mockMultipartFile);

        // When
        AsmrDTO asmrDTO = assertDoesNotThrow(() -> asmrService.saveASMR(asmrSaveRequestDTO));

        // Then
        assertThat(asmrDTO.getPrice()).isEqualTo(asmrSaveRequestDTO.getPrice());
        assertThat(asmrDTO.getFileEntityDTO().getFileName()).contains("test");
    }

    @Test
    @DisplayName("ASMR 파일을 상품으로 등록할 때 가격이 음수가 되면 예외를 반환한다.")
    void throw_exception_when_try_to_register_price_negative_number() {

        // Given
        // 테스트용 파일
        MockMultipartFile mockMultipartFile = AsmrServiceTestFixture.createMockMultipartFile("test");

        ASMRSaveRequestDTO asmrSaveRequestDTO = new ASMRSaveRequestDTO();
        asmrSaveRequestDTO.setPrice(-1);
        asmrSaveRequestDTO.setFile(mockMultipartFile);

        // When & Then
        assertThatThrownBy(() -> asmrService.saveASMR(asmrSaveRequestDTO))
                .isInstanceOf(PriceException.class);
    }

    @Test
    @DisplayName("ASMR 엔티티를 아이디로 조회시 해당 엔티티를 반환한다.")
    void find_asmr_entity_by_id() {

        // Given
        ASMRSaveRequestDTO test = AsmrServiceTestFixture.createASMRSaveRequestDTO("test", 1000);
        AsmrDTO asmrDTO = asmrService.saveASMR(test);

        // When
        ASMR asmrEntity = asmrService.findASMREntityById(asmrDTO.getId());

        // Then
        assertThat(asmrEntity.getFileEntity().getFileName()).isEqualTo(asmrDTO.getFileEntityDTO().getFileName());
        assertThat(asmrEntity.getPrice()).isEqualTo(asmrDTO.getPrice());
    }

    @Test
    @DisplayName("ASMR 엔티티를 아이디로 조회시 없은 아이디를 조회하면 예외를 반환한다..")
    void throw_exception_if_id_in_not_exists_when_call_find_asmr_entity_by_id() {
        // When & Then
        assertThatThrownBy(() -> asmrService.findASMREntityById(1L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("ASMR 파일 조회시 asmrDTO를 반환한다.")
    void find_asmr_dto_by_id() {

        // Given
        ASMRSaveRequestDTO test = AsmrServiceTestFixture.createASMRSaveRequestDTO("test", 1000);
        AsmrDTO asmrDTO = asmrService.saveASMR(test);

        // When
        AsmrDTO asmrById = asmrService.findASMRById(asmrDTO.getId());

        // Then
        assertThat(asmrDTO)
                .usingRecursiveComparison()
                .isEqualTo(asmrById);
    }

    @Test
    @DisplayName("ASMR DTO를 아이디로 조회시 없은 아이디를 조회하면 예외를 반환한다..")
    void throw_exception_if_id_in_not_exists_when_call_find_asmr_dto_by_id() {
        // When & Then
        assertThatThrownBy(() -> asmrService.findASMRById(1L)).isInstanceOf(EntityNotFoundException.class);
    }
}