package app.quantun.summary.service;


import app.quantun.summary.model.dto.TableIndexContent;
import org.springframework.web.multipart.MultipartFile;

public interface PdfServices {
    TableIndexContent getBookTableOfContentPages(String message);


    String storePdfFile(MultipartFile file);


}
