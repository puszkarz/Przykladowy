package com.example.bartek.przykladowy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DonationsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_list);
    }

    // To activity nie działa, zostało stworzone jako współgrające z odpowiednim layoutem,
    // lecz okazało się, że łatwiej tamten layout użyć z Main activity.
    // Stąd też klasa ta jest nieuzywana.

}
