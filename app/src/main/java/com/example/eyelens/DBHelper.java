package com.example.eyelens;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RECEPT";
    public static final String TABLE_RECEPT = "recept";

    public static final String L1 = "L1";
    public static final String L2 = "L2";
    public static final String L3 = "L3";
    public static final String L4 = "L4";
    public static final String L5 = "L5";

    public static final String R1 = "R1";
    public static final String R2 = "R2";
    public static final String R3 = "R3";
    public static final String R4 = "R4";
    public static final String R5 = "R5";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECEPT + " (" +
                L1 + " text, " + L2 + " text, " + L3 + " text, " + L4 + " text, " + L5 + " text, " +
                R1 + " text, " + R2 + " text, " + R3 + " text, " + R4 + " text, " + R5 + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_RECEPT);
        onCreate(db);
    }
}
