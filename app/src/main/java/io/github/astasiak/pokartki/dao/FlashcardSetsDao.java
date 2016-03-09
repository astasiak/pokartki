package io.github.astasiak.pokartki.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class FlashcardSetsDao extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "sets";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "id INTEGER, " +
                    "name TEXT, " +
                    "title TEXT, " +
                    "base_position_english REAL, " +
                    "base_position_chinese REAL, " +
                    "base_position_pinyin REAL, " +
                    "active INTEGER);";


    public FlashcardSetsDao(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public List<FlashcardSet> list() {
        Cursor cursor = getReadableDatabase().rawQuery("select "+
                "id, name, title, base_position_english, base_position_chinese, base_position_pinyin, active"+
                " from "+TABLE_NAME, new String[]{});

        List<FlashcardSet> result = new LinkedList<>();
        while(cursor.moveToNext()) {
            result.add(cursor2set(cursor));
        }
        return result;
    }

    public void insert(FlashcardSet set) {
        ContentValues values = new ContentValues();
        values.put("id", set.getId());
        values.put("name", set.getName());
        values.put("title", set.getTitle());
        values.put("base_position_english", set.getBasePositionEnglish());
        values.put("base_position_chinese", set.getBasePositionChinese());
        values.put("base_position_pinyin", set.getBasePositionPinyin());
        values.put("active", set.isActive());
        getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public void save(FlashcardSet set) {
        ContentValues values = new ContentValues();
        values.put("base_position_english", set.getBasePositionEnglish());
        values.put("base_position_chinese", set.getBasePositionChinese());
        values.put("base_position_pinyin", set.getBasePositionPinyin());
        values.put("active", set.isActive());
        getWritableDatabase().update(TABLE_NAME, values, "id=" + set.getId(), new String[]{});
    }

    public void setActive(Long id, boolean isActive) {
        ContentValues values = new ContentValues();
        values.put("active", isActive);
        getWritableDatabase().update(TABLE_NAME, values, "id=" + id, new String[]{});
    }

    private FlashcardSet cursor2set(Cursor cursor) {
        Long id = cursor.getLong(0);
        String name = cursor.getString(1);
        String title = cursor.getString(2);
        Double basePositionEnglish = cursor.getDouble(3);
        Double basePositionChinese = cursor.getDouble(4);
        Double basePositionPinyin = cursor.getDouble(5);
        Boolean active = cursor.getInt(6)>0;
        FlashcardSet set = new FlashcardSet();
        set.setId(id);
        set.setName(name);
        set.setTitle(title);
        set.setBasePositionEnglish(basePositionEnglish);
        set.setBasePositionChinese(basePositionChinese);
        set.setBasePositionPinyin(basePositionPinyin);
        set.setActive(active);
        return set;
    }

    public void clear() {
        getWritableDatabase().delete(TABLE_NAME, null, null);
    }
}
