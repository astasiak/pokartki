package io.github.astasiak.pokartki.dao;

public class FlashcardSet {
    private Long id;
    private String name;
    private double basePositionEnglish;
    private double basePositionChinese;
    private double basePositionPinyin;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBasePositionEnglish() {
        return basePositionEnglish;
    }

    public void setBasePositionEnglish(double basePositionEnglish) {
        this.basePositionEnglish = basePositionEnglish;
    }

    public double getBasePositionChinese() {
        return basePositionChinese;
    }

    public void setBasePositionChinese(double basePositionChinese) {
        this.basePositionChinese = basePositionChinese;
    }

    public double getBasePositionPinyin() {
        return basePositionPinyin;
    }

    public void setBasePositionPinyin(double basePositionPinyin) {
        this.basePositionPinyin = basePositionPinyin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
