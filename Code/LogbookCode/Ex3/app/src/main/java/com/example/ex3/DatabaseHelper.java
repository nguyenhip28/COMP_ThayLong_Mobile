package com.example.ex3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "contacts.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE = "contacts";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_DOB = "dob";
    public static final String COL_EMAIL = "email";
    public static final String COL_AVATAR = "avatar";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT, "
                + COL_DOB + " TEXT, "
                + COL_EMAIL + " TEXT, "
                + COL_AVATAR + " INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void insertContact(Contact c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME, c.getName());
        cv.put(COL_DOB, c.getDob());
        cv.put(COL_EMAIL, c.getEmail());
        cv.put(COL_AVATAR, c.getAvatar());

        db.insert(TABLE, null, cv);
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new Contact(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4)
                ));
            } while (cursor.moveToNext());
        }
        return list;
    }
}
