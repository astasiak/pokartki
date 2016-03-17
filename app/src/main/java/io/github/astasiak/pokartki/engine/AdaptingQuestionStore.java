package io.github.astasiak.pokartki.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import io.github.astasiak.pokartki.dao.Flashcard;
import io.github.astasiak.pokartki.dao.FlashcardSet;
import io.github.astasiak.pokartki.dao.FlashcardSetsDao;
import io.github.astasiak.pokartki.dao.FlashcardsDao;

public class AdaptingQuestionStore implements QuestionSource {
    private FlashcardsDao flashcardsDao;
    private FlashcardSetsDao flashcardSetsDao;
    private Set<QuestionDirection> directions = null;

    private PriorityQueue<QuestionEntry> queue = new PriorityQueue<>();

    private List<Flashcard> allFlashcards = new LinkedList<>();
    private Map<Long, FlashcardSet> sets = new HashMap<>();
    private Map<SetWithDirection, Double> beginnings = new HashMap<>();
    private double previousRelative;


    public AdaptingQuestionStore(FlashcardsDao flashcardsDao, FlashcardSetsDao flashcardSetsDao) {
        this.flashcardsDao = flashcardsDao;
        this.flashcardSetsDao = flashcardSetsDao;
    }

    public void init() {
        if(this.directions==null) {
            throw new IllegalStateException("Directions or sets not configured");
        }
        queue.clear();
        List<FlashcardSet> allSets = flashcardSetsDao.list();
        Set<Long> activeSetIds = new HashSet<>();
        for(FlashcardSet set : allSets) {
            sets.put(set.getId(), set);
            if(set.isActive()) {
                activeSetIds.add(set.getId());
                for(QuestionDirection direction : QuestionDirection.values()) {
                    beginnings.put(new SetWithDirection(set.getId(), direction), getBase(set, direction));
                }
            }
        }
        allFlashcards = flashcardsDao.list();
        for(Flashcard card : allFlashcards) {
            if(activeSetIds.contains(card.getSetId())) {
                for (QuestionDirection direction : directions) {
                    Flashcard.Parameters parameters = getParameters(card, direction);
                    Double setBeginning = beginnings.get(new SetWithDirection(card.getSetId(), direction));
                    double relativePosition = parameters.getPosition() - setBeginning;
                    queue.add(new QuestionEntry(card, direction, relativePosition));
                }
            }
        }
        previousRelative = queue.peek().relativePosition;
    }

    private double getBase(FlashcardSet set, QuestionDirection direction) {
        switch(direction) {
            case FROM_CHINESE:
                return set.getBasePositionChinese();
            case FROM_ENGLISH:
                return set.getBasePositionEnglish();
            default:
            case FROM_PINYIN:
                return set.getBasePositionPinyin();
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

    private Flashcard.Parameters getParameters(Flashcard flashcard, QuestionDirection direction) {
        switch(direction) {
            case FROM_CHINESE:
                return flashcard.getChinese();
            case FROM_ENGLISH:
                return flashcard.getEnglish();
            default:
            case FROM_PINYIN:
                return flashcard.getPinyin();
        }
    }

    public void setAnswer(Answer answer) {
        QuestionEntry entry = queue.poll();
        if(entry==null) {
            return;
        }
        Flashcard.Parameters parameters = getParameters(entry.flashcard, entry.direction);
        double interval = parameters.getInterval();
        if(Answer.ANSWER1==answer) interval = interval * 0.8;
        else if(Answer.ANSWER3==answer) interval = interval * 1.6;
        parameters.setInterval(interval);
        parameters.setPosition(parameters.getPosition() + interval);
        flashcardsDao.save(entry.flashcard);
        Double setBeginning = beginnings.get(new SetWithDirection(entry.flashcard.getSetId(), entry.direction));

        Double positionStep = entry.relativePosition - previousRelative;
        previousRelative = entry.relativePosition;
        for(FlashcardSet set : sets.values()) {
            if(set.isActive()) {
                increaseSetPosition(set, entry.direction, positionStep);
                flashcardSetsDao.save(set);
            }
        }

        entry.relativePosition = parameters.getPosition() - setBeginning;
        queue.add(entry);
    }

    private void increaseSetPosition(FlashcardSet set, QuestionDirection direction, Double positionStep) {
        if(direction==QuestionDirection.FROM_CHINESE) {
            double position = set.getBasePositionChinese();
            set.setBasePositionChinese(position+positionStep);
        } else if(direction==QuestionDirection.FROM_ENGLISH) {
            double position = set.getBasePositionEnglish();
            set.setBasePositionEnglish(position + positionStep);
        } else if(direction==QuestionDirection.FROM_PINYIN) {
            double position = set.getBasePositionPinyin();
            set.setBasePositionPinyin(position+positionStep);
        }
    }

    public void configureDirections(Set<QuestionDirection> directions) {
        this.directions = new HashSet<>(directions);
        init();
    }
}

class QuestionEntry implements Comparable<QuestionEntry> {
    public final Flashcard flashcard;
    public final QuestionDirection direction;
    public double relativePosition;

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
        return new Question(this.flashcard.getEnglish().getWord(), this.flashcard.getChinese().getWord(),
                this.flashcard.getPinyin().getWord(), this.direction);
    }
}

class SetWithDirection {
    public SetWithDirection(Long setId, QuestionDirection direction) {
        this.setId = setId;
        this.direction = direction;
    }
    public Long setId;
    public QuestionDirection direction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetWithDirection that = (SetWithDirection) o;

        if (!setId.equals(that.setId)) return false;
        return direction == that.direction;

    }
    @Override
    public int hashCode() {
        int result = setId.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }
}
