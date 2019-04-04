package com.example.friendscompolsury;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.friendscompolsury.Model.BEFriend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.easv.friendsv2.R;

public class AddContactActivity extends Activity {

    private String TAG = MainActivity.TAG;
    private EditText etSaveName, etSaveEmail, etSaveAddress, etSavePhone, etSaveBirthday, etSaveURL;
    private Button saveContactButton;
    private DataAccessFactory _dataAccess;
    private ImageView _pictureView;
    private IDataCRUD _dataCRUD;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        Log.d(TAG, "Add contact Activity started");
        locateItems();
        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContactInDatabase();
                setContentView(R.layout.activity_detail);
            }
        });
        _dataCRUD = _dataAccess.getInstance();
    }

    private void locateItems(){
        saveContactButton = findViewById(R.id.SaveContactButton);
        etSaveName = findViewById(R.id.etSName);
        etSaveEmail = findViewById(R.id.etSEmail);
        etSaveURL = findViewById(R.id.etSURL);
        etSaveAddress = findViewById(R.id.etSAddress);
        etSavePhone = findViewById(R.id.etSPhone);
        etSaveBirthday = findViewById(R.id.etSBirthday);
        _pictureView = findViewById(R.id.pictureView);
    }

    private void saveContactInDatabase() {
        Log.d(TAG,"Saving a new contact into the database");
        //String m_name, String m_address, String m_phone, String m_email,
        //String m_webSite, String m_birthday, double m_location_x,
        //double m_location_y, int m_img
        _dataCRUD.addPerson(new BEFriend(etSaveName.getText().toString(),etSaveAddress.getText().toString(),
                etSavePhone.getText().toString(), etSaveEmail.getText().toString(), etSaveURL.getText().toString(),
                etSaveBirthday.getText().toString(), 0,0, _pictureView.getImageAlpha()));
        Log.d(TAG,"New person added");
        startActivity(new Intent(AddContactActivity.this, MainActivity.class));
    }

}
