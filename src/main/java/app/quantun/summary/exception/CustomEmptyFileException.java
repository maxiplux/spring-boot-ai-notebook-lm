package app.quantun.summary.exception;

public class CustomEmptyFileException extends RuntimeException {
    public CustomEmptyFileException(String fileCannotBeEmpty) {
        super(fileCannotBeEmpty);
    }

}
