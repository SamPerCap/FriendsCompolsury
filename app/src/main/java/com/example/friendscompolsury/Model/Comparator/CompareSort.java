package com.example.friendscompolsury.Model.Comparator;

import android.util.Log;

import com.example.friendscompolsury.MainActivity;
import com.example.friendscompolsury.Model.BEFriend;

import java.util.Comparator;

public class CompareSort implements Comparator<BEFriend> {

    public int compare(BEFriend a, BEFriend b)
    {
        Log.d(MainActivity.TAG, "compare: "+a.getM_name() +b.getM_name());

        return a.getM_name().toUpperCase().compareTo(b.getM_name().toUpperCase());

    }
}
