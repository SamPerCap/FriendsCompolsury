package dk.easv.friendsv2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import dk.easv.friendsv2.Adaptor.FreindsAdaptor;
import dk.easv.friendsv2.Model.BEFriend;
import dk.easv.friendsv2.Model.Friends;

public class MainActivity extends Activity {

    public static String TAG = "Friend2";
    ListView list;
    Friends m_friends;
    Context context = this;
    ArrayList<BEFriend> friends;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);
        m_friends = new Friends();



        friends = m_friends.getAll();
        FreindsAdaptor adapter=new FreindsAdaptor(this, m_friends.getAll(),m_friends.getNames() );
        list=(ListView)findViewById(R.id.listview);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
               //BEFriend Slecteditem= m_f;
                Intent x = new Intent(context,DetailActivity.class);
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

    private void addData(Intent x, BEFriend f)
    {
        Log.d(TAG, "addData: " + f.toString());
        x.putExtra("friend", f);
    }



}
