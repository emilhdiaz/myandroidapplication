package com.emildiaz.myandroidapplication.models;

import android.database.sqlite.SQLiteDatabase;

public class CommentTable {
    public static final String TABLE_NAME = "comments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_COMMENT = "comment";
    public static final String[] COLUMNS = {
        COLUMN_ID,
        COLUMN_CONTACT_ID,
        COLUMN_COMMENT
    };

    private static final String DATABASE_DROP =
        "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String DATABASE_CREATE =
        "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_CONTACT_ID + " INTEGER NOT NULL," +
            COLUMN_COMMENT + " TEXT NOT NULL" +
        ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(DATABASE_DROP);
        onCreate(database);
    }
}