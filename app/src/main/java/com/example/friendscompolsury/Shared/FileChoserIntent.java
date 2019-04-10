package com.example.friendscompolsury.Shared;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.friendscompolsury.AddContactActivity;
import com.example.friendscompolsury.DataAccessFactory;
import com.example.friendscompolsury.DetailActivity;
import com.example.friendscompolsury.MainActivity;
import com.example.friendscompolsury.Model.BEFriend;

import dk.easv.friendsv2.R;

public class FileChoserIntent extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    String TAG = MainActivity.TAG;
    String messageToCamara ;
    String DetailActivity = "detailactivity";
    String addContactName = "addcontactactivity";
    String BEFriendKey = "selectedFriend";
    String messageToDetail ="friend";
    String filePath;
    DataAccessFactory _dataAccess = MainActivity._dataAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageToCamara = getString(R.string.activityClass);
        performFileSearch();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Request: " + RESULT_OK);
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().

            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                filePath = mf_szGetRealPathFromURI(this,uri);
                Log.i(TAG, "Uri: " + filePath);
                // showImage(uri);

            }
            changeActivity();
            }
            else
        {
            finish();
        }

        }
public void changeActivity()
{
    Intent intent = getIntent();
    String activity = intent.getStringExtra(messageToCamara);
    Log.d(TAG, "onActivityResult:1 "+ activity.toLowerCase());
    if(activity.toLowerCase().equals(addContactName)) {
        Log.d(TAG, " go to: " + addContactName);
        Intent camaraintent = new Intent(this, AddContactActivity.class);
        camaraintent.putExtra(messageToCamara,filePath);
        startActivity(camaraintent);
    }
    else  if(activity.toLowerCase().equals(DetailActivity)) {
        Log.d(TAG, " go to: " + DetailActivity);
        Intent camaraintent = new Intent(this, DetailActivity.class);

        BEFriend currentFriendId= _dataAccess.getFriendByID(getIntent().getLongExtra(BEFriendKey,0));

        camaraintent.putExtra(messageToDetail,currentFriendId.m_id);
        camaraintent.putExtra(messageToCamara,filePath);

        startActivity(camaraintent);
    }
}
    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startActivityForResult(i, 42);
    }
    /*
    get the android/emolated/0 path from media.
     */
    public String mf_szGetRealPathFromURI(final Context context, final Uri ac_Uri )
    {
        String result = "";
        boolean isok = false;

        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(ac_Uri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            isok = true;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return isok ? result : "";
    }
}
