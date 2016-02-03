package io.github.astasiak.pokartki.engine;

import java.util.Set;

public interface QuestionSource {
    Question getCurrentQuestion();
    void setAnswer(Answer answer);
    void configureDirections(Set<QuestionDirection> directions);
}
