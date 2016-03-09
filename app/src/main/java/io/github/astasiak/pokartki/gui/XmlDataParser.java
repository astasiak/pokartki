package io.github.astasiak.pokartki.gui;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import io.github.astasiak.pokartki.dao.Flashcard;
import io.github.astasiak.pokartki.dao.FlashcardSet;

public class XmlDataParser {
    public static Result parse(XmlResourceParser xml) throws IOException, XmlPullParserException {
        Result result = new Result();

        int eventType = xml.next();
        Long currentSetId = 0l;
        Long currentCardId = 0l;
        Flashcard currentFlashcard = null;
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if(eventType == XmlPullParser.START_TAG)
            {
                if("set".equals(xml.getName())) {
                    String name = xml.getAttributeValue(null, "name");
                    String title = xml.getAttributeValue(null, "title");
                    if(name!=null && title!=null) {
                        currentSetId++;
                        FlashcardSet set = new FlashcardSet();
                        set.setActive(true);
                        set.setName(name);
                        set.setTitle(title);
                        set.setId(currentSetId);
                        result.getSets().add(set);
                    }
                }
                if("flashcard".equals(xml.getName())) {
                    currentCardId++;
                    currentFlashcard = new Flashcard();
                    currentFlashcard.setId(currentCardId);
                    currentFlashcard.setSetId(currentSetId);
                    currentFlashcard.setIntervalChinese(1.0);
                    currentFlashcard.setIntervalEnglish(1.0);
                    currentFlashcard.setIntervalPinyin(1.0);
                    result.getCards().add(currentFlashcard);
                }
                if("english".equals(xml.getName())) {
                    eventType = xml.next();
                    if(eventType == XmlPullParser.TEXT) {
                        String english = xml.getText();
                        currentFlashcard.setEnglish(english);
                    }
                }
                if("chinese".equals(xml.getName())) {
                    eventType = xml.next();
                    if(eventType == XmlPullParser.TEXT) {
                        String chinese = xml.getText();
                        currentFlashcard.setChinese(chinese);
                    }
                }
                if("pinyin".equals(xml.getName())) {
                    eventType = xml.next();
                    if(eventType == XmlPullParser.TEXT) {
                        String pinyin = xml.getText();
                        currentFlashcard.setPinyin(pinyin);
                    }
                }
            }
            eventType = xml.next();
        }
        return result;
    }

    public static class Result {
        private List<Flashcard> cards = new LinkedList<>();

        private List<FlashcardSet> sets = new LinkedList<>();

        public List<Flashcard> getCards() {
            return cards;
        }

        public List<FlashcardSet> getSets() {
            return sets;
        }
    }
}

