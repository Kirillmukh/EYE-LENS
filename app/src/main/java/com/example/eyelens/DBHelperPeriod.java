package com.example.eyelens;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperPeriod extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PERIOD";
    public static final String TABLE_PERIOD = "period";

    public static final String FINALTIME = "FINAL_PERIOD";
    public static final String LENGTH = "LENGTH";

    public DBHelperPeriod(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PERIOD + " (" + FINALTIME + " integer, " + LENGTH  + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_PERIOD);
        onCreate(db);
    }
}
