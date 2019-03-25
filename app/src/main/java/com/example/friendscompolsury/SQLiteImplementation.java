package com.example.friendscompolsury;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class SQLiteImplementation implements IDataCRUD {

    private static final String DATABASE_NAME = "agenda.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "contacts_table";
    private static final String INSERT = "insert into " + TABLE_NAME
            + "(name, phone) values (?,?)";
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
    public long addPerson(BEPerson p) {
        Log.d(MainActivity.TAG, "Adding a new person.");
        this.insertStmt.bindString(1, p.m_name);
        this.insertStmt.bindString(2, "12345678");
        long id = this.insertStmt.executeInsert();
        p.m_id = id;
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
    public void updatePerson(BEPerson p) {
        Log.d(MainActivity.TAG, "Updating a person " + p.m_id);

    }

    @Override
    public List<BEPerson> getAllPersons() {
        Log.d(MainActivity.TAG, "Getting all persons");
        List<BEPerson> list = new ArrayList<BEPerson>();
        Cursor cursor = this.db.query(TABLE_NAME,
                new String[]{"id", "name", "phone"},
                null, null,
                null, null, "name");
        if (cursor.moveToFirst()) {
            do {
                list.add(new BEPerson(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    @Override
    public BEPerson getPersonById(long id) {
        Log.d(MainActivity.TAG,"Getting a person by id " + id);
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE id = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"" + id});
        if (cursor.moveToFirst()) {
            return new BEPerson(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
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
                    + " (id INTEGER PRIMARY KEY, name TEXT, phone TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
