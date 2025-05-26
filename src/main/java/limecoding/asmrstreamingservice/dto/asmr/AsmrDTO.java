package limecoding.asmrstreamingservice.dto.asmr;

import limecoding.asmrstreamingservice.dto.fileEntity.FileEntityDTO;
import limecoding.asmrstreamingservice.entity.ASMR;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class AsmrDTO {
    private Long id;
    private Integer price;
    private FileEntityDTO fileEntityDTO;

    public static AsmrDTO from(ASMR asmr) {

        AsmrDTO asmrDTO = new AsmrDTO();

        asmrDTO.setId(asmr.getId());
        asmrDTO.setPrice(asmr.getPrice());
        asmrDTO.setFileEntityDTO(FileEntityDTO.from(asmr.getFileEntity()));

        return asmrDTO;
    }
}
