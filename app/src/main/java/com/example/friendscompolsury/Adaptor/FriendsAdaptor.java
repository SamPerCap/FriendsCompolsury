package com.example.friendscompolsury.Adaptor;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.friendscompolsury.DataAccessFactory;
import com.example.friendscompolsury.MainActivity;
import com.example.friendscompolsury.Model.BEFriend;

import java.util.ArrayList;

import dk.easv.friendsv2.R;

public class FriendsAdaptor extends ArrayAdapter<BEFriend> {

    private final Activity context;
    private String TAG = MainActivity.TAG;
    DataAccessFactory _dataAccess = MainActivity._dataAccess;

    public FriendsAdaptor(Activity context) {
        super(context, R.layout.simple_list_item_with_image);
        // TODO Auto-generated constructor stub
        Log.d(TAG, "FriendsAdaptor invoked");
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        Log.d(TAG, "Getting the view from Friends Adapter ");
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.simple_list_item_with_image, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);

        for (BEFriend person : _dataAccess.getFriendsList()) {
            txtTitle.setText(person.getM_name());
            //imageView.setImageResource(person.getM_img());
        }
        return rowView;

    }

}
