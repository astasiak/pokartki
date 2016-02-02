package io.github.astasiak.pokartki.dao;

public class Flashcard {
    private Long id;
    private Long setId;
    private String english;
    private String chinese;
    private String pinyin;
    private double positionEnglish;
    private double positionChinese;
    private double positionPinyin;
    private double intervalEnglish;
    private double intervalChinese;
    private double intervalPinyin;

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

    public double getPositionEnglish() {
        return positionEnglish;
    }

    public void setPositionEnglish(double positionEnglish) {
        this.positionEnglish = positionEnglish;
    }

    public double getPositionChinese() {
        return positionChinese;
    }

    public void setPositionChinese(double positionChinese) {
        this.positionChinese = positionChinese;
    }

    public double getPositionPinyin() {
        return positionPinyin;
    }

    public void setPositionPinyin(double positionPinyin) {
        this.positionPinyin = positionPinyin;
    }

    public double getIntervalEnglish() {
        return intervalEnglish;
    }

    public void setIntervalEnglish(double intervalEnglish) {
        this.intervalEnglish = intervalEnglish;
    }

    public double getIntervalChinese() {
        return intervalChinese;
    }

    public void setIntervalChinese(double intervalChinese) {
        this.intervalChinese = intervalChinese;
    }

    public double getIntervalPinyin() {
        return intervalPinyin;
    }

    public void setIntervalPinyin(double intervalPinyin) {
        this.intervalPinyin = intervalPinyin;
    }
}
