package com.example.bartek.przykladowy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DonationsListActivity extends AppCompatActivity {


    //To activity nie działa, wykorzystywany jest tylko layout, a ta klasa jest nieuzywana.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_list);
    }

    public void onClick_backToMain(View v) {
        setContentView(R.layout.activity_main);
    }
}
