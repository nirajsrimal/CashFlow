package com.example.niraj.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class records extends Activity {
    ListView listView;
    SQLiteDatabase db;
    Double sum;
    TextView bal;
    SharedPreferences sharedPreferences;
    String u;
    Spinner sp1;
    String account="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        listView = (ListView)findViewById(R.id.listview);
        bal = (TextView) findViewById(R.id.amount);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        u = sharedPreferences.getString("username","");
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
                generate_transactions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                account = "Account1";
                Toast.makeText(getApplicationContext(),"Nothing ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listview) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch(item.getItemId()) {
            case R.id.edit:

                Cursor c0 = db.rawQuery("select * from transactions where username = '" + u + "'", null);
                int x1=0,flag1=0;
                String amt1="",dec1="",type="",date="",time="",acc="",ct="";
                if (c0.getCount() != 0) {
                    c0.moveToLast();
                    while (c0.isBeforeFirst() == false) {
                        if(x1==index) {
                            amt1 = c0.getString(c0.getColumnIndex("amount"));
                            dec1 = c0.getString(c0.getColumnIndex("description"));
                            type = c0.getString(c0.getColumnIndex("type"));
                            acc = c0.getString(c0.getColumnIndex("account"));
                            ct = c0.getString(c0.getColumnIndex("category"));
                            date = c0.getString(c0.getColumnIndex("lineardate"));
                            time = c0.getString(c0.getColumnIndex("lineartime"));

                            flag1=1;
                            break;
                        }
                        else
                        {
                            x1++;
                        }
                        c0.moveToPrevious();
                    }
                }
                if(flag1==1) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tdescription", dec1);
                    editor.putString("tamount", amt1);
                    editor.putString("taccount", acc);
                    editor.putString("ttype", type);
                    editor.putString("tcategory", ct);
                    editor.putString("tdate", date);
                    editor.putString("ttime", time);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), edit_trans.class);
                    startActivity(intent);
                }
                return true;
            case R.id.delete:
                Cursor c1 = db.rawQuery("select * from transactions where username = '" + u + "'", null);
                int x=0,flag=0;
                String amt="",dec="";
                if (c1.getCount() != 0) {
                    c1.moveToLast();
                    while (c1.isBeforeFirst() == false) {
                        if(x==index) {
                            amt = c1.getString(c1.getColumnIndex("amount"));
                            dec = c1.getString(c1.getColumnIndex("description"));
                            flag=1;
                            break;
                        }
                        else
                        {
                            x++;
                        }
                        c1.moveToPrevious();
                    }
                }
                if(flag==1) {
                    db.execSQL("DELETE from transactions where amount =" + amt+" and description = '"+dec+"'");
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent1 =new Intent(this,records.class);
                    startActivity(intent1);
                    finish();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    void generate_transactions()
    {
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        Cursor c1;
        if(account=="All Accounts")
            c1 = db.rawQuery("select * from transactions where username = '"+u+"'",null);
        else
            c1 = db.rawQuery("select * from transactions where username = '"+u+"' and account ='"+account+"'",null);
        if(c1.getCount()!=0) {
            sum=0.0;
            c1.moveToLast();
            while (c1.isBeforeFirst() == false) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("description", c1.getString(c1.getColumnIndex("description")));
                System.out.print(hm.get("description"));
                Double l = c1.getDouble(c1.getColumnIndex("amount"));
                hm.put("amount", l.toString());
                hm.put("category", c1.getString(c1.getColumnIndex("category")));
                hm.put("account", c1.getString(c1.getColumnIndex("account")));
                String typ =  c1.getString(c1.getColumnIndex("type"));
                if(typ.equals("expense"))
                    sum = sum-l;
                else
                    sum = sum+l;
                hm.put("type",typ);
                hm.put("date", c1.getString(c1.getColumnIndex("lineardate")));
                hm.put("time", c1.getString(c1.getColumnIndex("lineartime")));
                arrayList.add(hm);
                c1.moveToPrevious();
            }
            bal.setText("₹ "+Double.toString(sum));
            ListAdapter listAdapter = new SimpleAdapter(getApplicationContext(), arrayList, R.layout.custom_list_item, new String[]
                    {"description", "amount", "category", "account", "type", "date", "time"}, new int[]{R.id.de, R.id.rs, R.id.cat, R.id.acc, R.id.type, R.id.dt, R.id.time});
            listView.setAdapter(listAdapter);
            registerForContextMenu(listView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        generate_transactions();
    }
}
