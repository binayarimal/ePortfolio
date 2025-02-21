package com.example.mobileapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class databaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_EVENTS = "events";

    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_POST_USER_ID_FK = "userId";
    private static final String EVENT_DATA = "event";
    private static final String EVENT_DATE = "date";


    public static final String DBNAME = "eventTracker.db";

    public databaseHelper(Context context) {
        super(context, "eventTracker.db", null, 1);
    }


    //Creates data tables for events and users
    @Override
    public void onCreate(SQLiteDatabase myDB) {
        myDB.execSQL("create Table users(username TEXT, id INTEGER primary key autoincrement, password TEXT)");

        myDB.execSQL("create Table events(date TEXT,id INTEGER primary key autoincrement, event TEXT)");



    }

    //prevents from creating duplicate tables
    @Override
    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {
        myDB .execSQL("drop Table if exists users");
        myDB .execSQL("drop Table if exists events");

    }
    //Adding user name
    public Boolean insertUserData(String username, String password){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);

        long results = MyDB.insert("users", null, contentValues);
        return results != -1;

    }
    //Check if username exist
    public Boolean checkUser(String username, String password){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
        return cursor.getCount() > 0;

    }

    //adding event data
    public Boolean insertEventData(String eventData, String dateData){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("event", eventData);
        contentValues.put("date", dateData);


        long results = MyDB.insert("events", null, contentValues);
        return results != -1;

    }
    //Reading event data, returns cursor object that needs to be parsed
    public Cursor getEvents(){
        SQLiteDatabase MyDB = getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from events", null);
        return cursor;

    }

    //Deletes events

    public Boolean deleteEvent(Integer eventId){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from events where id = ?", new String[]{String.valueOf(eventId)});
        if(cursor.getCount() > 0){
            long result = MyDB.delete("events","id=?", new String[]{String.valueOf(eventId)});
            return result != -1;
        }else{
            return false;
        }

    }

    //Updates events
    public Boolean updateEvent(Integer eventId, String event, String date){
        ContentValues values= new ContentValues();
        values.put("event", event);
        values.put("date", date);
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from events where id = ?", new String[]{String.valueOf(eventId)});
        if(cursor.getCount() > 0){

            long result = MyDB.update("events",values, "id = ?", new String[]{String.valueOf(eventId)});
            return result != -1;
        }else{
            return false;
        }

    }






}

