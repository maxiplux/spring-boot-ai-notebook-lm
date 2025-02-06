package app.quantun.summary.service.impl;

import app.quantun.summary.exception.CustomEmptyFileException;
import app.quantun.summary.exception.FileStorageException;
import app.quantun.summary.exception.InvalidFileTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceImplTest {

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    @Value("${app.config.pdf.upload.path}")
    private String uploadDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStorePdfFile_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test content".getBytes());
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);
        String storedFileName = fileStorageService.storePdfFile(file);
        assertEquals("test.pdf", storedFileName);
        assertTrue(Files.exists(uploadPath.resolve("test.pdf")));
    }

    @Test
    void testStorePdfFile_EmptyFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[0]);
        assertThrows(CustomEmptyFileException.class, () -> fileStorageService.storePdfFile(file));
    }

    @Test
    void testStorePdfFile_InvalidFileType() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        assertThrows(InvalidFileTypeException.class, () -> fileStorageService.storePdfFile(file));
    }

    @Test
    void testStorePdfFile_IOException() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test content".getBytes());
        doThrow(IOException.class).when(multipartFile).transferTo(any(Path.class));
        assertThrows(FileStorageException.class, () -> fileStorageService.storePdfFile(file));
    }
}
