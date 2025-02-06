package app.quantun.summary.service;


import app.quantun.summary.model.dto.TableIndexContent;
import app.quantun.summary.model.request.Answer;
import org.springframework.web.multipart.MultipartFile;

public interface PdfServices {
    TableIndexContent getBookTableOfContentPages(String message);

    String getResponse(String message);

    Answer getSimpleAnswerFromRandomQuestionString(String message);

    Answer getCapital(String stateOrCountry);

    Answer getCapitalWithInfo(String stateOrCountry);

    String storePdfFile(MultipartFile file);


}
