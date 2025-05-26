package limecoding.asmrstreamingservice.dto.asmr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Setter
@Getter
public class ASMRSaveRequestDTO {
    private Integer price;
    private MultipartFile file;
}
