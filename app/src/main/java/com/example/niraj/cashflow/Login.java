package com.example.niraj.cashflow;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;



public class Login extends Activity {
    SQLiteDatabase db;
    EditText usrname,pass;
    Button signin1;
    String user,passwrd;
    Button reg;
    SharedPreferences sharedPreferences;
    ImageButton imageButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        signin1 = (Button)findViewById(R.id.signin);
        usrname = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        signin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = usrname.getText().toString();
                String p = pass.getText().toString();
                //if ((user.equals(usrname.getText().toString())) && (passwrd.equals(pass.getText().toString()))) {
                  Cursor cursor = db.rawQuery("select * from users where username =" + "\"" + n.trim() + "\"" + " and password=" + "\"" + p.trim() + "\"", null);
                if(cursor.getCount()!=0) {
                    Cursor c1 = db.rawQuery("select * from userdetails where username =" + "\"" + n.trim() +"\"", null);
                    c1.moveToFirst();
                    Intent intent = new Intent(Login.this, dashboard.class);
                    intent.putExtra("user", usrname.getText().toString());
                    intent.putExtra("from", "login");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", usrname.getText().toString());
                    editor.putString("email", c1.getString(c1.getColumnIndex("email")));
                    editor.putString("phone", c1.getString(c1.getColumnIndex("phone")));
                    editor.putString("name", c1.getString(c1.getColumnIndex("name")));
                    NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(getApplicationContext());
                    mBuilder.setSmallIcon(R.drawable.logo1);
                    mBuilder.setContentTitle("CashFlow Log In");
                    mBuilder.setContentText(usrname.getText().toString()+" has been Logged In!");
                    NotificationManager mNotificationmanager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationmanager.notify(1,mBuilder.build());
                    editor.commit();
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid id/Password",Toast.LENGTH_SHORT).show();
                }
                }
        });
        reg = (Button) findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent reg1 = new Intent(Login.this,register1.class);
                startActivity(reg1);

            }
        });


        imageButton = (ImageButton)findViewById(R.id.imageView);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent z = new Intent(Login.this,about.class);
                startActivity(z);
            }
        });
    }
}
