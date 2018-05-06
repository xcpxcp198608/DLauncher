package com.px.dlauncher.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "SMART_TV";

    public static final String TABLE_NAME = "apps";
    private static final String CREATE_TABLE = "create table if not exists "+TABLE_NAME+"(_id integer primary key autoincrement," +
            "label text ,packageName text,type text , shortcut text)";
    private static final String DROP_TABLE= "drop table if exists "+TABLE_NAME;

    private static final int VERSION =11;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        this.onCreate(db);
    }
}
