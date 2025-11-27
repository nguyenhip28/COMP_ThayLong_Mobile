package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hike_db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    public static final String TABLE_HIKES = "hikes";
    public static final String TABLE_OBSERVATIONS = "observations";

    // Hike Table Columns
    public static final String KEY_HIKE_ID = "id";
    public static final String KEY_HIKE_NAME = "name";
    public static final String KEY_HIKE_LOCATION = "location";
    public static final String KEY_HIKE_DATE = "date";
    public static final String KEY_HIKE_PARKING = "parking_available";
    public static final String KEY_HIKE_LENGTH = "length";
    public static final String KEY_HIKE_DIFFICULTY = "difficulty";
    public static final String KEY_HIKE_DESCRIPTION = "description";
    public static final String KEY_HIKE_WEATHER = "weather";
    public static final String KEY_HIKE_GROUP_SIZE = "group_size";

    // Observation Table Columns
    public static final String KEY_OBSERVATION_ID = "id";
    public static final String KEY_OBSERVATION_TEXT = "observation_text";
    public static final String KEY_OBSERVATION_TIME = "time_of_observation";
    public static final String KEY_OBSERVATION_COMMENT = "additional_comments";
    public static final String KEY_OBSERVATION_HIKE_ID = "hike_id";

    // Hike Table Create Statement
    private static final String CREATE_TABLE_HIKES = "CREATE TABLE " + TABLE_HIKES + "(" +
            KEY_HIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_HIKE_NAME + " TEXT NOT NULL," +
            KEY_HIKE_LOCATION + " TEXT NOT NULL," +
            KEY_HIKE_DATE + " TEXT NOT NULL," +
            KEY_HIKE_PARKING + " INTEGER NOT NULL," +
            KEY_HIKE_LENGTH + " REAL NOT NULL," +
            KEY_HIKE_DIFFICULTY + " TEXT NOT NULL," +
            KEY_HIKE_DESCRIPTION + " TEXT," +
            KEY_HIKE_WEATHER + " TEXT," +
            KEY_HIKE_GROUP_SIZE + " INTEGER)";

    // Observation Table Create Statement
    private static final String CREATE_TABLE_OBSERVATIONS = "CREATE TABLE " + TABLE_OBSERVATIONS + "(" +
            KEY_OBSERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_OBSERVATION_TEXT + " TEXT NOT NULL," +
            KEY_OBSERVATION_TIME + " TEXT NOT NULL," +
            KEY_OBSERVATION_COMMENT + " TEXT," +
            KEY_OBSERVATION_HIKE_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + KEY_OBSERVATION_HIKE_ID + ") REFERENCES " + TABLE_HIKES + "(" + KEY_HIKE_ID + ") ON DELETE CASCADE)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HIKES);
        db.execSQL(CREATE_TABLE_OBSERVATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // A simple upgrade policy is to drop tables and recreate them.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        onCreate(db);
    }

    // --- Hike Methods ---
    public void addHike(String name, String location, String date, int parking, double length, String difficulty, String description, String weather, int groupSize) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HIKE_NAME, name);
        values.put(KEY_HIKE_LOCATION, location);
        values.put(KEY_HIKE_DATE, date);
        values.put(KEY_HIKE_PARKING, parking);
        values.put(KEY_HIKE_LENGTH, length);
        values.put(KEY_HIKE_DIFFICULTY, difficulty);
        values.put(KEY_HIKE_DESCRIPTION, description);
        values.put(KEY_HIKE_WEATHER, weather);
        values.put(KEY_HIKE_GROUP_SIZE, groupSize);
        db.insert(TABLE_HIKES, null, values);
        db.close();
    }

    public int updateHike(int id, String name, String location, String date, int parking, double length, String difficulty, String description, String weather, int groupSize) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HIKE_NAME, name);
        values.put(KEY_HIKE_LOCATION, location);
        values.put(KEY_HIKE_DATE, date);
        values.put(KEY_HIKE_PARKING, parking);
        values.put(KEY_HIKE_LENGTH, length);
        values.put(KEY_HIKE_DIFFICULTY, difficulty);
        values.put(KEY_HIKE_DESCRIPTION, description);
        values.put(KEY_HIKE_WEATHER, weather);
        values.put(KEY_HIKE_GROUP_SIZE, groupSize);

        return db.update(TABLE_HIKES, values, KEY_HIKE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<Hike> getAllHikes() {
        List<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HIKES, null);
        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HIKE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HIKE_PARKING)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_HIKE_LENGTH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_DIFFICULTY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_WEATHER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HIKE_GROUP_SIZE))
                );
                hikeList.add(hike);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hikeList;
    }

    public List<Hike> searchHikes(String query) {
        List<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        String selection = KEY_HIKE_NAME + " LIKE ? OR " +
                           KEY_HIKE_LOCATION + " LIKE ? OR " +
                           KEY_HIKE_DATE + " LIKE ? OR " +
                           "CAST(" + KEY_HIKE_LENGTH + " AS TEXT) LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.query(TABLE_HIKES, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HIKE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HIKE_PARKING)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_HIKE_LENGTH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_DIFFICULTY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_WEATHER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HIKE_GROUP_SIZE))
                );
                hikeList.add(hike);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hikeList;
    }

    public Hike getHikeById(int hikeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_HIKES, null, KEY_HIKE_ID + "=?", new String[]{String.valueOf(hikeId)}, null, null, null, null);
        Hike hike = null;
        if (cursor != null && cursor.moveToFirst()) {
            hike = new Hike(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HIKE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_NAME)),
                    // ... (populate other fields)
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HIKE_PARKING)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_HIKE_LENGTH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_DIFFICULTY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_HIKE_WEATHER)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HIKE_GROUP_SIZE))
            );
            cursor.close();
        }
        db.close();
        return hike;
    }

    public void deleteHike(int hikeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HIKES, KEY_HIKE_ID + " = ?", new String[]{String.valueOf(hikeId)});
        db.close();
    }

    public void deleteAllHikes() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HIKES, null, null);
        db.close();
    }

    // --- Observation Methods ---
    public void addObservation(int hikeId, String observation, String time, String comment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OBSERVATION_HIKE_ID, hikeId);
        values.put(KEY_OBSERVATION_TEXT, observation);
        values.put(KEY_OBSERVATION_TIME, time);
        values.put(KEY_OBSERVATION_COMMENT, comment);
        db.insert(TABLE_OBSERVATIONS, null, values);
        db.close();
    }

    public List<Observation> getObservationsForHike(int hikeId) {
        List<Observation> observationList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_OBSERVATIONS, null, KEY_OBSERVATION_HIKE_ID + "=?", new String[]{String.valueOf(hikeId)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                observationList.add(new Observation(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_COMMENT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_HIKE_ID))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return observationList;
    }

    public Observation getObservationById(int observationId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_OBSERVATIONS, null, KEY_OBSERVATION_ID + "=?", new String[]{String.valueOf(observationId)}, null, null, null, null);
        Observation observation = null;
        if (cursor != null && cursor.moveToFirst()) {
            observation = new Observation(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_TEXT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_TIME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_COMMENT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_OBSERVATION_HIKE_ID))
            );
            cursor.close();
        }
        db.close();
        return observation;
    }

    public int updateObservation(int observationId, String newObservationText, String newComment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OBSERVATION_TEXT, newObservationText);
        values.put(KEY_OBSERVATION_COMMENT, newComment);
        int rowsAffected = db.update(TABLE_OBSERVATIONS, values, KEY_OBSERVATION_ID + " = ?", new String[]{String.valueOf(observationId)});
        db.close();
        return rowsAffected;
    }

    public void deleteObservation(int observationId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, KEY_OBSERVATION_ID + " = ?", new String[]{String.valueOf(observationId)});
        db.close();
    }
}
