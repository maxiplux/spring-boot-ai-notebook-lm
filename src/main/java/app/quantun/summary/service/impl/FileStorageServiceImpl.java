package app.quantun.summary.service.impl;

import app.quantun.summary.exception.CustomEmptyFileException;
import app.quantun.summary.exception.FileStorageException;
import app.quantun.summary.exception.InvalidFileTypeException;
import app.quantun.summary.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.config.pdf.upload.path}")
    private String uploadDir;

    /**
     * Stores the PDF file to the specified directory.
     *
     * @param file the PDF file to store
     * @return the file name
     * @throws FileStorageException if an error occurs during file storage
     */
    @Override
    public String storePdfFile(MultipartFile file) throws FileStorageException {
        try {
            log.info("Storing file: {}", file.getOriginalFilename());
            validateFile(file);
            Path uploadPath = createUploadDirectory();
            return saveFileToDisk(file, uploadPath);
        } catch (IOException e) {
            log.error("Failed to store file: {}", e.getMessage());
            throw new FileStorageException("Failed to store file: " + e.getMessage(), e);
        }
    }

    /**
     * Validates the uploaded file.
     *
     * @param file the file to validate
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            log.warn("Uploaded file is empty");
            throw new CustomEmptyFileException("File cannot be empty");
        }

        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        if (!fileName.toLowerCase().endsWith(".pdf")) {
            log.warn("Invalid file type: {}", fileName);
            throw new InvalidFileTypeException("Only PDF files are allowed");
        }
    }

    /**
     * Creates the upload directory if it does not exist.
     *
     * @return the path to the upload directory
     * @throws IOException if an error occurs during directory creation
     */
    private Path createUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(this.uploadDir);
        if (!Files.exists(uploadPath)) {
            log.info("Creating upload directory at: {}", uploadPath);
            return Files.createDirectories(uploadPath);
        }
        return uploadPath;
    }

    /**
     * Saves the file to the disk.
     *
     * @param file       the file to save
     * @param uploadPath the path to the upload directory
     * @return the file name
     * @throws IOException if an error occurs during file saving
     */
    private String saveFileToDisk(MultipartFile file, Path uploadPath) throws IOException {
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);
        log.info("Saving file to disk at: {}", filePath);
        file.transferTo(filePath);
        return fileName;
    }
}