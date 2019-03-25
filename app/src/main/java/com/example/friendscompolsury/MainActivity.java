package com.example.friendscompolsury;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "Developer";
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = this.findViewById(R.id.btnGetPerson);
    }
    void getPersons(){
        SQLiteImplementation sql = new SQLiteImplementation(this);

        button.setText(sql.getPersonById(0).m_name);
    }
}
