package app.quantun.summary.exception;

import java.io.IOException;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String s, IOException e) {
        super(s, e);
    }
}
