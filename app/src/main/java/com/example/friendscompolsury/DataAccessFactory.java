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
        clearArrayList();
        for (BEFriend person : mInstance.getAllPersons()) {
            friendsList.add(person);
            Log.d(TAG, person.getM_name()+" "+ person.getM_address());
        }
        return friendsList;
    }
    public BEFriend getFriendByName(BEFriend p) {

        return mInstance.getPersonByName(p);
    }

    public BEFriend getFriendByID(long ID){
        return mInstance.getPersonById(ID);
    }
    protected void deleteEverything(){
        mInstance.deleteAll();
        clearArrayList();
    }

    public void setFriendsList(ArrayList<BEFriend> friendsList) {
        this.friendsList = friendsList;
    }

    protected void clearArrayList() {
        friendsList.clear();
    }

    public void updateContact(BEFriend contactInfo) {
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
    protected void removeByID(long ID){
        mInstance.deleteById(ID);
    }


}
