package app.quantun.exception;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(String onlyPdfFilesAreAllowed) {
        super(onlyPdfFilesAreAllowed);
    }
}
