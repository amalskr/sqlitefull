package com.ceylonapz.mysqlite.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ceylonapz.mysqlite.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ceylonApz on 2018-03-14
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "booking.db";
    private static final String TABLE_MESSAGE = "message";

    // Message Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_MSG_TITLE = "title";
    private static final String KEY_MSG = "message";
    private static final String KEY_MESSAGE_DATE = "msg_date";
    private static final String KEY_IS_READ = "is_read";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //REC
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MSG_TITLE + " TEXT,"
                + KEY_MSG + " TEXT,"
                + KEY_MESSAGE_DATE + " TEXT,"
                + KEY_IS_READ + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        onCreate(db);*/
    }

    // Adding new message
    public int addMessage(Message message) {

        int lastInsertId;

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            System.out.println("AddMessage "+message.getMsgTitle() +" "+ message.getMsgBody());

            ContentValues values = new ContentValues();
            values.put(KEY_MSG_TITLE, message.getMsgTitle());
            values.put(KEY_MSG, message.getMsgBody());
            values.put(KEY_MESSAGE_DATE, message.getMsgDate());
            values.put(KEY_IS_READ, 0); //not read  (true = 1 | false = 0)

            lastInsertId = (int) db.insert(TABLE_MESSAGE, null, values);
            db.close();
        } catch (Exception e) {
            lastInsertId = -1;
        }

        return lastInsertId;
    }

    // Getting All Message
    public List<Message> getAllMessages() {

        List<Message> messageList = new ArrayList<>();

        try {

            String selectQuery = "SELECT  * FROM " + TABLE_MESSAGE + " ORDER BY " + KEY_ID + " DESC";

            SQLiteDatabase db = this.getWritableDatabase();
            @SuppressLint("Recycle")
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    int KEY_ID = cursor.getInt(0);
                    String KEY_MSG_TITLE = cursor.getString(1);
                    String KEY_MSG = cursor.getString(2);
                    String KEY_MESSAGE_DATE = cursor.getString(3);
                    int KEY_IS_READ = cursor.getInt(4);

                    messageList.add(new Message(KEY_ID, KEY_MSG_TITLE, KEY_MSG, KEY_MESSAGE_DATE, KEY_IS_READ));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBHandler", "All : " + e.toString());
        }

        return messageList;
    }

    //update read status in message
    public void updateReadStatus(long msgId) {

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_IS_READ, 1);

            SQLiteDatabase db = this.getWritableDatabase();
            db.update(TABLE_MESSAGE, contentValues, KEY_ID + " = ?",
                    new String[]{String.valueOf(msgId)});
            db.close();
        } catch (Exception e) {
            Log.e("DBHandler", "Update : " + e.toString());
        }

    }

    // Deleting single record
    public void deleteMessage(int msgId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_MESSAGE, KEY_ID + " = ?",
                    new String[]{String.valueOf(msgId)});
            db.close();
        } catch (Exception e) {
            Log.e("DBHandler", "Delete E : " + e.toString());
        }

    }


    // Getting unread Count
    public int getUnreadCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGE + " WHERE " + KEY_IS_READ + " = 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

}
