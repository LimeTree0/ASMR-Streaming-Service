package limecoding.asmrstreamingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import limecoding.asmrstreamingservice.util.FFmpegConverter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    private static final String BASE_PATH = "videos";

    @PostMapping(path = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadVideo(@RequestPart(name = "file") MultipartFile file) throws IOException, InterruptedException {
        String basePath = System.getProperty("user.dir");
        File savedFile = new File(basePath + "/upload/video/" + file.getOriginalFilename());
        file.transferTo(savedFile);

        String outputDir = "videos/movie1";
        FFmpegConverter.convertMp4ToHLS(savedFile.getPath(), outputDir);

        return ResponseEntity.ok("동영상이 HLS로 변환되었습니다!");
    }

    @Operation(summary = "HLS 스트리밍 파일 제공", description = "폴더명과 파일명을 받아 HLS 조각을 스트리밍합니다.")
    @GetMapping("/{folder}/{fileName}")
    public ResponseEntity<Resource> streamFile(
            @Parameter(description = "동영상 폴더 이름") @PathVariable String folder,
            @Parameter(description = "m3u8 또는 ts 파일 이름") @PathVariable String fileName
    ) {
        File file = new File(BASE_PATH + "/" + folder + "/" + fileName);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = getMediaType(fileName);
        FileSystemResource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    private MediaType getMediaType(String fileName) {
        if (fileName.endsWith(".m3u8")) return MediaType.parseMediaType("application/vnd.apple.mpegurl");
        if (fileName.endsWith(".ts")) return MediaType.parseMediaType("video/mp2t");
        return MediaType.APPLICATION_OCTET_STREAM;
    }

}
