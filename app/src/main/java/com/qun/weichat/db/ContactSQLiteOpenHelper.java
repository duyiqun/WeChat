package com.qun.weichat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Qun on 2017/5/23.
 */

public class ContactSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "contacts.db";
    private static final int VERSION = 1;

    private ContactSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ContactSQLiteOpenHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    /**
     * 初始化表结构
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table t_contact(_id integer primary key, username  varchar(16), contact varchar(16))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
