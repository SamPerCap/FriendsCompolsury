package com.example.friendscompolsury;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.maps.GoogleMap;

import dk.easv.friendsv2.R;

public class DetailActivity extends FragmentActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String TAG = MainActivity.TAG;
    DataAccessFactory _dataAccess;
    EditText etName, etPhone, etEmail, etAddress, etURL, etBirthday;
    ImageView mImageView;
    GoogleMap m_map;
    BEFriend f;
    Button updateBtn;
    long _contactID;
    private Bitmap mImageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "Detail Activity started");
        LocateItems();
        setGUI();
        _dataAccess = new DataAccessFactory();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dataAccess.updateContact(new BEFriend(_contactID, etName.getText().toString(), etAddress.getText().toString(),
                        etPhone.getText().toString(), etEmail.getText().toString(), etURL.getText().toString(),
                        etBirthday.getText().toString(), 0, 0, mImageView.getTransitionName()));
                finish();
            }
        });
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

    private void setGUI() {
        if (_dataAccess.getInstance().getAllPersons().size() < 0) {
            Log.d(TAG, "The database is empty");
        } else {
            for (BEFriend person : _dataAccess.getInstance().getAllPersons()) {
                Log.d(TAG, "setGUI: " + person.toString());
                etName.setText(person.getM_name());
                etEmail.setText(person.getM_email());
                etPhone.setText(person.getM_phone());
                etAddress.setText(person.getM_address());
                etBirthday.setText(person.getM_birthday());
                etURL.setText(person.getM_webSite());
                try {
                    mImageView.setImageBitmap(BitmapFactory.decodeFile(person.getM_img()));
                }
                catch (Exception ex)
                {
                    Log.d(TAG, "Can't parse this to image: " + person.getM_img());
                    Log.d(TAG, ""+ex);
                }
            }
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

    public void camButton(View view) {
        Log.e(TAG, "What happens?");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {

                    Log.d(TAG, "permission denied to CAMERA - requesting it");
                    String[] permissions = {Manifest.permission.CAMERA};

                    requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                }
            }
            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
            Bundle b = data.getExtras();

            mImageBitmap = (Bitmap) b.get("data");
            mImageView.setImageBitmap(mImageBitmap);
        }
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

    public void getLocation(View view) {
        Log.d(TAG, "Detail activity will be started");
        f = (BEFriend) getIntent().getSerializableExtra("friend");
        Intent x = new Intent(this, MapActivity.class);
        addData(x, f);
        startActivity(x);
        Log.d(TAG, "Detail activity is started");
    }

    private void addData(Intent x, BEFriend f) {
        x.putExtra("friend", f);
    }

    public void goToCamera(View view) {
        Log.e(TAG, "What happens?");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {


                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {

                    Log.d(TAG, "permission denied to CAMERA - requesting it");
                    String[] permissions = {Manifest.permission.CAMERA};

                    requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                }
            }
            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
