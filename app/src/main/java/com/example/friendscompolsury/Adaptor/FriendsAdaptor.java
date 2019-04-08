package com.example.friendscompolsury.Adaptor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.util.ArrayList;

import dk.easv.friendsv2.R;

public class FriendsAdaptor extends ArrayAdapter<BEFriend> {

    private final Activity context;
    private String TAG = MainActivity.TAG;
    DataAccessFactory _dataAccess = MainActivity._dataAccess;
    ArrayList<BEFriend> _arrayData;

    public FriendsAdaptor(Activity context, ArrayList<BEFriend> arrayData)
    {
        super(context, R.layout.simple_list_item_with_image);
        // TODO Auto-generated constructor stub
        Log.d(TAG, "FriendsAdaptor invoked");
        this.context = context;
        this._arrayData = arrayData;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        Log.d(TAG, "Getting the view from Friends Adapter ");
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.simple_list_item_with_image, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        txtTitle.setText(_dataAccess.getFriendsList().get(position).getM_name());
        try
        {Bitmap bit = BitmapFactory.decodeFile(_dataAccess.getFriendsList().get(position).getM_img());
        if(bit != null) {
            imageView.setImageBitmap(bit);
        /* Old code for backup by the flies.
        for (BEFriend person : _dataAccess.getFriendsList()) {
            txtTitle.setText(person.getM_name());
            imageView.setImageResource(person.getM_img());*/
        }
        else
        {
            Log.d(TAG, "Bitmap: is 0 ");
            imageView.setImageResource(R.drawable.cake);
        }
        }
        catch(Exception ex)
        {
            Log.d(TAG, "file can convertes: ");
            imageView.setImageResource(R.drawable.cake);
        }

        return rowView;

    }

}
