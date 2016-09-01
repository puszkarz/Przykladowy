package com.example.bartek.przykladowy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick_showDonations(View v) {
        setContentView(R.layout.activity_donations_list);
    }


    public void onClick_showAnythingDB(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.namefield );
        CharSequence nick = tv1.getText();

        TextView tv2 = (TextView) findViewById( R.id.typefield );
        CharSequence type = tv2.getText();

        db.createUser(new User(nick.toString(), type.toString()));

        tv1.setText("Udalo sie 1");
        tv2.setText("Udalo sie 2");

        Log.d("Reading: ", "Reading all users..");
        List<User> users = db.getAllUsers();

        for (User cn : users) {
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_nick();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

    }
}
