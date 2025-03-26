package com.example.labs.db.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.labs.db.TextStyleRepository;
import com.example.labs.db.dao.TextStyleEntity;

import java.util.ArrayList;
import java.util.List;

public class TextStyleRepositoryImpl extends SQLiteOpenHelper implements TextStyleRepository {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user_data.db";
    private static final String TABLE_NAME = "text_styles";

    private static final String ID_COLUMN = "id";
    private static final String TEXT_COLUMN = "text";
    private static final String FONT_ID_COLUMN = "font_id";

    private static final  String LOG_TAG = "DB_MANIPULATIONS";
    private static final String CREATE_TEXT_STYLES_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID_COLUMN + " INTEGER PRIMARY KEY, " +
                    TEXT_COLUMN + " TEXT, " +
                    FONT_ID_COLUMN + " INTEGER" + ")";

    private static final String DROP_TEXT_STYLES_TABLE_QUERY =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public TextStyleRepositoryImpl(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEXT_STYLES_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TEXT_STYLES_TABLE_QUERY);
        onCreate(db);
    }

    @Override
    public boolean createTextStyleEntry(TextStyleEntity textStyleEntity) {
        Log.d(LOG_TAG, "Starting insert new record");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TEXT_COLUMN, textStyleEntity.getText());
        values.put(FONT_ID_COLUMN, textStyleEntity.getFontId());

        long result = db.insert(TABLE_NAME, null, values);

        db.close();

        if (result > -1) {
            Log.d(LOG_TAG, "New record - " + result + "inserted");
        } else {
            Log.e(LOG_TAG, "Failed to insert new record");
        }

        return result > -1;
    }

    @Override
    public List<TextStyleEntity> getAllTextStyles() {
        Log.d(LOG_TAG, "Starting fetching all records");
        List<TextStyleEntity> res = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{ID_COLUMN, TEXT_COLUMN, FONT_ID_COLUMN},
                null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            res.add(new TextStyleEntity(
                    cursor.getLong(cursor.getColumnIndexOrThrow(ID_COLUMN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TEXT_COLUMN)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FONT_ID_COLUMN)))
            );
        }
        cursor.close();
        db.close();
        Log.d(LOG_TAG, "Fetched " + res.size() + " records");
        return res;
    }

    @Override
    public void deleteAllTextStyles() {
        SQLiteDatabase db = this.getWritableDatabase();
        int countOfDeletedRows = db.delete(TABLE_NAME, null, null);
        db.close();

        if(countOfDeletedRows > 0) {
            Log.d(LOG_TAG, "Deleted " + countOfDeletedRows + " records");
        } else {
            Log.e(LOG_TAG, "Failed to delete records");
        }
    }

    @Override
    public void deleteTextStyleById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ID_COLUMN + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id)};
        int countOfDeletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();

        if(countOfDeletedRows > 0) {
            Log.d(LOG_TAG, "Deleted " + countOfDeletedRows + " records");
        } else {
            Log.e(LOG_TAG, "Failed to delete records");
        }
    }
}
