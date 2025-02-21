package com.example.mobileapp;//package com.example.mobileapp;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//
//public class databaseHelper extends SQLiteOpenHelper {
//    private static final String TABLE_EVENTS = "events";
//
//    private static final String KEY_EVENT_ID = "id";
//    private static final String KEY_POST_USER_ID_FK = "userId";
//    private static final String EVENT_DATA = "event";
//    private static final String EVENT_DATE = "date";
//
//
//    public static final String DBNAME = "eventTracker.db";
//
//    public databaseHelper(Context context) {
//        super(context, "eventTracker.db", null, 1);
//    }
//
//
//    //Creates data tables for events and users
//    @Override
//    public void onCreate(SQLiteDatabase myDB) {
//        myDB.execSQL("create Table users(username TEXT, id INTEGER primary key autoincrement, password TEXT)");
//
//        myDB.execSQL("create Table events(date TEXT,id INTEGER primary key autoincrement, event TEXT)");
//
//
//
//    }
//
//    //prevents from creating duplicate tables
//    @Override
//    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {
//        myDB .execSQL("drop Table if exists users");
//        myDB .execSQL("drop Table if exists events");
//
//    }
//    //Adding user name
//    public Boolean insertUserData(String username, String password){
//        SQLiteDatabase MyDB = this.getReadableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("username", username);
//        contentValues.put("password", password);
//
//        long results = MyDB.insert("users", null, contentValues);
//        return results != -1;
//
//    }
//    //Check if username exist
//    public Boolean checkUser(String username, String password){
//        SQLiteDatabase MyDB = this.getReadableDatabase();
//        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
//        return cursor.getCount() > 0;
//
//    }
//
//    //adding event data
//    public Boolean insertEventData(String eventData, String dateData){
//        SQLiteDatabase MyDB = this.getReadableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("event", eventData);
//        contentValues.put("date", dateData);
//
//
//        long results = MyDB.insert("events", null, contentValues);
//        return results != -1;
//
//    }
//    //Reading event data, returns cursor object that needs to be parsed
//    public Cursor getEvents(){
//        SQLiteDatabase MyDB = getReadableDatabase();
//        Cursor cursor = MyDB.rawQuery("Select * from events ORDER BY DATE ASC", null);
//        return cursor;
//
//    }
//
//    //Deletes events
//
//    public Boolean deleteEvent(Integer eventId){
//        SQLiteDatabase MyDB = this.getReadableDatabase();
//        Cursor cursor = MyDB.rawQuery("Select * from events where id = ?", new String[]{String.valueOf(eventId)});
//        if(cursor.getCount() > 0){
//            long result = MyDB.delete("events","id=?", new String[]{String.valueOf(eventId)});
//            return result != -1;
//        }else{
//            return false;
//        }
//
//    }
//
//    //Updates events
//    public Boolean updateEvent(Integer eventId, String event, String date){
//        ContentValues values= new ContentValues();
//        values.put("event", event);
//        values.put("date", date);
//        SQLiteDatabase MyDB = this.getReadableDatabase();
//        Cursor cursor = MyDB.rawQuery("Select * from events where id = ?", new String[]{String.valueOf(eventId)});
//        if(cursor.getCount() > 0){
//
//            long result = MyDB.update("events",values, "id = ?", new String[]{String.valueOf(eventId)});
//            return result != -1;
//        }else{
//            return false;
//        }
//
//    }
//
//
//
//
//
//
//}
//



import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import kotlin.Triple;

public class databaseHelper {
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseEvents;

    public databaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("users");
        databaseEvents = database.getReference("events");
    }

    // Add User Data
    public void insertUserData(String username, String password, OnUserInsertListener listener) {
        String userId = databaseUsers.push().getKey(); // Generates a unique key
        if (userId == null) {
            listener.onFailure("Failed to generate user ID.");
            return;
        }

        User user = new User(username, password);
        databaseUsers.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    listener.onSuccess(userId); // Pass generated userId back
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e.getMessage());
                });
    }

    // Callback Interface
    public interface OnUserInsertListener {
        void onSuccess(String userId);
        void onFailure(String errorMessage);
    }

    // Listener interface to handle the event data
    public interface OnEventsRetrievedListener {
        void onEventsRetrieved(List<Event> events);
    }



    public void getEvents(final OnEventsRetrievedListener listener) {
        // Use the default order by push key (this is implicitly done)
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> eventList = new ArrayList<>(); // List of Event objects

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        String eventId = snapshot.getKey();
                        event.setEventId(eventId);
                        eventList.add(event); // Add Event object directly to the list
                    }
                }
                Collections.reverse(eventList);

                // Call the listener with the retrieved events
                listener.onEventsRetrieved(eventList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to retrieve events: " + databaseError.getMessage());
            }
        });
    }


    // Add Event Data
    public void insertEventData(String event, String date, final FirebaseCallback callback) {
        String eventId = databaseEvents.push().getKey(); // Generate unique ID
        Event eventData = new Event(event, date);

        databaseEvents.child(eventId).setValue(eventData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onSuccess(false);
                    }
                });
    }

    public interface FirebaseCallback {
        void onSuccess(boolean success);
    }


    // Update Event Data
    public Boolean updateEvent(String eventId, String event, String date) {
        AtomicBoolean eventOutput = new AtomicBoolean(false);
        AtomicBoolean dateOutput = new AtomicBoolean(false);
        databaseEvents.child(eventId).child("event").setValue(event)
                .addOnSuccessListener(aVoid -> {
                    // Event insertion successful
                    eventOutput.set(true);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    eventOutput.set(false);
                });


        databaseEvents.child(eventId).child("date").setValue(date)
                .addOnSuccessListener(aVoid -> {
                    // Event insertion successful
                    dateOutput.set(true);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    dateOutput.set(false);
                });
        return eventOutput.get() == dateOutput.get();
    }

    // Delete Event Data
    public Boolean deleteEvent(String eventId) {
        AtomicBoolean output = new AtomicBoolean(false);
        databaseEvents.child(eventId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Event insertion successful
                    output.set(true);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    output.set(false);
                });
        return output.get() == output.get();
    }
    public void checkUser(String username, String password, OnUserCheckListener listener) {
        databaseUsers.orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                if (user != null && user.getPassword().equals(password)) {
                                    listener.onUserChecked(true); // Username and password match
                                    return;
                                }
                            }
                        }
                        listener.onUserChecked(false); // No match found
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onUserChecked(false); // Handle error case
                    }
                });
    }

    // Callback Interface
    public interface OnUserCheckListener {
        void onUserChecked(boolean isValid);
    }

}

// User model
 class User {

    private String username;
    private String password;


    // Default constructor (Firebase needs it)
    public User() {}

    // Constructor with parameters
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

// Event model
class Event {
    private static int counter;
    private String eventId;
    private String event;
    private String date;

    // Default constructor (Firebase needs it)
    public Event() {
        counter++;
    }

    // Constructor with parameters
    public Event(String event, String date) {
        this.event = event;
        this.date = date;
        this.eventId = String.valueOf(counter);
    }

    // Getters and setters
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}