package io.github.astasiak.pokartki.engine;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.List;

import io.github.astasiak.pokartki.dao.Flashcard;
import io.github.astasiak.pokartki.dao.FlashcardsDao;
import io.github.astasiak.pokartki.dao.FlashcardSetsDao;

public class DaoQuestionStore implements QuestionSource {
    private FlashcardsDao flashcardsDao;
    private FlashcardSetsDao flashcardSetsDao;
    private Set<QuestionDirection> directions = null;
    private Set<Long> setIds = null;

    private PriorityQueue<QuestionEntry> queue = new PriorityQueue<>();
    private double globalBasePosition = 0.0;

    private List<Flashcard> allFlashcards = new LinkedList<>();

    public DaoQuestionStore(FlashcardsDao flashcardsDao, FlashcardSetsDao flashcardSetsDao) {
        this.flashcardsDao = flashcardsDao;
        this.flashcardSetsDao = flashcardSetsDao;
    }

    public void init() {
        if(this.directions==null /*|| this.setIds==null*/) {
            throw new IllegalStateException("Directions or sets not configured");
        }
        queue.clear();
        allFlashcards = flashcardsDao.list();
        for(Flashcard card : allFlashcards) {
            for(QuestionDirection direction : QuestionDirection.values()) {
                if(directions.contains(direction)) {
                    queue.add(new QuestionEntry(card, direction, 0.0));
                }
            }
        }
    }

    public Question getCurrentQuestion() {
        QuestionEntry entry = queue.peek();
        if(entry==null) {
            return null;
        } else {
            return entry.toQuestion();
        }
    }

    public void setAnswer(Answer answer) {
        QuestionEntry entry = queue.poll();
        //double positionIncrease = entry.relativePosition - globalBasePosition;
        // sets.increase positions by position increase
        //globalBasePosition = entry.relativePosition;
        //entry.flashcard.saveNewParams
        //queue.offer(new entry)
    }

    public void configureDirections(Set<QuestionDirection> directions) {
        this.directions = new HashSet<>(directions);
        init();
    }

    public void configureSets(Set<Long> setIds) {
        this.setIds = new HashSet<>(setIds);
        // reload queue
    }
}

class QuestionEntry implements Comparable<QuestionEntry> {
    public final Flashcard flashcard;
    public final QuestionDirection direction;
    public final double relativePosition;

    public QuestionEntry(Flashcard flashcard, QuestionDirection direction, double relativePosition) {
        this.flashcard = flashcard;
        this.direction = direction;
        this.relativePosition = relativePosition;
    }

    @Override
    public int compareTo(QuestionEntry another) {
        return Double.compare(this.relativePosition, another.relativePosition);
    }

    public Question toQuestion() {
        return new Question(this.flashcard.getEnglish(), this.flashcard.getChinese(),
                this.flashcard.getPinyin(), this.direction);
    }
}