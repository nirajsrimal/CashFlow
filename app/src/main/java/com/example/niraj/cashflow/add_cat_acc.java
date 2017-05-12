package com.example.niraj.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class add_cat_acc extends Activity {
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    String s,user;
    EditText item;
    ListView listView;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cat_acc);
        Intent i = getIntent();
        s = i.getStringExtra("type");
        txt = (TextView)findViewById(R.id.txt);
        txt.setText(s.toString());
        DBHelper dbHelper = new DBHelper(this);
        listView = (ListView)findViewById(R.id.listview);
        db = dbHelper.getWritableDatabase();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        user = sharedPreferences.getString("username", "");
        Cursor c = db.rawQuery("select * from "+s+" where username='admin'", null);
        c.moveToFirst();
        ArrayList arrayList = new ArrayList();
        while (c.isAfterLast() == false) {
            arrayList.add(c.getString(1));
            c.moveToNext();

        }
        c = db.rawQuery("select * from "+s+" where username='" + user + "'", null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            arrayList.add(c.getString(1));
            c.moveToNext();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item, arrayList);
        listView.setAdapter(arrayAdapter);
    }
    public void adddata(View view)
    {
        item = (EditText)findViewById(R.id.item);
        String s1 = item.getText().toString();
        try {
            db.execSQL("insert into " + s + " values('" + user + "','" + s1 + "')");
            Cursor c = db.rawQuery("select * from " + s + " where username='admin'", null);
            c.moveToFirst();
            ArrayList arrayList = new ArrayList();
            while (c.isAfterLast() == false) {
                arrayList.add(c.getString(1));
                c.moveToNext();

            }
            c = db.rawQuery("select * from " + s + " where username='" + user + "'", null);
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                arrayList.add(c.getString(1));
                c.moveToNext();
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item, arrayList);
            listView.setAdapter(arrayAdapter);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Already Exists",Toast.LENGTH_SHORT).show();
        }
    }
}