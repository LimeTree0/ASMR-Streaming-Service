package limecoding.asmrstreamingservice.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FFmpegConverter {
    
    public static void convertMp4ToHLS(String inputPath, String outputDir) throws IOException, InterruptedException, RuntimeException {

        File dir = new File(outputDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<String> command = List.of(
                "ffmpeg",
                "-i", inputPath,
                "-c", "copy",
                "-start_number", "0",
                "-hls_time", "10",
                "-hls_list_size", "0",
                "-f", "hls",
                outputDir + "/output.m3u8"
        );

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.inheritIO(); // 콘솔 로그 출력
        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg 변환 실패: 종료 코드 = " + exitCode);
        }
    }
}
