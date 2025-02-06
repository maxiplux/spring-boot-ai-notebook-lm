package app.quantun.summary.controller;

import app.quantun.summary.service.PdfServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PdfControllerTest {

    @Mock
    private PdfServices pdfServices;

    @InjectMocks
    private PdfController pdfController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleFileUpload() {
        MultipartFile file = new MockMultipartFile("file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "test content".getBytes());
        String expectedFileName = "test.pdf";
        when(pdfServices.storePdfFile(file)).thenReturn(expectedFileName);

        ResponseEntity<String> response = pdfController.handleFileUpload(file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded successfully: " + expectedFileName, response.getBody());
    }
}
