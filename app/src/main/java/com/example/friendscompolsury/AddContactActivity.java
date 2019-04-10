package com.example.friendscompolsury;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.friendscompolsury.Model.BEFriend;
import com.example.friendscompolsury.Shared.CamaraIntent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.easv.friendsv2.R;

public class AddContactActivity extends Activity {
    String className  ="AddContactActivity";
     String messageToCamara = "activityClass";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String TAG = MainActivity.TAG;
    private EditText etSaveName, etSaveEmail, etSaveAddress, etSavePhone, etSaveBirthday, etSaveURL;
    private Button saveContactButton;
    private DataAccessFactory _dataAccess = MainActivity._dataAccess;
    private ImageView _pictureView;
    Bitmap mImageBitmap;
    String filePath;
    Context context;
    Intent camaraintent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        Log.d(TAG, "Add contact Activity started");
        locateItems();

        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContactInDatabase();
            }
        });
    }

    private void locateItems()
    {
        context = this;
        saveContactButton = findViewById(R.id.SaveContactButton);
        etSaveName = findViewById(R.id.etSName);
        etSaveEmail = findViewById(R.id.etSEmail);
        etSaveURL = findViewById(R.id.etSURL);
        etSaveAddress = findViewById(R.id.etSAddress);
        etSavePhone = findViewById(R.id.etSPhone);
        etSaveBirthday = findViewById(R.id.etSBirthday);
        _pictureView = findViewById(R.id.pictureView);

            try {
                Intent intent = getIntent();
                filePath = intent.getStringExtra(messageToCamara);
                if (filePath != null) {
                    Bitmap bit = BitmapFactory.decodeFile(filePath);
                    if (bit != null) {
                        _pictureView.setImageBitmap(bit);
                    }


                }
            } catch (Exception ex) {
                Log.d(TAG, "locateItems: images " + ex);
            }


    }

    private void saveContactInDatabase() {

        if(!etSaveAddress.getText().equals(null)
        && !etSaveName.getText().equals(null)
        && !etSaveBirthday.getText().equals(null)
        && !etSaveEmail.getText().equals(null)
        && !etSavePhone.getText().equals(null)
        && !etSaveURL.getText().equals(null)) {
            Log.d(TAG, "Saving a new contact into the database");
            //String m_name, String m_address, String m_phone, String m_email,
            //String m_webSite, String m_birthday, double m_location_x,
            //double m_location_y, int m_img
            _dataAccess.addContact(new BEFriend(etSaveName.getText().toString(), etSaveAddress.getText().toString(),
                    etSavePhone.getText().toString(), etSaveEmail.getText().toString(), etSaveURL.getText().toString(),
                    etSaveBirthday.getText().toString(), 0, 0, filePath));
            Log.d(TAG, "New person added");
        }
        else
        {
            Toast.makeText(this, "Fill out every fields", Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(AddContactActivity.this, MainActivity.class));
    }
    public void goToCamera(View view) {
        Log.e(TAG, "What happens?");
        final String[] options = {"Select image", "Take new image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which].equals(options[0]))
                {
                    performFileSearch();
                }
                if(options[which].equals(options[1]))
                {


                    camaraintent = new Intent(context, CamaraIntent.class);
                    camaraintent.putExtra(messageToCamara,className);
                    startActivity(camaraintent);
                }
            }
        });
        builder.show();


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
                _pictureView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                Log.i(TAG, "Uri: " + filePath);
                // showImage(uri);
            }
        }
    }


    private static final int READ_REQUEST_CODE = 42;
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
