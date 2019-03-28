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

    private static final String DATABASE_NAME = "agenda.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "contacts_table";
    private static final String INSERT = "insert into " + TABLE_NAME
            + "(name, phone, adress, location, mail, website, birthday, picture) values (?,?,?,?,?,?,?,?)";
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
        this.insertStmt.bindString(1, p.getName());
        this.insertStmt.bindString(2, "12345678");
        long id = this.insertStmt.executeInsert();
        p. = id;
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
        Log.d(MainActivity.TAG, "Updating a person " + p.m_id);

    }

    @Override
    public List<BEFriend> getAllPersons() {
        Log.d(MainActivity.TAG, "Getting all persons");
        List<BEFriend> list = new ArrayList<BEFriend>();
        Cursor cursor = this.db.query(TABLE_NAME,
                new String[]{"id", "name", "phone", "address", "location", "mail", "website", "birthday", "picture"},
                null, null,
                null, null, "name");
        if (cursor.moveToFirst()) {
            do {
                //For each column it will take a value.
                list.add(new BEFriend(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getFloat(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7),
                        cursor.getInt(8)));
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
            return new BEFriend(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getFloat(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getInt(8));
        }
        throw new IllegalArgumentException("Could not get Person by Id " + id);
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, name TEXT, phone TEXT, address TEXT, location REAL," +
                    " mail TEXT, website TEXT, birthday TEXT, picture INTEGER)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
