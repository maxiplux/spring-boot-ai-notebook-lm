package app.quantun.summay.services;

import app.quantun.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storePdfFile(MultipartFile file) throws FileStorageException;
}
