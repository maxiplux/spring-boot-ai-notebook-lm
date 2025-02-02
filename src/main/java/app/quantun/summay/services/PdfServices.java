package app.quantun.summay.services;


import app.quantun.summay.model.request.Answer;
import app.quantun.summay.model.request.Question;

public interface PdfServices {
    String getResponse(String message);

    Answer getSimpleAnswerFromRandomQuestionString(String message);

    Answer getCapital(String stateOrCountry);

    Answer getCapitalWithInfo(String stateOrCountry);

    //Answer getAnswerFromDatabaseMovies(Question question);

    //Answer getAdviceToBuyATruck(Question question);
}
