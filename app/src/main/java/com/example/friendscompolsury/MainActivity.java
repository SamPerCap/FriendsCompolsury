package com.example.friendscompolsury;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "Developer";
    Button button;
    TextView txV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = this.findViewById(R.id.btnGetPerson);
        txV = this.findViewById(R.id.textView);
    }
    void getPersons(View view){
        SQLiteImplementation sql = new SQLiteImplementation(this);

        txV.setText(sql.getPersonById(0).m_name);
    }

    public void addPerson(View view) {
        SQLiteImplementation sql = new SQLiteImplementation(this);
        BEPerson person = new BEPerson(1,"Sam", "213", "sdae", ((float) (0120)),
                "deam.com", "www.com", "34/2", 0);
        sql.addPerson(person);
    }
}
