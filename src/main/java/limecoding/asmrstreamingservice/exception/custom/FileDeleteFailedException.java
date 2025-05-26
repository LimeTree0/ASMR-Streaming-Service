package limecoding.asmrstreamingservice.exception.custom;

public class FileDeleteFailedException extends RuntimeException {
    public FileDeleteFailedException(String message) {
        super(message);
    }
}
