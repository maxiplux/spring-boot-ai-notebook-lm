package app.quantun.summary.controller;

import app.quantun.summary.model.contract.dto.TableIndexContent;
import app.quantun.summary.service.PdfServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "Endpoints for PDF file uploads")
public class PdfController {

    private final PdfServices pdfServices;
    @Autowired
    @Qualifier("anthropicChatClient")
    private ChatClient anthropicChatClient;
    @Autowired
    @Qualifier("openAiChatClient")
    private ChatClient openAiChatClient;
    @Autowired
    @Qualifier("perplexityChatClient")
    private ChatClient perplexityChatClient;

    /**
     * Handles the upload of a PDF file.
     *
     * @param file the PDF file to upload
     * @return a response entity with the status of the upload
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload PDF file",
            description = "Upload a PDF file to the server",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "File uploaded successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid file format or empty file",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content)
            })
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            return ResponseEntity.badRequest().body("File cannot be null");
        }
        String fileName = this.pdfServices.storePdfFile(file);
        return ResponseEntity.ok("File uploaded successfully: " + fileName);
    }

    @PostMapping("/get-table-of-content")
    public TableIndexContent getTableOfContent(String message) {
        return this.pdfServices.getBookTableOfContentPages(message);
    }

    @GetMapping("/anthropic")
    public String getAnthropicChatClient() {
        return anthropicChatClient.prompt().user("GIVE A NUMBER BETWEEN ONE TO FIVE").call().content();
    }

    @GetMapping("/openai")
    public String getOpenAiChatClient() {
        return openAiChatClient.prompt().user("GIVE A NUMBER BETWEEN ONE TO FIVE").call().content();
    }

    @GetMapping("/perplexity")
    public String getPerplexityChatClient() {
        return perplexityChatClient.prompt().advisors(new SimpleLoggerAdvisor()).user("GIVE A NUMBER BETWEEN ONE TO FIVE").call().content();
    }


}
