package com.nic.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //database name
    public static final String DATABASE_NAME = "jdk";
    //database version
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "tbl_notes1";
    public static final String TABLE_NAME1 = "sample";
    public static final String TABLE_NAME2 = "sample11";
    public  static final String IMAGE_TABLE_1="image_table_1";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        String query1;
        String images;
        //creating table
        query = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY, Title TEXT, Description TEXT,Profile BLOB NOT NULL)";
        query1 = "CREATE TABLE " + TABLE_NAME1 + "(ID INTEGER PRIMARY KEY, Title TEXT, Description TEXT , Latitude TEXT, Langtitude TEXT, Profile BLOB NOT NULL)";
        //query1 = "CREATE TABLE " + TABLE_NAME2 + "(ID INTEGER PRIMARY KEY, Title TEXT, Description TEXT , Latitude TEXT, Langtitude TEXT, Date DATETIME DEFAULT CURRENT_TIMESTAMP, Profile BLOB NOT NULL)";
        images="CREATE TABLE " + IMAGE_TABLE_1 + "(ID INTEGER PRIMARY KEY,images BLOB NOT NULL)";

        db.execSQL(query1);
        db.execSQL(images);
    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion>newVersion) {
            db.execSQL("ALTER TABLE sample ADD COLUMN Date DATETIME DEFAULT CURRENT_TIMESTAMP");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
            db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE_1);
            onCreate(db);
        }
    }

    //add the new note
    public void addNotes(String title, String des, double Latitude, double Langtitude, String date_time, byte[] profile) {
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        values.put("Description", des);
        values.put("Profile",profile);
        values.put("Latitude",Latitude);
        values.put("Langtitude",Langtitude);
        values.put("Date",date_time);


        //inserting new row
        sqLiteDatabase.insert(TABLE_NAME1, null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    //get the all notes
    public ArrayList<NoteModel> getNotes() {
        ArrayList<NoteModel> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + TABLE_NAME1;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setID(cursor.getString(0));
                noteModel.setTitle(cursor.getString(1));
                noteModel.setDes(cursor.getString(2));
                noteModel.setProfile(cursor.getBlob(cursor.getColumnIndexOrThrow("Profile")));
                noteModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude")));
                noteModel.setLangtitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Langtitude")));
                noteModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow("Date")));

                arrayList.add(noteModel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

    //delete the note
    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(TABLE_NAME1, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

    //update the note
    public void updateNote(String title, String des, String ID,double Latitude, double Langtitude, String date_time, byte[] profile) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("Title", title);
        values.put("Description", des);
        values.put("Profile",profile);
        values.put("Latitude",Latitude);
        values.put("Langtitude",Langtitude);
        values.put("Date",date_time);


        //updating row
        sqLiteDatabase.update(TABLE_NAME1, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }
}