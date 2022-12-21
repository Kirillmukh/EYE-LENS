package com.example.eyelens;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperUnique extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UNIVERSE";
    public static final String TABLE_UNIQUE = "universe";

    public static String NOTIFY_HOURS = "HOURS";
    public static String NOTIFY_MINUTES = "MINUTES";


    public DBHelperUnique(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_UNIQUE + " (" + NOTIFY_HOURS + " integer, " + NOTIFY_MINUTES + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_UNIQUE);
        onCreate(db);
    }
}
