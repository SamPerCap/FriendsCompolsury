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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.friendscompolsury.Model.BEFriend;
import com.example.friendscompolsury.Shared.CamaraIntent;
import com.example.friendscompolsury.Shared.FileChoserIntent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.easv.friendsv2.R;

public class DetailActivity extends FragmentActivity {
    String friendIdKey;
    String className  ="DetailActivity";
    String messageToCamara ;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String TAG = MainActivity.TAG;
    DataAccessFactory _dataAccess = MainActivity._dataAccess;
    EditText etName, etPhone, etEmail, etAddress, etURL, etBirthday;
    ImageView mImageView;
    GoogleMap m_map;
    Button updateBtn;
    BEFriend currentFriend;
    Context context;
    String filePath;
    String BEFriendKey = "selectedFriend";

    private Bitmap mImageBitmap;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageToCamara = getString(R.string.activityClass);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "Detail Activity started");
        LocateItems();
        setGUI();
        Log.d(TAG,"current friend" + currentFriend);
        context= this;
    }

    /* When update button is clicked it will save all the changes to the
        DataAccessFactory class
     */

    private void updateView()
    {
        currentFriend.setM_address(etAddress.getText().toString());
        currentFriend.setM_birthday(etBirthday.getText().toString());
        currentFriend.setM_email(etEmail.getText().toString());
        currentFriend.setM_phone(etPhone.getText().toString());
        currentFriend.setM_webSite(etURL.getText().toString());
        currentFriend.setM_name(etName.getText().toString());
        // Goes to DataAccessFactory and in to a method that
        // updates the contact by getting the changes that have been made to each textfield
        _dataAccess.updateContact(currentFriend);
        // When done, go to main activity, user should see the changes
        startActivity(new Intent(DetailActivity.this, MainActivity.class));
    }

    private void showFileChooser()
    {
        // Open the file chooser where user can find an image
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startActivityForResult(i, 42);
    }


    private void LocateItems() {
        Log.d(TAG, "Locating items");
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etURL = findViewById(R.id.etURL);
        mImageView = findViewById(R.id.pictureView);
        etBirthday = findViewById(R.id.etBirthday);
        updateBtn = findViewById(R.id.btnUPDATE);
    }

    private void getCurrentFriendImage() {
        try
        {
            Intent intent = getIntent();
           filePath = intent.getStringExtra(messageToCamara);
            if(filePath == null ) {
                Bitmap bit = BitmapFactory.decodeFile(currentFriend.getM_img());
                if (bit != null) {
                    mImageView.setImageBitmap(bit);
                } else {
                    Log.d(TAG, "Bitmap: is 0 ");
                    mImageView.setImageResource(R.drawable.cake);
                }
            }
            else
            {
                currentFriend.setM_img(filePath);
                Log.d(TAG, "camara changes the image: " + filePath);
                Bitmap bit = BitmapFactory.decodeFile(filePath);
                if (bit != null) {
                    mImageView.setImageBitmap(bit);
                }
            }
        }
        catch(Exception ex)
        {
            Log.d(TAG, "file can convertes: ");
            mImageView.setImageResource(R.drawable.cake);
        }}



    private void setGUI()
    {
        if (_dataAccess.getFriendsList().size() <= 0) {
            Log.d(TAG, "The database is empty");
        } else {
            friendIdKey = getString(R.string.friendKey);
            currentFriend= _dataAccess.getFriendByID(getIntent().getLongExtra(friendIdKey,0));
                Log.d(TAG, "setGUI: " + currentFriend.toString());
                getCurrentFriendImage();
                etName.setText(currentFriend.getM_name());
                etEmail.setText(currentFriend.getM_email());
                etPhone.setText(currentFriend.getM_phone());
                etAddress.setText(currentFriend.getM_address());
                etBirthday.setText(currentFriend.getM_birthday());
                etURL.setText(currentFriend.getM_webSite());
            }
    }



    private void sendSMS() {
        Log.d(TAG, "sendSMS method invoked");
        Toast.makeText(this, "An sms will be send", Toast.LENGTH_LONG)
                .show();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                return;

            } else
                Log.d(TAG, "permission to SEND_SMS granted!");

        }

        SmsManager m = SmsManager.getDefault();
        String text = "Hi, it goes well on the android course...";
        m.sendTextMessage(etPhone.getText().toString(), null, text, null, null);
    }

    public void smsButton(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("SMS Handling");

        alertDialogBuilder
                .setMessage("Click Direct if SMS should be send directly. Click Start to start SMS app...")
                .setCancelable(true)
                .setPositiveButton("Direct", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DetailActivity.this.sendSMS();
                    }
                })
                .setNegativeButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DetailActivity.this.startSMSActivity();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void startSMSActivity() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + etPhone.getText()));
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...");
        startActivity(sendIntent);
    }

    public void mailButton(View view) {
        Log.e(TAG, "Email: " + etEmail.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] receivers = {etEmail.getText().toString()};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "This is a test mail");
        startActivity(emailIntent);
    }



    public void callButton(View view) {

        Toast.makeText(this, "A CALL will be made", Toast.LENGTH_LONG)
                .show();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to CALL - requesting it");
                String[] permissions = {android.Manifest.permission.CALL_PHONE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                return;

            } else {
                makeCall();
                Log.d(TAG, "permission to CALL granted!");
            }
        }
        makeCall();
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + etPhone.getText().toString()));
        Log.e(TAG, "Calling permission: " + intent);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        Log.d(TAG, "Permission: " + permissions[0] + " - grantResult: " + grantResults[0]);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            SmsManager m = SmsManager.getDefault();
            m_map.setMyLocationEnabled(true);
            String text = "Hi, it goes well on the android course...";
            m.sendTextMessage(etPhone.getText().toString(), null, text, null, null);
        }


    }

    public void currentLocation()
    {
        try
        {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                Criteria criteria = new Criteria();

                String provider = locationManager.getBestProvider(criteria,false);
                Location location = locationManager.getLastKnownLocation(provider);
                if ( location == null ) {

                    location = locationManager.getLastKnownLocation(locationManager.getAllProviders().get(0));
                }

                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                currentFriend.setM_location(location.getLatitude(), location.getLongitude());

                Toast.makeText(this, "Location saved: " + userLocation, Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex)
        {
            Log.d(TAG, "Exception: " +ex);
        }
    }
    private void addData(Intent x, BEFriend f) {
        x.putExtra("friend", f);
    }

    public void goToCamera(View view)
    {
        Log.e(TAG, "What happens?");
        final String[] options = {"Select image", "Take new image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(options, new DialogInterface.OnClickListener()
        {
            /*
            * Give the user an option to either choose an image that already exists on the phone
            * or to take a picture
            * */
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(options[which].equals(options[0]))
                {
                    Intent  fileintent = new Intent(context, FileChoserIntent.class);
                    fileintent.putExtra(messageToCamara,className);
                    fileintent.putExtra(BEFriendKey,currentFriend.m_id);
                    startActivity(fileintent);
                }
                if(options[which].equals(options[1]))
                {
                    Intent  camaraintent = new Intent(context, CamaraIntent.class);
                    camaraintent.putExtra(messageToCamara,className);
                    camaraintent.putExtra(BEFriendKey,currentFriend.m_id);
                    startActivity(camaraintent);
                }
            }
        });
        builder.show();
    }

    public void showMap(View view) {
       try {
           Intent x = new Intent(this, MapActivity.class);
           addData(x, currentFriend);
           Log.d(TAG, "Detail activity will be started");
           startActivity(x);
           Log.d(TAG, "Detail activity is started");
       }
       catch (Exception ex)
       {
           Log.d(TAG, "showMap: "+ ex);
       }
    }

    public void saveLocation(View view) {
        currentLocation();
    }

    public void openWebURL( String inURL ) {

        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );
        if ( !inURL.startsWith("https://") ) {
            Toast.makeText(this, "Must type https://", Toast.LENGTH_LONG)
                    .show();
        }
        else
            startActivity( browse );
    }

    public void goToURL(View view) {
        openWebURL(currentFriend.getM_webSite().toLowerCase());
    }

    public void updateCurrentContact(View view) {
        updateView();
    }

}
