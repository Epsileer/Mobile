package com.example.zues.healthok.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Db extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Mesages";
    private static final String TABLE_GCM = "GCM";
    private static final String KEY_ID = "time";
    private static final String KEY_NAME = "message";


    public Db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GCM + "("
                + KEY_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GCM);

        // Create tables again
        onCreate(db);
    }
    public void deleteNote( String t) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GCM,KEY_NAME +"="+" "+"'"+t+"'",null);
        db.close();
    }


   public  void addMessges(String message)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, message); // Contact Name

         // Contact Phone

        // Inserting Row
        db.insert(TABLE_GCM, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<String> getAllContacts() {
        List<String> contactList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GCM+" ORDER BY time DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


                // Adding contact to list
                contactList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
}
