package io.github.astasiak.pokartki.dao;

public class Flashcard {
    private Long id;
    private Long setId;
    private Parameters english = new Parameters();
    private Parameters chinese = new Parameters();
    private Parameters pinyin = new Parameters();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSetId() {
        return setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
    }

    public double getAverageInterval() {
        return (chinese.getInterval()+english.getInterval()+pinyin.getInterval())/3;
    }

    public Parameters getChinese() {
        return chinese;
    }

    public Parameters getPinyin() {
        return pinyin;
    }

    public Parameters getEnglish() {
        return english;
    }

    public static class Parameters {
        private String word;
        private double position;
        private double interval;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public double getPosition() {
            return position;
        }

        public void setPosition(double position) {
            this.position = position;
        }

        public double getInterval() {
            return interval;
        }

        public void setInterval(double interval) {
            this.interval = interval;
        }
    }
}
