package com.liamdang.englishkidslearnwithfun.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.liamdang.englishkidslearnwithfun.mySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class HighScores {

    private static SQLiteDatabase database;
    private static mySQLiteHelper dbHelp;
    private static String[] all_columns = {
            mySQLiteHelper.COLUMN_FRUITS,
            mySQLiteHelper.COLUMN_ANIMALS,
            mySQLiteHelper.COLUMN_FOOD,
            mySQLiteHelper.COLUMN_COLORS,
            mySQLiteHelper.COLUMN_ALPHABET,
            mySQLiteHelper.COLUMN_NUMBER
    };

    public static void open(Context context) throws SQLException {
        dbHelp = new mySQLiteHelper(context);

        database = dbHelp.getWritableDatabase();

        //System.out.println(getAllHighScores());
        /*
        String[] col = {"alphabett"};
        Cursor cursor = database.query(mySQLiteHelper.TABLE_HIGH_SCORES, col,null, null, null, null, null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        Toast.makeText(context.getApplicationContext(), "Test: " + result, Toast.LENGTH_SHORT).show();

         */

    }

    public static void close() {
        dbHelp.close();
    }

    public static List<Integer> getAllHighScores() {
        Cursor cursor = database.query(mySQLiteHelper.TABLE_HIGH_SCORES, all_columns, null, null, null, null, null);
        cursor.moveToFirst();
        List<Integer> highScores = new ArrayList<>();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            highScores.add(cursor.getInt(i));
        }

        cursor.close();
        return highScores;
    }

    public static int getHighScore(String column) {
        String[] col = {column};
        Cursor cursor = database.query(mySQLiteHelper.TABLE_HIGH_SCORES, col,null, null, null, null, null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        return result;
    }

    public static boolean setHighScore (String column, int newScore) {
        boolean result = false;
        String[] col = {column};
        Cursor cursor = database.query(mySQLiteHelper.TABLE_HIGH_SCORES, col,null, null, null, null, null);
        cursor.moveToFirst();
        int oldScore = cursor.getInt(0);
        if (newScore > oldScore) {
            ContentValues cv = new ContentValues();
            cv.put(column, newScore);
            database.update(mySQLiteHelper.TABLE_HIGH_SCORES, cv, mySQLiteHelper.COLUMN_ID + "=1",null);
            result = true;

        }
        cursor.close();
        return result;

    }

}
