package com.example.niraj.cashflow;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.androidanimations.library.Techniques;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

public class add_trans extends Activity {
    TextView tv_display;
    TextView intext,extext;
    String str,prev_string="";
    private int year,month,day,hour,min;
    String yr="",mnt="",dy="",hr="",mn="";
    Button changeDate,changetime;
    int i;
    Switch aSwitch;
    String user,inex,des,date,time;
    Spinner sp,sp1;
    ToggleButton tb;
    EditText amount;
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    static final int DATE_PICKER_ID = 1111;
    static final int TIME_DIALOG_ID = 2222;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trans);

        aSwitch = (Switch) findViewById(R.id.switch1);
        changeDate = (Button) findViewById(R.id.linearDate);
        final Calendar c1 = Calendar.getInstance();
        year = c1.get(Calendar.YEAR);
        month = c1.get(Calendar.MONTH);
        day = c1.get(Calendar.DAY_OF_MONTH);
        changeDate.setText(new StringBuilder().append(day).append("-").append(month + 1)
                .append("-").append(year)
                .append(" "));
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        changetime = (Button) findViewById(R.id.linearTime);
        hour = c1.get(Calendar.HOUR_OF_DAY);
        min = c1.get(Calendar.MINUTE);
        if((hour/10)==0)
        {
            time = "0"+hour+":";
        }
        else {
            time = hour + ":";
        }
        if(min/10==0)
            time = time + "0" + min;
        else
            time = time + min;
        changetime.setText(time);
        date = day + "/" + (month + 1) + "/" + year;
        changeDate.setText(date);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });
        changetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //Toast.makeText(getApplicationContext(),"HEEEE",Toast.LENGTH_SHORT).show();
                            //intext.setTextColor(Color.RED);
                            //extext.setTextColor(Color.GRAY);
                            //aSwitch.setText("INCOME");


                        } else {
                            //Toast.makeText(getApplicationContext(),"HEEEE",Toast.LENGTH_SHORT).show();
                            //extext.setTextColor(Color.RED);
                            //intext.setTextColor(Color.GRAY);


                            //aSwitch.setTextColor(Color.BLUE);

                        }
                    }
                });
            }
        });

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        user = sharedPreferences.getString("username", "");
        sp = (Spinner) findViewById(R.id.category);
        Cursor c = db.rawQuery("select * from categories where username='admin'", null);
        c.moveToFirst();
        ArrayList arrayList = new ArrayList();
        while (c.isAfterLast() == false) {
            arrayList.add(c.getString(1));
            c.moveToNext();

        }
        c = db.rawQuery("select * from categories where username='" + user + "'", null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            arrayList.add(c.getString(1));
            c.moveToNext();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayList);
        sp.setAdapter(arrayAdapter);

        sp1 = (Spinner) findViewById(R.id.account);
        Cursor c2 = db.rawQuery("select * from accounts where username ='" + user + "'", null);
        c2.moveToFirst();
        ArrayList arrayList1 = new ArrayList();
        while (c2.isAfterLast() == false) {
            arrayList1.add(c2.getString(1));
            c2.moveToNext();
        }
        c2 = db.rawQuery("select * from accounts where username ='admin'", null);
        c2.moveToFirst();
        while (c2.isAfterLast() == false) {
            arrayList1.add(c2.getString(1));
            c2.moveToNext();
        }
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayList1);
        sp1.setAdapter(arrayAdapter1);
    }
    public void done_trans(View view)
    {
        try {
            intext = (TextView) findViewById(R.id.intext);
            extext = (TextView) findViewById(R.id.extext);
            EditText description = (EditText) findViewById(R.id.desc);
            des = description.getText().toString();
            if (des != null) {
                if (!aSwitch.isChecked()) {
                    inex = "income";
                } else {
                    inex = "expense";
                }
                String cat = sp.getSelectedItem().toString();
                if (cat == null) {
                    cat = "food";
                }
                String ac = sp1.getSelectedItem().toString();
                if (ac == null) {
                    sp1.getItemAtPosition(0);
                }
                amount = (EditText) findViewById(R.id.amt);
                double amt = Double.parseDouble(amount.getText().toString());
                db.execSQL("insert into transactions values('" + user + "','" + des + "','" + ac + "','" + inex + "','" + cat + "'," + amt + ",'" + date + "','" + time + "')");
                Toast.makeText(getApplicationContext(), "Added.", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Enter description", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Some fields may have been left blank",Toast.LENGTH_SHORT).show();
        }
    }
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, min,
                        false);

        }

        return null;
    }
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            min = minutes;
            if((hour/10)==0)
            {
                time = "0"+hour+":";
            }
            else {
                time = hour + ":";
            }
            if(min/10==0)
                time = time + "0" + min;
            else
                time = time + min;
            changetime.setText(time);
        }

    };
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            date = day+"/"+(month + 1)+"/"+year;
            // Show selected date
            changeDate.setText(new StringBuilder().append(day).append("-").append(month + 1)
                    .append("-").append(year)
                    .append(" "));

        }
    };
}