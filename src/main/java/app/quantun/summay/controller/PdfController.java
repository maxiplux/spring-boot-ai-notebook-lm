package app.quantun.summay.controller;


import app.quantun.summay.model.request.Answer;
import app.quantun.summay.model.request.Question;
import app.quantun.summay.services.FileStorageService;
import app.quantun.summay.services.PdfServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "Endpoints for PDF file uploads")

public class PdfController {


    private final PdfServices pdfServices;

    private final FileStorageService fileStorageService;

    @PostMapping("/question")
    public Answer getResponse(String message) {
        return this.pdfServices.getSimpleAnswerFromRandomQuestionString(message);
    }


    @PostMapping("/give-a-capital")
    public Answer getCapitalOrState(String countryOrState) {
        return this.pdfServices.getCapitalWithInfo(countryOrState);
    }



    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload PDF file",
            description = "Upload a PDF file to the server",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "File uploaded successfully",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid file format or empty file",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName= this.fileStorageService.storePdfFile(file);
        return ResponseEntity.ok("File uploaded successfully: " + fileName);
    }
}
