package io.github.astasiak.pokartki.engine.mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.github.astasiak.pokartki.engine.Answer;
import io.github.astasiak.pokartki.engine.Question;
import io.github.astasiak.pokartki.engine.QuestionDirection;
import io.github.astasiak.pokartki.engine.QuestionSource;

public class MockQuestionSource implements QuestionSource {

    private static List<Question> QUESTIONS = Arrays.asList(
            new Question("a student", "\u5b66\u751f", "xu\u00e9sheng"),
            new Question("a language", "\u8bed\u8a00", "y\u016dy\u00e1n"),
            new Question("I'm sorry", "\u5bf9\u4e0d\u8d77", "du\u00ec buq\u012d"),
            new Question("Good bye", "\u518d\u89c1", "z\u00e0iji\u00e0n"),
            new Question("this", "\u8FD9", "zh\u00e8")
    );

    private Question currentQuestion = null;
    private int currentId = -1;
    private Set<QuestionDirection> directions = null;

    @Override
    public Question getCurrentQuestion() {
        if(currentQuestion==null) {
            chooseNewQuestion();
        }
        return currentQuestion;
    }

    @Override
    public void setAnswer(Answer answer) {
        chooseNewQuestion();
    }

    @Override
    public void configureDirections(Set<QuestionDirection> directions) {
        this.directions = new HashSet<>(directions);
    }

    private void chooseNewQuestion() {
        int previousId = currentId;
        while(previousId==currentId) {
            currentId = new Random().nextInt(5);
        }
        currentQuestion = QUESTIONS.get(currentId);
        QuestionDirection dir = QuestionDirection.values()[new Random().nextInt(3)];
        while(!directions.isEmpty() && !directions.contains(dir)) {
            dir = QuestionDirection.values()[new Random().nextInt(3)];
        }
        currentQuestion.setDirection(dir);
    }
}
