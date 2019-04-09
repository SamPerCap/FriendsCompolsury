package com.example.friendscompolsury;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
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
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static String TAG = "Friend2";
    public static DataAccessFactory _dataAccess;
    ListView list;
    FloatingActionButton addContactButton;
    Context context;
    FriendsAdaptor adapter;
    Intent adapterIntent;
    AdapterView.AdapterContextMenuInfo menuInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);
        list = (ListView) findViewById(R.id.listview);
        _dataAccess = new DataAccessFactory();
        _dataAccess.init(MainActivity.this);
        context = this;

        askPremision();
        list = (ListView) findViewById(R.id.listview);

        SettingAdapter();
        registerForContextMenu(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                adapterIntent = new Intent(context, DetailActivity.class);
                Log.d(TAG, "Detail activity will be started");
                addData(adapterIntent, _dataAccess.getFriendsList().get(position));
                startActivity(adapterIntent);
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
    private void askPremision()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {


            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.INTERNET)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to CAMERA - requesting it");
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.CAMERA
                        , Manifest.permission.SEND_SMS
                        , Manifest.permission.CALL_PHONE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.INTERNET
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_NETWORK_STATE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }}

    private void SettingAdapter() {
        adapter = new FriendsAdaptor(this, _dataAccess.getFriendsList());
        list.setAdapter(adapter);
    }

    private void addData(Intent x, BEFriend f) {
        Log.d(TAG, "adding Data to details");
        x.putExtra("friend", f.getM_id());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    public void changeSort(MenuItem item) {
        Log.d(TAG, "sort changing to " + item.getTitle());
        ArrayList<BEFriend> newArray = _dataAccess.getFriendsList();
        Collections.sort(newArray, new CompareSort());
        FriendsAdaptor adapter = new FriendsAdaptor(this, newArray);
        list.setAdapter(adapter);
    }

    public void deleteEverything(MenuItem item) {
        Log.d(TAG, "Removing all contacts");
        _dataAccess.deleteEverything();
        _dataAccess.clearArrayList();
        adapter = new FriendsAdaptor(this, _dataAccess.getFriendsList());
        list.setAdapter(adapter);
        Log.d(TAG, "Database and Arraylist have been removed");
    }


    public void deleteContact(MenuItem item) {
        Log.d(TAG,"Removing contact choosen");
        // We get the item we have focused on to open the context menu
        menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        // Through the adapter, we are asking the object that is on the position we have choosen
        BEFriend itemFromList = (BEFriend) adapter.getItem(menuInfo.position);
        // And we remove it from the list, after, refresh the adapter.
        _dataAccess.removeByID(itemFromList.getM_id());
        adapter = new FriendsAdaptor(this, _dataAccess.getFriendsList());
        list.setAdapter(adapter);
        Log.d(TAG, "View has been refreshed");
    }
}
