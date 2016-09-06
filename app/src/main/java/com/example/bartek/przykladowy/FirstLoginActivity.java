package com.example.bartek.przykladowy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.User;

/**
 * Created by magda on 03.09.16.
 */
public class FirstLoginActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private String _bloodType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_donations:
                Intent donationsActivity = new Intent(getApplicationContext(), DonationsListActivity.class);
                startActivity(donationsActivity);
                return true;
            case R.id.show_stations:
                Intent stationsActivity = new Intent(getApplicationContext(), StationsListActivity.class);
                startActivity(stationsActivity);
                return true;
            case R.id.manage_donations:
                Intent manageDonationsActivity = new Intent(getApplicationContext(), ManageDonationsActivity.class);
                startActivity(manageDonationsActivity);
                return true;
            case R.id.manage_stations:
                setContentView(R.layout.activity_manage_stations);
                return true;
            case R.id.manage_users:
                setContentView(R.layout.activity_manage_users);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick_showPopUpBloodTypes(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(FirstLoginActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bloodtype_menu, popup.getMenu());
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.APlus:
                Toast.makeText(getBaseContext(), "You selected A+", Toast.LENGTH_SHORT).show();
                _bloodType = "A+";
                return true;
            case R.id.AMinus:
                Toast.makeText(getBaseContext(), "You selected A-", Toast.LENGTH_SHORT).show();
                _bloodType = "A-";
                return true;
            case R.id.BPlus:
                Toast.makeText(getBaseContext(), "You selected B+", Toast.LENGTH_SHORT).show();
                _bloodType = "B+";
                return true;
            case R.id.BMinus:
                Toast.makeText(getBaseContext(), "You selected B-", Toast.LENGTH_SHORT).show();
                _bloodType = "B-";
                return true;
            case R.id.ABPlus:
                Toast.makeText(getBaseContext(), "You selected AB+", Toast.LENGTH_SHORT).show();
                _bloodType = "AB+";
                return true;
            case R.id.ABMinus:
                Toast.makeText(getBaseContext(), "You selected AB-", Toast.LENGTH_SHORT).show();
                _bloodType = "AB-";
                return true;
            case R.id.ZeroPlus:
                Toast.makeText(getBaseContext(), "You selected 0+", Toast.LENGTH_SHORT).show();
                _bloodType = "0+";
                return true;
            case R.id.ZeroMinus:
                Toast.makeText(getBaseContext(), "You selected 0-", Toast.LENGTH_SHORT).show();
                _bloodType = "0-";
                return true;
            default:
                return false;
        }
    }

    public void onClick_addUser(View v){
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.FLL_f_addNick);

        if (tv1 != null && !_bloodType.equalsIgnoreCase("")) {
            CharSequence nick = tv1.getText();
            db.insertUser(new User(nick.toString(), _bloodType));
            _bloodType = "";
            tv1.setText(R.string.debugOK);

        }

        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish(); //@TODO - czy ta aktywność się zamyka?

    }
}
