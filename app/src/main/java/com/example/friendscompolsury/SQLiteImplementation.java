package com.example.friendscompolsury;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.friendscompolsury.Model.BEFriend;

import java.util.ArrayList;
import java.util.List;

public class SQLiteImplementation implements IDataCRUD {

    private static String TAG = MainActivity.TAG;
    private static final String DATABASE_NAME = "agenda.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "contacts_table";
    private static final String INSERT = "insert into " + TABLE_NAME
            + "(name, address, number, email, website, birthday ,location_x, location_y, picture)" +
            " values (?,?,?,?,?,?,?,?,?)";
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;

    public SQLiteImplementation(Context context) {
        Log.d(MainActivity.TAG, "Implementing SQLite, openHelper will be invoked");
        // OpenHelper is a class that extends SQLiteOpenHelper.
        OpenHelper openHelper = new OpenHelper(context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
    }

    @Override
    public long addPerson(BEFriend p) {
        Log.d(MainActivity.TAG, "Adding a new person.");
        this.insertStmt.bindString(1, p.getM_name());
        this.insertStmt.bindString(2, p.getM_address());
        this.insertStmt.bindString(3, p.getM_phone());
        this.insertStmt.bindString(4, p.getM_email());
        this.insertStmt.bindString(5, p.getM_webSite());
        this.insertStmt.bindString(6, p.getM_birthday());
        this.insertStmt.bindDouble(7, p.getM_location_x());
        this.insertStmt.bindDouble(8, p.getM_location_y());
        this.insertStmt.bindDouble(9, p.getM_img());
        long id = this.insertStmt.executeInsert();
        p.setM_id(id);
        return id;
    }

    @Override
    public void deleteAll() {
        Log.d(MainActivity.TAG, "Deleting all");
        this.db.delete(TABLE_NAME, null, null);
    }

    @Override
    public void deleteById(long id) {
        Log.d(MainActivity.TAG, "Deleting by id " + id);
        this.db.delete(TABLE_NAME, "id = ?", new String[]{"" + id});
    }

    @Override
    public void updatePerson(BEFriend p) {
        Log.d(MainActivity.TAG, "Updating a person " + p.getM_id());

    }

    @Override
    public List<BEFriend> getAllPersons() {
        Log.d(MainActivity.TAG, "Getting all persons");
        List<BEFriend> list = new ArrayList<BEFriend>();
        Cursor cursor = this.db.query(TABLE_NAME,
                new String[]{"id", "name", "address", "number","email","website", "birthday", "location_x",
                        "location_y", "picture"},
                null, null,
                null, null, "name");
        if (cursor.moveToFirst()) {
            do {
                //For each column it will take a value.
                list.add(new BEFriend(cursor.getLong(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getFloat(7),
                        cursor.getFloat(8), cursor.getInt(9)));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    @Override
    public BEFriend getPersonById(long id) {
        Log.d(MainActivity.TAG,"Getting a person by id " + id);
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE id = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"" + id});
        if (cursor.moveToFirst()) {
            //For each column it will take a value.
            return new BEFriend(cursor.getLong(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6), cursor.getFloat(7),
                    cursor.getFloat(8), cursor.getInt(9));
        }
        throw new IllegalArgumentException("Could not get Person by Id " + id);
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(MainActivity.TAG, "onCreate: table");
            db.execSQL("CREATE TABLE " + TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, name TEXT, address TEXT, number TEXT, email TEXT," +
                    "website TEXT,birthday TEXT,  location_x REAL, location_y REAL, picture INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
            Log.d(TAG,"Table should have been created");
        }
    }
}
