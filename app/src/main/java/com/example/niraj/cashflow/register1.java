package com.example.niraj.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class register1 extends Activity {
    SQLiteDatabase db;
    EditText name,username,phone,email,password;
    TextView error;
    Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);
        reg = (Button)findViewById(R.id.register);
        error = (TextView)findViewById(R.id.errormsg);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n,p,u,pass,e;
                n = name.getText().toString();
                p = phone.getText().toString();
                u = username.getText().toString();
                pass = password.getText().toString();
                e = email.getText().toString();
                if((n.equals(""))||(p.equals(""))||(u.equals(""))||(pass.equals(""))||(e.equals("")))
                {
                    //error.setText("*some fields may have been left blank");
                    //error.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Some fields may have been left blank",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try
                    {
                        Cursor c1 = db.rawQuery("select * from users where username='"+u+"'",null);
                        if(c1.getCount()==0) {
                            Cursor c2 = db.rawQuery("select * from userdetails where phone='"+p+"'",null);
                            if(c2.getCount()==0) {
                                Cursor c3 =  db.rawQuery("select * from userdetails where email='"+e+"'",null);
                                if(c3.getCount()==0)
                                {
                                    if(pass.length()>=5) {
                                        db.execSQL("insert into users values('"+ u + "','" + pass + "')");
                                        db.execSQL("insert into userdetails values('" + u + "','" + n + "','" + p + "','" + e + "')");
                                        Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(register1.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Password too short", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Email already registered", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Phone number already registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception exc)
                    {
                        Toast.makeText(getApplicationContext(), "Some Error Occured!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
