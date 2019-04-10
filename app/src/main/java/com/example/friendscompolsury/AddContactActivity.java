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
import com.example.friendscompolsury.Shared.FileChoserIntent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.easv.friendsv2.R;

public class AddContactActivity extends Activity {
    String className  ="AddContactActivity";
     String messageToCamara;
    private String TAG = MainActivity.TAG;
    private EditText etSaveName, etSaveEmail, etSaveAddress, etSavePhone, etSaveBirthday, etSaveURL;
    private Button saveContactButton;
    private DataAccessFactory _dataAccess = MainActivity._dataAccess;
    private ImageView _pictureView;
    String filePath;
    Context context;
    Intent ImageIntent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageToCamara = getString(R.string.activityClass);
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

        if(!etSaveName.getText().toString().isEmpty()
                && !etSavePhone.getText().toString().isEmpty())
        {
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
            Toast.makeText(this, "Needs to fill out name and number at least before saving.", Toast.LENGTH_SHORT).show();
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
                    ImageIntent = new Intent(context, FileChoserIntent.class);
                    ImageIntent.putExtra(messageToCamara,className);
                    startActivity(ImageIntent);
                }
                if(options[which].equals(options[1]))
                {


                    ImageIntent = new Intent(context, CamaraIntent.class);
                    ImageIntent.putExtra(messageToCamara,className);
                    startActivity(ImageIntent);
                }
            }
        });
        builder.show();


    }

    @Override
    public void onBackPressed()
    {
        Log.e(TAG, "What is the state of the current textfields: "
                + etSaveAddress.getText()
                + etSaveName.getText()
                + etSaveURL.getText()
                + etSaveBirthday.getText()
                + etSaveEmail.getText()
                + etSavePhone.getText());

        if(!etSaveAddress.getText().toString().isEmpty()
                || !etSaveName.getText().toString().isEmpty()
                || !etSaveBirthday.getText().toString().isEmpty()
                || !etSaveEmail.getText().toString().isEmpty()
                || !etSavePhone.getText().toString().isEmpty()
                || !etSaveURL.getText().toString().isEmpty())
        {
            // Here you want to show the user a dialog box
            new AlertDialog.Builder(context)
                    .setTitle("Going back to the main screen without saving the changes")
                    .setMessage("Are you sure?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // The user wants to leave - so dismiss the dialog and exit
                            finish();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // The user is not sure, so you can exit or just stay
                    dialog.dismiss();
                }
            }).show();
        }
        else
            finish();
    }
}
