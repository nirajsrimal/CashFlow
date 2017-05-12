package com.example.niraj.cashflow;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


import com.example.niraj.cashflow.R;

import org.w3c.dom.Text;

public class profile extends Activity {

    TextView name,user,email,phone;
    String u,n,p,e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView)findViewById(R.id.name);
        user = (TextView)findViewById(R.id.user);
        email = (TextView)findViewById(R.id.email);
        phone = (TextView)findViewById(R.id.phone);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        user.setText(sharedPreferences.getString("username",""));
        name.setText(sharedPreferences.getString("name",""));
        email.setText(sharedPreferences.getString("email",""));
        phone.setText(sharedPreferences.getString("phone",""));
    }
}
