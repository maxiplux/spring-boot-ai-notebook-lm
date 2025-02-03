package app.quantun.summary.services;


import app.quantun.summary.model.request.Answer;

public interface PdfServices {
    String getResponse(String message);

    Answer getSimpleAnswerFromRandomQuestionString(String message);

    Answer getCapital(String stateOrCountry);

    Answer getCapitalWithInfo(String stateOrCountry);

    //Answer getAnswerFromDatabaseMovies(Question question);

    //Answer getAdviceToBuyATruck(Question question);
}
