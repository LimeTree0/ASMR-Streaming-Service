package limecoding.support.testFIxture;

import limecoding.asmrstreamingservice.dto.asmr.ASMRSaveRequestDTO;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.entity.FileEntity;
import limecoding.asmrstreamingservice.entity.User;
import limecoding.asmrstreamingservice.entity.post.Post;
import limecoding.asmrstreamingservice.entity.post.PostType;
import org.springframework.mock.web.MockMultipartFile;

public class AsmrServiceTestFixture {

    public static MockMultipartFile createMockMultipartFile(String fileName) {
        return new MockMultipartFile(fileName, fileName + ".mp3",
                "audio/mpeng", fileName.getBytes());
    }

    public static User createUser(Long id) {
        return User.builder()
                .id(id)
                .userId("testUser")
                .password("secure")
                .build();
    }

    public static Post createPost(Long id, User user) {
        return Post.builder()
                .id(id)
                .postType(PostType.ASMR)
                .user(user)
                .build();
    }

    public static FileEntity createFileEntity(Long id, MockMultipartFile mockMultipartFile) {
        return FileEntity.builder()
                .id(id)
                .fileName(mockMultipartFile.getOriginalFilename())
                .filePath("test/path/" + mockMultipartFile.getOriginalFilename())
                .build();
    }

    public static ASMR createASMR(Long id, Post post, FileEntity fileEntity) {
        return ASMR.builder()
                .id(id)
                .price(1000)
                .post(post)
                .fileEntity(fileEntity)
                .build();
    }

    public static ASMR createASMR(Long id, MockMultipartFile mockMultipartFile) {
        User defaultUser = createUser(1L);
        Post defaultPost = createPost(1L, defaultUser);
        FileEntity defaultFile = createFileEntity(1L, mockMultipartFile);

        return createASMR(id, defaultPost, defaultFile);
    }

    public static ASMRSaveRequestDTO createASMRSaveRequestDTO(String fileName, Integer price) {

        MockMultipartFile mockMultipartFile = createMockMultipartFile(fileName);
        ASMRSaveRequestDTO asmrSaveRequestDTO = new ASMRSaveRequestDTO();

        asmrSaveRequestDTO.setFile(mockMultipartFile);
        asmrSaveRequestDTO.setPrice(price);

        return asmrSaveRequestDTO;
    }
}
