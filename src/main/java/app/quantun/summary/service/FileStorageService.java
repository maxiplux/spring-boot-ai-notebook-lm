package app.quantun.summary.service;

import app.quantun.summary.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  String storePdfFile(MultipartFile file) throws FileStorageException;
}
