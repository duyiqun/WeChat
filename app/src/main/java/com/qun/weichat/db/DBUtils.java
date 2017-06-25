package com.qun.weichat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qun on 2017/5/23.
 */

public class DBUtils {

    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static List<String> getContacts(String username) {
        checkInit();
        ContactSQLiteOpenHelper contactSQLiteOpenHelper = new ContactSQLiteOpenHelper(sContext);
        List<String> contactList = new ArrayList<>();
        SQLiteDatabase database = contactSQLiteOpenHelper.getReadableDatabase();
        String sql = "select contact from t_contact where username=?";
        Cursor cursor = database.rawQuery(sql, new String[]{username});
        while (cursor.moveToNext()) {
            String contact = cursor.getString(0);
            contactList.add(contact);
        }
        cursor.close();
        database.close();
        contactSQLiteOpenHelper.close();
        return contactList;
    }

    /**
     * 更新username的好友列表
     * 1. 先删除username的所有好友
     * 2. 然后再插入username的最新的好友列表
     *
     * @param username
     * @param contacts
     */
    public static void updateContacts(String username, List<String> contacts) {
        checkInit();
        ContactSQLiteOpenHelper contactSQLiteOpenHelper = new ContactSQLiteOpenHelper(sContext);
        SQLiteDatabase database = contactSQLiteOpenHelper.getWritableDatabase();
        database.beginTransaction();
        //delete from t_contact where username='zhangsan'
        database.delete("t_contact", "username=?", new String[]{username});
        for (int i = 0; i < contacts.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("contact", contacts.get(i));
            // insert into t_contact(username,contact) values('zhangsan','lisi');
            database.insert("t_contact", null, values);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
        contactSQLiteOpenHelper.close();
    }

    private static void checkInit() {
        if (sContext == null) {
            throw new RuntimeException("DBUtils必须先初始化再使用");
        }
    }
}
