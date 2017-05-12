package com.example.niraj.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class report extends Activity {
    ListView listView;
    SQLiteDatabase db;
    Double sum;
    SharedPreferences sharedPreferences;
    Spinner sp1;
    String u;
    TextView inc,exp,tot;
    double netinc,netexp,nettotal;
    private static String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        listView = (ListView) findViewById(R.id.listview);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        u = sharedPreferences.getString("username", "");

        sp1 = (Spinner) findViewById(R.id.spinner);
        Cursor c2 = db.rawQuery("select * from accounts where username ='" + u + "'", null);
        c2.moveToFirst();
        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("All Accounts");
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
        account=sp1.getSelectedItem().toString();

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                account = sp1.getSelectedItem().toString();
                generate_list();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                account = "Account1";
                Toast.makeText(getApplicationContext(),"Nothing ",Toast.LENGTH_SHORT).show();
            }
        });


    }
    void generate_list()
    {
        String cat;
        netexp =0;
        nettotal=0;
        netinc=0;
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor c = db.rawQuery("select * from categories where username='admin'", null);
        c.moveToFirst();
        String ac;
        if(account.equalsIgnoreCase("All Accounts"))
            ac="";
        else
            ac =" and account ='"+account+"'";
        while (c.isAfterLast() == false) {
            cat = (c.getString(1));
            c.moveToNext();
            Cursor c1 = db.rawQuery("select * from transactions where username = '" + u + "' and category = '" + cat + "'"+ac, null);
            if (c1.getCount() != 0) {
                double incsum = 0;
                double expsum = 0f;
                c1.moveToLast();
                HashMap<String, String> hm = new HashMap<>();
                while (c1.isBeforeFirst() == false) {
                    hm.put("category", cat);
                    Double l = c1.getDouble(c1.getColumnIndex("amount"));
                    String typ = c1.getString(c1.getColumnIndex("type"));
                    if (typ.equals("expense"))
                        expsum = expsum + l;
                    else
                        incsum = incsum + l;
                    c1.moveToPrevious();
                }
                hm.put("incsum", Double.toString(incsum));
                hm.put("expsum", Double.toString(expsum));
                double totalsum = incsum - expsum;
                netexp = netexp+expsum;
                netinc = netinc+incsum;
                hm.put("totalsum", Double.toString(totalsum));
                arrayList.add(hm);
            }
        }
        c = db.rawQuery("select * from categories where username='" + u + "'", null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            cat = (c.getString(1));
            c.moveToNext();
            Cursor c1 = db.rawQuery("select * from transactions where username = '" + u + "' and category = '" + cat + "'"+ac, null);
            if (c1.getCount() != 0) {
                double incsum = 0;
                double expsum = 0f;
                c1.moveToLast();
                HashMap<String, String> hm = new HashMap<>();
                while (c1.isBeforeFirst() == false) {
                    hm.put("category", cat);
                    Double l = c1.getDouble(c1.getColumnIndex("amount"));
                    String typ = c1.getString(c1.getColumnIndex("type"));
                    if (typ.equals("expense"))
                        expsum = expsum + l;
                    else
                        incsum = incsum + l;
                    c1.moveToPrevious();
                }
                hm.put("incsum", Double.toString(incsum));
                hm.put("expsum", Double.toString(expsum));
                netexp = netexp+expsum;
                netinc = netinc+incsum;
                double totalsum = incsum - expsum;
                hm.put("totalsum", Double.toString(totalsum));
                arrayList.add(hm);
            }
        }
        nettotal = netinc - netexp;
        inc = (TextView)findViewById(R.id.netincome);
        exp = (TextView)findViewById(R.id.netexpense);
        tot = (TextView)findViewById(R.id.nettotal);
        inc.setText(Double.toString(netinc));
        exp.setText(Double.toString(netexp));
        tot.setText(Double.toString(nettotal));
        //bal.setText("₹ " + Double.toString(sum));
        ListAdapter listAdapter = new SimpleAdapter(getApplicationContext(), arrayList, R.layout.report_list_item, new String[]
                {"category", "totalsum", "incsum", "expsum",}, new int[]{R.id.de, R.id.rs, R.id.incsum, R.id.expsum});
        listView.setAdapter(listAdapter);
    }
}
