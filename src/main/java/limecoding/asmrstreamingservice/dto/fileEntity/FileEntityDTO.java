package limecoding.asmrstreamingservice.dto.fileEntity;

import limecoding.asmrstreamingservice.entity.FileEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class FileEntityDTO {
    private Long id;
    private String fileName;
    private String filePath;

    public static FileEntityDTO from(FileEntity fileEntity) {
        FileEntityDTO fileEntityDTO = new FileEntityDTO();

        fileEntityDTO.setId(fileEntityDTO.getId());
        fileEntityDTO.setFileName(fileEntity.getFileName());
        fileEntityDTO.setFilePath(fileEntity.getFilePath());

        return fileEntityDTO;
    }
}
