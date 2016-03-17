package io.github.astasiak.pokartki.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.List;

import io.github.astasiak.pokartki.dao.Flashcard;
import io.github.astasiak.pokartki.dao.FlashcardSet;
import io.github.astasiak.pokartki.dao.FlashcardsDao;
import io.github.astasiak.pokartki.dao.FlashcardSetsDao;

public class RandomQuestionStore implements QuestionSource {
    private FlashcardsDao flashcardsDao;
    private FlashcardSetsDao flashcardSetsDao;
    private List<QuestionDirection> directions = null;

    private List<Flashcard> flashcards = new ArrayList<>();

    private Random random = new Random();
    private Question currentQuestion;
    private Long currentFlashcardId;

    public RandomQuestionStore(FlashcardsDao flashcardsDao, FlashcardSetsDao flashcardSetsDao) {
        this.flashcardsDao = flashcardsDao;
        this.flashcardSetsDao = flashcardSetsDao;
    }

    public void init() {
        if(this.directions==null) {
            throw new IllegalStateException("Directions or sets not configured");
        }
        flashcards.clear();
        List<FlashcardSet> sets = flashcardSetsDao.list();
        for(FlashcardSet set : sets) {
            if(set.isActive()) {
                List<Flashcard> setFlashcards = flashcardsDao.listBySet(set.getId());
                flashcards.addAll(setFlashcards);
            }
        }
        pickNextQuestion();
    }

    private void pickNextQuestion() {
        int i = random.nextInt(directions.size());
        QuestionDirection direction = directions.get(i);

        int j = random.nextInt(flashcards.size());
        Flashcard flashcard = flashcards.get(j);
        while(flashcards.size()>1 && flashcard.getId().equals(currentFlashcardId)) {
            j = random.nextInt(flashcards.size());
            flashcard = flashcards.get(j);
        }

        currentFlashcardId = flashcard.getId();
        currentQuestion = new Question(flashcard.getEnglish().getWord(), flashcard.getChinese().getWord(),
                flashcard.getPinyin().getWord(), direction);
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setAnswer(Answer answer) {
        pickNextQuestion();
    }

    public void configureDirections(Set<QuestionDirection> directions) {
        this.directions = new ArrayList<>(directions);
        init();
    }
}
