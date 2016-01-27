package io.github.astasiak.pokartki.engine;

public class Question {
    private String english;
    private String chinese;
    private String pinyin;
    private QuestionDirection direction;

    public Question(String english, String chinese, String pinyin) {
        this.english = english;
        this.chinese = chinese;
        this.pinyin = pinyin;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public QuestionDirection getDirection() {
        return direction;
    }

    public void setDirection(QuestionDirection direction) {
        this.direction = direction;
    }
}
