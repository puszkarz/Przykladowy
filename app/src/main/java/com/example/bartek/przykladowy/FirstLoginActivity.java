package com.example.bartek.przykladowy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
    }

    public void onClick_showPopUpBloodTypes(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(FirstLoginActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_actions, popup.getMenu());
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ap:
                Toast.makeText(getBaseContext(), "You selected A+", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.am:
                Toast.makeText(getBaseContext(), "You selected A-", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ab:
                Toast.makeText(getBaseContext(), "You selected AB", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    public void onClick_addUser(View v){
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.MSL_f_addStationName);
        TextView tv2 = (TextView) findViewById( R.id.typefield );
        if (tv1 != null && tv2 != null) {
            //CharSequence nick = tv1.getText();
            //CharSequence type = tv2.getText();
            //db.insertUser(new User(nick.toString(), Integer.parseInt(type.toString())));

            tv1.setText(R.string.debugOK);
            tv2.setText(R.string.debugOK);

        }
    }
}
