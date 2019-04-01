package com.example.friendscompolsury;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.friendscompolsury.Adaptor.FreindsAdaptor;
import com.example.friendscompolsury.Model.BEFriend;

import java.util.ArrayList;

import dk.easv.friendsv2.R;

public class MainActivity extends Activity {

    public static String TAG = "Friend2";
    ListView list;
    Friends m_friends;
    Context context = this;
    ArrayList<BEFriend> friends;
    public static IDataCRUD dataCRUD;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);
        m_friends = new Friends();
        dataCRUD = new SQLiteImplementation(this);
        dataCRUD.getAllPersons().clear();
        dataCRUD.addPerson(new BEFriend(1,"Example", "Street",
                "000000", "example@gmail.com", "www.example.com",
                "18-00-2001", 0, 0, 0));
        dataCRUD.getAllPersons();
        m_friends.setArraylistFried((ArrayList) dataCRUD.getAllPersons());
        FreindsAdaptor adapter = new FreindsAdaptor(this, (ArrayList) dataCRUD.getAllPersons(), m_friends.getNames());
        list = (ListView) findViewById(R.id.listview);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //BEFriend Slecteditem= m_f;
                Intent x = new Intent(context, DetailActivity.class);
                Log.d(TAG, "Detail activity will be started");
                BEFriend friend = m_friends.getAll().get(position);
                addData(x, friend);
                startActivity(x);
                Log.d(TAG, "Detail activity is started");
            }
        });
    }


   /* @Override
    public void onListItemClick(ListView parent, View v, int position,
                                long id) {

        Intent x = new Intent(this, DetailActivity.class);
        Log.d(TAG, "Detail activity will be started");
        BEFriend friend = m_friends.getAll().get(position);
        addData(x, friend);
        startActivity(x);
        Log.d(TAG, "Detail activity is started");

    }*/

    private void addData(Intent x, BEFriend f) {
        Log.d(TAG, "addData: " + f.toString());
        x.putExtra("friend", f);
    }


}
