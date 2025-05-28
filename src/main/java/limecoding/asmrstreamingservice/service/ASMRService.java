package limecoding.asmrstreamingservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.dto.asmr.ASMRSaveRequestDTO;
import limecoding.asmrstreamingservice.dto.asmr.AsmrDTO;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.entity.FileEntity;
import limecoding.asmrstreamingservice.exception.custom.business.PriceException;
import limecoding.asmrstreamingservice.repository.asmr.ASMRRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class ASMRService {

    private final FileService fileService;
    private final ASMRRepository asmrRepository;

    /**
     * save asmr file and asmr info in the database
     *
     * @param requestDTO {@link ASMRSaveRequestDTO} for requesting save the asmr file
     * @return ASMR {@link ASMR} entity of saving asmr file
     */
    public AsmrDTO saveASMR(ASMRSaveRequestDTO requestDTO) {

        asmrSaveRequestDtoCheck(requestDTO);

        // trying to save file
        FileEntity fileEntity = fileService.uploadFile(requestDTO.getFile());

        ASMR asmr = ASMR.builder()
                .price(requestDTO.getPrice())
                .fileEntity(fileEntity)
                .build();

        ASMR save = asmrRepository.save(asmr);

        return AsmrDTO.from(save);
    }

    /**
     * find asmr entity by id. return ${@link ASMR} entity if success, or not, throw {@link EntityNotFoundException}
     *
     * @param id asmr entity id
     * @return entity of ASMR
     * @throws EntityNotFoundException throw if failed to find ASMR entity
     */
    public ASMR findASMREntityById(Long id) {
        return asmrRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("해당하는 아이디의 ASMR 파일을 찾을 수 없습니다. id=" + id)
        );
    }

    public AsmrDTO findASMRById(Long id) {
        return AsmrDTO.from(findASMREntityById(id));
    }

    public void asmrSaveRequestDtoCheck(ASMRSaveRequestDTO requestDTO) {
        if (requestDTO.getPrice() < 0) {
            throw new PriceException("가격은 0보다 작을 수 없습니다.");
        }
    }
}
