package com.example.friendscompolsury;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.friendscompolsury.Model.BEFriend;

import dk.easv.friendsv2.R;

public class AddContactActivity extends Activity {

    private String TAG = MainActivity.TAG;
    private EditText etSaveName, etSaveEmail, etSaveAddress, etSavePhone, etSaveBirthday, etSaveURL;
    private Button saveContactButton;
    private DataAccessFactory _dataAccess = MainActivity._dataAccess;
    private ImageView _pictureView;

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

    private void locateItems() {
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
        Log.d(TAG, "Saving a new contact into the database");
        //String m_name, String m_address, String m_phone, String m_email,
        //String m_webSite, String m_birthday, double m_location_x,
        //double m_location_y, int m_img
        _dataAccess.addContact(new BEFriend(etSaveName.getText().toString(), etSaveAddress.getText().toString(),
                etSavePhone.getText().toString(), etSaveEmail.getText().toString(), etSaveURL.getText().toString(),
                etSaveBirthday.getText().toString(), 0, 0, _pictureView.getId()));
        finish();
    }

}
