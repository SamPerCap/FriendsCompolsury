package com.example.friendscompolsury;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.friendscompolsury.Adaptor.FriendsAdaptor;
import com.example.friendscompolsury.Model.BEFriend;
import com.example.friendscompolsury.Model.Comparator.CompareSort;

import java.util.ArrayList;
import java.util.Collections;

import dk.easv.friendsv2.R;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "Friend2";
    ListView list;
    FloatingActionButton addContactButton;
    Context context = this;
    BEFriend _friends;
    ArrayList<BEFriend> friendsList = new ArrayList<>();
    DataAccessFactory _dataAccess;
    IDataCRUD _dataCRUD;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);
        _dataAccess.init(MainActivity.this);
        _dataCRUD = _dataAccess.getInstance();
        _dataCRUD.getAllPersons().clear();
        _dataCRUD.addPerson(new BEFriend("jacob", "Street",
                "000000", "example@gmail.com", "www.example.com",
                "18-00-2001", 0, 0, "/storage/emulated/0/DCIM/Camera/hej.jpg"));
        for (BEFriend person : _dataCRUD.getAllPersons()){
        friendsList.add(person);
        }
        FriendsAdaptor adapter = new FriendsAdaptor(this, friendsList);
        list = (ListView) findViewById(R.id.listview);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, DetailActivity.class);
                Log.d(TAG, "Detail activity will be started");
                //_dataCRUD.getAll().get(position);
                addData(intent, _friends);
                startActivity(intent);
                Log.d(TAG, "Detail activity is started");
            }
        });
        addContactButton = (FloatingActionButton) findViewById(R.id.addNewContact);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddContactActivity.class));
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
        Log.d(TAG, "adding Data to details");
        x.putExtra("friend", f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void changeSort(MenuItem item) {
        Log.d(TAG, "sort changing to "+item.getTitle());
        ArrayList<BEFriend> asd =(ArrayList<BEFriend>) _dataCRUD.getAllPersons();
        Collections.sort(asd, new CompareSort());
        //FriendsAdaptor adapter=new FriendsAdaptor(this, asd, friendsList);
        //list.setAdapter(adapter);

    }
}
