package com.example.javerill.wailpay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javerill on 9/22/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static Variables

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "WailPay";

    // Users Table Name
    private static final String TABLE_MSUSER = "MsUser";

    // Users Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FULLNAME = "fullName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERID = "userID";
    private static final String KEY_PASSWORD = "password";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MSUSER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FULLNAME + " TEXT,"
                + KEY_EMAIL + " TEXT, " + KEY_USERID + " TEXT,"
                + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSUSER);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding New User
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FULLNAME, user.getFullName()); // User Full Name
        values.put(KEY_EMAIL, user.getEmail()); // User Email
        values.put(KEY_USERID, user.getUserID()); // User ID
        values.put(KEY_PASSWORD, user.getPassword()); // User Password

        // Inserting Row
        db.insert(TABLE_MSUSER, null, values);
        // Closing Database Connection
        db.close();
    }

    // Getting Single User
//    public User getUser(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_MSUSER, new String[] { KEY_ID,
//                        KEY_FULLNAME, KEY_EMAIL, KEY_USERID, KEY_PASSWORD }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        User user = new User(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
//
//        // Return User
//        return user;
//    }

    // Getting User By UserID and Password
    public User getUserByUID(String userID, String password) {
        String selectQueryByCondition = "SELECT * FROM " + TABLE_MSUSER + " WHERE userID = ? " +
                "AND password = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQueryByCondition, new String[]{ userID, password });

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

        return user;
    }

    public boolean checkIfExist(String userID, String password) {
        String selectQueryByCondition = "SELECT * FROM " + TABLE_MSUSER + " WHERE userID = ? " +
                "AND password = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQueryByCondition, new String[]{ userID, password });

        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }

    // Getting All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MSUSER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping Through All Rows and Adding to List
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setFullName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setUserID(cursor.getString(3));
                user.setPassword(cursor.getString(4));

                // Adding User to List
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // Return User List
        return userList;
    }

    // Getting Users Count
    public int getUsersCount() {
        String countQuery = "SELECT * FROM " + TABLE_MSUSER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating Single User
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FULLNAME, user.getFullName()); // User Full Name
        values.put(KEY_EMAIL, user.getEmail()); // User Email
        values.put(KEY_USERID, user.getUserID()); // User ID
        values.put(KEY_PASSWORD, user.getPassword()); // User Password

        // updating row
        return db.update(TABLE_MSUSER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getID()) });
    }

    // Deleting Single User
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MSUSER, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getID()) });
        db.close();
    }

}
