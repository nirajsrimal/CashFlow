package com.example.niraj.cashflow;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Niraj on 17-10-2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String database_name = "cashflowdb";

    public DBHelper(Context context) {
        super(context, database_name, null, 1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(username varchar(20) primary key, password varchar(20))");
        db.execSQL("CREATE TABLE userdetails(username varchar(20) PRIMARY KEY,name varchar(20), phone varchar(10),email varchar(30))");
        db.execSQL("CREATE TABLE categories(username varchar(20), category varchar(20) PRIMARY KEY)");
        db.execSQL("CREATE TABLE accounts(username varchar(20),account_name varchar(20) PRIMARY KEY)");
        db.execSQL("insert into accounts values('admin','Account1')");
        db.execSQL("CREATE TABLE transactions(username varchar(20),description varchar(20), account varchar(20),type varchar(8),category varchar(20),amount integer,lineardate varchar(20),lineartime varchar(20))");

        db.execSQL("insert into users values('admin','admin')");
        db.execSQL("insert into categories values('admin','food')");
        db.execSQL("insert into categories values('admin','clothing')");
        db.execSQL("insert into categories values('admin','grocery')");
        db.execSQL("insert into categories values('admin','travel')");
        db.execSQL("insert into categories values('admin','salary')");
        db.execSQL("insert into categories values('admin','personal')");
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
    /*
    public int Login(SQLiteDatabase db, String username, String password) {
        try {
            int i = 0;
            Cursor c = null;
            c = db.rawQuery("select * from db where username =" + "\"" + username.trim() + "\"" + " and password=" + "\"" + password.trim() + "\"", null);
            c.moveToFirst();
            i = c.getCount();
            c.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    */
}