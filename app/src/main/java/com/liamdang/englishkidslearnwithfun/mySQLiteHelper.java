package com.liamdang.englishkidslearnwithfun;

import  android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLData;

public class mySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_HIGH_SCORES = "highscores";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FRUITS = "fruits";
    public static final String COLUMN_ANIMALS = "animals";
    public static final String COLUMN_COLORS = "colors";
    public static final String COLUMN_FOOD = "food";
    public static final String COLUMN_ALPHABET = "alphabet";
    public static final String COLUMN_NUMBER = "numbers";


    private static final String DATABASE_NAME = "highscores.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table " + TABLE_HIGH_SCORES
            + "( "
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_ANIMALS + " integer,"
            + COLUMN_FRUITS + " integer,"
            + COLUMN_FOOD + " integer,"
            + COLUMN_COLORS + " integer,"
            + COLUMN_ALPHABET + " integer,"
            + COLUMN_NUMBER + " integer"
            + ");";

    public mySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGH_SCORES );
        db.execSQL(DATABASE_CREATE);
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ANIMALS, 0);
        cv.put(COLUMN_FRUITS, 0);
        cv.put(COLUMN_FOOD, 0);
        cv.put(COLUMN_COLORS, 0);
        cv.put(COLUMN_ALPHABET, 0);
        cv.put(COLUMN_NUMBER, 0);

        db.insert(TABLE_HIGH_SCORES, null, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVers, int newVers) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGH_SCORES );
        onCreate(db);
    }
}
