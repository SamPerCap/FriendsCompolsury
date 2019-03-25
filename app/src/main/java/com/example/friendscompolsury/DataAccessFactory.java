package com.example.friendscompolsury;

import android.content.Context;

public class DataAccessFactory {
    static IDataCRUD mInstance;

    public static void init(Context context)
    {
        mInstance = new SQLiteImplementation(context);
    }
    public static IDataCRUD getInstance()
    {
        return mInstance;
    }
}
