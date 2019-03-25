package dk.easv.friendsv2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import dk.easv.friendsv2.Model.BEFriend;
import dk.easv.friendsv2.Model.Friends;

public class MainActivity extends ListActivity {

    public static String TAG = "Friend2";

    Friends m_friends;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Friends v2");
        m_friends = new Friends();

        ArrayList<BEFriend> friends;

        friends = m_friends.getAll();

        ListAdapter adapter =
                new ArrayAdapter<BEFriend>(this,
                        android.R.layout.simple_list_item_1,
                        friends);

        setListAdapter(adapter);

    }


    @Override
    public void onListItemClick(ListView parent, View v, int position,
                                long id) {

        Intent x = new Intent(this, DetailActivity.class);
        Log.d(TAG, "Detail activity will be started");
        BEFriend friend = m_friends.getAll().get(position);
        addData(x, friend);
        startActivity(x);
        Log.d(TAG, "Detail activity is started");

    }

    private void addData(Intent x, BEFriend f)
    {
        Log.d(TAG, "addData: " + f.toString());
        x.putExtra("friend", f);
    }



}
