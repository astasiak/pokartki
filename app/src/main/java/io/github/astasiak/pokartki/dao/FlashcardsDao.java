package io.github.astasiak.pokartki.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

public class FlashcardsDao extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "flashcards";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "id INTEGER, " +
                    "set_id INTEGER, " +
                    "english TEXT, " +
                    "chinese TEXT, " +
                    "pinyin TEXT, " +
                    "position_english REAL, " +
                    "position_chinese REAL, " +
                    "position_pinyin REAL, " +
                    "interval_english REAL, " +
                    "interval_chinese REAL, " +
                    "interval_pinyin REAL);";
    private static final String SELECT = "select "+
            "id, set_id, english, chinese, pinyin, position_english, position_chinese, "+
            "position_pinyin, interval_english, interval_chinese, interval_pinyin"+
            " from "+TABLE_NAME;

    public FlashcardsDao(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public List<Flashcard> list() {
        Cursor cursor = getReadableDatabase().rawQuery(SELECT, new String[]{});
        return readListFromCursor(cursor);
    }

    public List<Flashcard> listBySet(Long setId) {
        Cursor cursor = getReadableDatabase().rawQuery(SELECT+" WHERE set_id = ?", new String[]{setId.toString()});
        return readListFromCursor(cursor);
    }

    public Flashcard get(Long cardId) {
        Cursor cursor = getReadableDatabase().rawQuery(SELECT+" WHERE id = ?", new String[]{cardId.toString()});
        if(!cursor.moveToNext()) {
            return null;
        }
        return readCardFromCursor(cursor);
    }

    @NonNull
    private List<Flashcard> readListFromCursor(Cursor cursor) {
        List<Flashcard> result = new LinkedList<>();
        while(cursor.moveToNext()) {
            result.add(readCardFromCursor(cursor));
        }
        return result;
    }

    public void insert(Flashcard flashcard) {
        ContentValues values = new ContentValues();
        values.put("id", flashcard.getId());
        values.put("set_id", flashcard.getSetId());
        values.put("english", flashcard.getEnglish().getWord());
        values.put("chinese", flashcard.getChinese().getWord());
        values.put("pinyin", flashcard.getPinyin().getWord());
        values.put("position_english", flashcard.getEnglish().getPosition());
        values.put("position_chinese", flashcard.getChinese().getPosition());
        values.put("position_pinyin", flashcard.getPinyin().getPosition());
        values.put("interval_english", flashcard.getEnglish().getInterval());
        values.put("interval_chinese", flashcard.getChinese().getInterval());
        values.put("interval_pinyin", flashcard.getPinyin().getInterval());
        getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public void save(Flashcard flashcard) {
        ContentValues values = new ContentValues();
        values.put("position_english", flashcard.getEnglish().getPosition());
        values.put("position_chinese", flashcard.getChinese().getPosition());
        values.put("position_pinyin", flashcard.getPinyin().getPosition());
        values.put("interval_english", flashcard.getEnglish().getInterval());
        values.put("interval_chinese", flashcard.getChinese().getInterval());
        values.put("interval_pinyin", flashcard.getPinyin().getInterval());
        getWritableDatabase().update(TABLE_NAME, values, "id=" + flashcard.getId(), new String[]{});
    }

    private Flashcard readCardFromCursor(Cursor cursor) {
        if(cursor.isClosed()) {
            return null;
        }
        Long id = cursor.getLong(0);
        Long setId = cursor.getLong(1);
        String english = cursor.getString(2);
        String chinese = cursor.getString(3);
        String pinyin = cursor.getString(4);
        Double positionEnglish = cursor.getDouble(5);
        Double positionChinese = cursor.getDouble(6);
        Double positionPinyin = cursor.getDouble(7);
        Double intervalEnglish = cursor.getDouble(8);
        Double intervalChinese = cursor.getDouble(9);
        Double intervalPinyin = cursor.getDouble(10);
        Flashcard card = new Flashcard();
        card.setId(id);
        card.setSetId(setId);
        card.getEnglish().setWord(english);
        card.getChinese().setWord(chinese);
        card.getPinyin().setWord(pinyin);
        card.getEnglish().setPosition(positionEnglish);
        card.getChinese().setPosition(positionChinese);
        card.getPinyin().setPosition(positionPinyin);
        card.getEnglish().setInterval(intervalEnglish);
        card.getChinese().setInterval(intervalChinese);
        card.getPinyin().setInterval(intervalPinyin);
        return card;
    }

    public void clear() {
        getWritableDatabase().delete(TABLE_NAME, null, null);
    }
}
