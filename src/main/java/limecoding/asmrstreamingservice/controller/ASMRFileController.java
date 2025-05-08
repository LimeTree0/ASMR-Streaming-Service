package limecoding.asmrstreamingservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import limecoding.asmrstreamingservice.common.response.ApiResponse;
import limecoding.asmrstreamingservice.entity.ASMRFile;
import limecoding.asmrstreamingservice.service.ASMRFileService;
import limecoding.asmrstreamingservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/file")
public class ASMRFileController {

    private final ASMRFileService asmrFileService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<?>> uploadFile(
            @RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "파일이 비어있습니다", null));
        }

        Long fileId = asmrFileService.uploadASMRFile(file);

        Map<String, String> map = new HashMap<>();

        map.put("fileId", String.valueOf(fileId));

        return ResponseEntity.ok(ApiResponse.success(map));
    }

    @GetMapping("/{filename}")
    public ResponseEntity<ApiResponse<?>> getFile(@PathVariable String filename) {

        return ResponseEntity.ok(ApiResponse.success("/file/" + filename));
    }

    @GetMapping("/audio/stream/{fileId}")
    public void streamAudio(
            @PathVariable Long fileId,
            @RequestHeader(value = "range", required = false) String rangeHeader,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        ASMRFile asmrFile = asmrFileService.findASMRFileById(fileId);

        log.info("Streaming audio file: {}", asmrFile.getFileName());

        File audioFile = new File(asmrFile.getFilePath());
        long fileLength = audioFile.length();

        if (rangeHeader == null) {
            response.setContentType("audio/mpeg");
            response.setContentLengthLong(fileLength);
            Files.copy(audioFile.toPath(), response.getOutputStream());
            return;
        }

        String[] ranges = rangeHeader.replace("bytes=", "").split("-");
        long start = Long.parseLong(ranges[0]);
        long end = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : fileLength - 1;
        long chunkSize = end - start + 1;

        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType("audio/mpeg");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
        response.setHeader("Content-Length", String.valueOf(chunkSize));

        try (RandomAccessFile inputFile = new RandomAccessFile(audioFile, "r");
             OutputStream outputStream = response.getOutputStream()) {

            inputFile.seek(start);

            byte[] buffer = new byte[8192];
            long bytesRemaining = chunkSize;

            while (bytesRemaining > 0) {
                int bytesToRead = (int) Math.min(buffer.length, chunkSize);
                int bytesRead = inputFile.read(buffer, 0, bytesToRead);

                if (bytesRead == -1) break;

                outputStream.write(buffer, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }
        } catch (Exception e) {
            log.error("오디오 스트림 중 예외 발생 : {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }
}