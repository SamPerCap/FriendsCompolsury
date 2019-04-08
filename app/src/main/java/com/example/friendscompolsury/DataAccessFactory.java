package com.example.friendscompolsury;

import android.content.Context;
import android.util.Log;

import com.example.friendscompolsury.Adaptor.FriendsAdaptor;
import com.example.friendscompolsury.Model.BEFriend;

import java.util.ArrayList;

import dk.easv.friendsv2.R;


public class DataAccessFactory {
    static IDataCRUD mInstance;
    private String TAG = MainActivity.TAG;
    private ArrayList<BEFriend> friendsList;


    protected void init(Context context) {
        mInstance = new SQLiteImplementation(context);
        friendsList = new ArrayList<>();
    }

    protected static IDataCRUD getInstance() {
        return mInstance;
    }

    public ArrayList<BEFriend> getFriendsList() {
        /*if (friendsList.isEmpty()){
            mInstance.addPerson(new BEFriend("Example", "Street",
                    "000000", "example@gmail.com", "www.example.com",
                    "18-00-2001", 0, 0, R.drawable.lupa));
        }*/
        for (BEFriend person : mInstance.getAllPersons()) {
            friendsList.add(person);
            Log.d(TAG, person.getM_name()+" "+ person.getM_address());
        }
        return friendsList;
    }

    public void setFriendsList(ArrayList<BEFriend> friendsList) {
        this.friendsList = friendsList;
    }

    protected void clearArrayList() {
        if (!friendsList.isEmpty())
        friendsList.clear();
    }

    protected void updateContact(BEFriend contactInfo) {
        BEFriend personToUpdate = contactInfo;
        mInstance.updatePerson(personToUpdate);
        getFriendsList();
    }

    protected void addContact(BEFriend contactInfo) {
        mInstance.addPerson(contactInfo);
        friendsList.add(contactInfo);
        Log.d(TAG, "New person added");
        getFriendsList();
    }


}
