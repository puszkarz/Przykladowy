package com.example.bartek.przykladowy;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

//import com.example.bartek.przykladowy.FeedReaderContract.FeedEntry;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addContact(new Contact("Ravi", "9100000000"));
        db.addContact(new Contact("Srinivas", "9199999999"));
        db.addContact(new Contact("Tommy", "9522222222"));
        db.addContact(new Contact("Karthik", "9533333333"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

    }

    public void onClick_showDonations(View v) {
        setContentView(R.layout.activity_donations_list);
    }

    public void onClick_backToMain(View v) { setContentView(R.layout.activity_main); }

    public void onClick_showAnythingDB(View v) {
        DatabaseHandler db = new DatabaseHandler(this);
//        SQLiteDatabase db = dbHandler.getWritableDatabase();
        TextView tv1 = (TextView) findViewById( R.id.textField );
        CharSequence nazwa = tv1.getText();

        TextView tv2 = (TextView) findViewById( R.id.numericField );
        CharSequence liczba = tv2.getText();

        db.addContact(new Contact(nazwa.toString(), liczba.toString()));

        tv1.setText("Udalo sie 1");
        tv2.setText("Udalo sie 2");

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

    }

//    public void onClick_showAnythingDB(View v) {
//        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
//        // Gets the data repository in write mode
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//
//        // Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//
//        String title = "Tytul";
//        String subtitle = "Podtytul";
//
//        values.put(FeedEntry.COLUMN_NAME_TITLE, title);
//        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);
//
//        // Insert the new row, returning the primary key value of the new row
//        long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
//
//        TextView tv = (TextView) findViewById( R.id.textOutDB );
//
//        tv.setText("Udalo sie");
//
//    }

}
