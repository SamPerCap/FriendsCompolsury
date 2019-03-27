package dk.easv.friendsv2.Adaptor;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dk.easv.friendsv2.Model.BEFriend;
import dk.easv.friendsv2.Model.Friends;
import dk.easv.friendsv2.R;

public class FreindsAdaptor extends ArrayAdapter<BEFriend> {

    private final Activity context;
    private final ArrayList<BEFriend> friends;

    public FreindsAdaptor(Activity context, ArrayList<BEFriend> friends, String[] friendsName) {
        super(context, R.layout.simple_list_item_with_image,friends );
        // TODO Auto-generated constructor stub
        Log.d("hey", "hello: ");

        this.context=context;
        this.friends=friends;
    }

    public View getView(int position, View view, ViewGroup parent) {
        Log.d("hey", "getView: ");
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.simple_list_item_with_image, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);

        txtTitle.setText(friends.get(position).getName());
        imageView.setImageResource(friends.get(position).getPicture());
        return rowView;

    };
}
