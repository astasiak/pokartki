package io.github.astasiak.pokartki.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        Cursor cursor = getReadableDatabase().rawQuery("select "+
                "id, set_id, english, chinese, pinyin, position_english, position_chinese, "+
                "position_pinyin, interval_english, interval_chinese, interval_pinyin"+
                " from "+TABLE_NAME, new String[]{});

        List<Flashcard> result = new LinkedList<>();
        while(cursor.moveToNext()) {
            result.add(cursor2card(cursor));
        }
        return result;
    }

    public void insert(Flashcard flashcard) {
        ContentValues values = new ContentValues();
        values.put("id", flashcard.getId());
        values.put("set_id", flashcard.getSetId());
        values.put("english", flashcard.getEnglish());
        values.put("chinese", flashcard.getChinese());
        values.put("pinyin", flashcard.getPinyin());
        values.put("position_english", flashcard.getPositionEnglish());
        values.put("position_chinese", flashcard.getPositionChinese());
        values.put("position_pinyin", flashcard.getPositionPinyin());
        values.put("interval_english", flashcard.getIntervalEnglish());
        values.put("interval_chinese", flashcard.getIntervalChinese());
        values.put("interval_pinyin", flashcard.getIntervalPinyin());
        getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public void save(Flashcard flashcard) {
        ContentValues values = new ContentValues();
        values.put("position_english", flashcard.getPositionEnglish());
        values.put("position_chinese", flashcard.getPositionChinese());
        values.put("position_pinyin", flashcard.getPositionPinyin());
        values.put("interval_english", flashcard.getIntervalEnglish());
        values.put("interval_chinese", flashcard.getIntervalChinese());
        values.put("interval_pinyin", flashcard.getIntervalPinyin());
        getWritableDatabase().update(TABLE_NAME, values, "id=" + flashcard.getId(), new String[]{});
    }

    private Flashcard cursor2card(Cursor cursor) {
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
        card.setEnglish(english);
        card.setChinese(chinese);
        card.setPinyin(pinyin);
        card.setPositionEnglish(positionEnglish);
        card.setPositionChinese(positionChinese);
        card.setPositionPinyin(positionPinyin);
        card.setIntervalEnglish(intervalEnglish);
        card.setIntervalChinese(intervalChinese);
        card.setIntervalPinyin(intervalPinyin);
        return card;
    }

    public void clear() {
        getWritableDatabase().delete(TABLE_NAME, null, null);
    }
}
