package com.example.bartek.przykladowy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.User;
/**
 * Created by magda on 07.09.16.
 */
public class SettingsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    public void onClick_UpdateNick(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv_nick = (TextView) findViewById( R.id.MUL_updateNick);
        if (tv_nick != null) {
            CharSequence nick = tv_nick.getText();
            User user = db.getUser(1);
            user.set_nick(nick.toString());
            db.updateUser(user);

            tv_nick.setText(R.string.debugOK);
            logListUsers(db);
        }
    }

    // Writing Users to Log
    private void logListUsers(DatabaseHelper db) {
        Log.d("Reading: ", "Reading all users..");
        List<User> users = db.getAllUsers();
        for (User cn : users) {
            String log = "Id: " + cn.get_id() + " ,Nick: " + cn.get_nick() + " ,Blood type:" + cn.get_bloodType();
            Log.d("Name: ", log);
        }
    }

    public void onClick_showUpdatePopUpBloodTypes(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(SettingsActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bloodtype_menu, popup.getMenu());
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.APlus:
                Toast.makeText(getBaseContext(), "You selected A+", Toast.LENGTH_SHORT).show();
                updateBloodType("A+");
                return true;
            case R.id.AMinus:
                Toast.makeText(getBaseContext(), "You selected A-", Toast.LENGTH_SHORT).show();
                updateBloodType("A-");
                return true;
            case R.id.BPlus:
                Toast.makeText(getBaseContext(), "You selected B+", Toast.LENGTH_SHORT).show();
                updateBloodType("B+");
                return true;
            case R.id.BMinus:
                Toast.makeText(getBaseContext(), "You selected B-", Toast.LENGTH_SHORT).show();
                updateBloodType("B-");
                return true;
            case R.id.ABPlus:
                Toast.makeText(getBaseContext(), "You selected AB+", Toast.LENGTH_SHORT).show();
                updateBloodType("AB+");
                return true;
            case R.id.ABMinus:
                Toast.makeText(getBaseContext(), "You selected AB-", Toast.LENGTH_SHORT).show();
                updateBloodType("AB-");
                return true;
            case R.id.ZeroPlus:
                Toast.makeText(getBaseContext(), "You selected 0+", Toast.LENGTH_SHORT).show();
                updateBloodType("0+");
                return true;
            case R.id.ZeroMinus:
                Toast.makeText(getBaseContext(), "You selected 0-", Toast.LENGTH_SHORT).show();
                updateBloodType("0-");
                return true;
            default:
                return false;
        }
    }

    private void updateBloodType(String s) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        User user = db.getUser(1);
        user.set_bloodType(s);
        db.updateUser(user);
    }

    public void onClick_endSettingsActivity(View v) {
        finish();
    }

    public void onClick_deleteUser(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.deleteUser(1);
        logListUsers(db);
        finish();
    }

}