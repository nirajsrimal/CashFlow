package com.example.niraj.cashflow;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView t,name,email;
    SQLiteDatabase db;
    TextView bal;
    String u;
    double sum=0;
    ListView listView;
    SharedPreferences sharedPreferences;
    AdapterView.AdapterContextMenuInfo menuinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        t = (TextView)findViewById(R.id.hi);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        u = sharedPreferences.getString("username","");
        t.setText("Hello " + u);
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        listView = (ListView)findViewById(R.id.listview);
        bal = (TextView) findViewById(R.id.amount);
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        Cursor c1 = db.rawQuery("select * from transactions where username = '"+u+"'",null);
        int z=5;
        if(c1.getCount()!=0) {
            sum=0;
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
                z=z-1;
                if(z==0)
                    break;
            }
            bal.setText("₹ "+Double.toString(sum));
            ListAdapter listAdapter = new SimpleAdapter(getApplicationContext(), arrayList, R.layout.custom_list_item, new String[]
                    {"description", "amount", "category", "account", "type", "date", "time"}, new int[]{R.id.de, R.id.rs, R.id.cat, R.id.acc, R.id.type, R.id.dt, R.id.time});
            listView.setAdapter(listAdapter);
        }
        registerForContextMenu(listView);
        menuinfo = null;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),add_trans.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
               this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                    sum=0;
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
                String amt="",dec="",time1="";
                if (c1.getCount() != 0) {
                    sum=0;
                    c1.moveToLast();
                    while (c1.isBeforeFirst() == false) {
                        if(x==index) {
                            amt = c1.getString(c1.getColumnIndex("amount"));
                            dec = c1.getString(c1.getColumnIndex("description"));
                            time1 = c1.getString(c1.getColumnIndex("lineartime"));
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
                    db.execSQL("DELETE from transactions where amount =" + amt+" and description = '"+dec+"' and lineartime='"+time1+"'");
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent1 =new Intent(this,dashboard.class);
                    startActivity(intent1);
                    finish();
                }
                    return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listview) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            menuinfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Options");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finishAndRemoveTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username","");
            editor.commit();
            finish();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(),profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_report) {
            Intent intent = new Intent(getApplicationContext(),report.class);
            startActivity(intent);
        } else if (id == R.id.nav_acc) {
            Intent intent = new Intent(getApplicationContext(),add_cat_acc.class);
            intent.putExtra("type","Accounts");
            startActivity(intent);
        } else if (id == R.id.nav_cat) {
                Intent intent = new Intent(getApplicationContext(),add_cat_acc.class);
                intent.putExtra("type","Categories");
                startActivity(intent);
        } else if (id == R.id.nav_dash) {
            Intent intent = new Intent(getApplicationContext(),dashboard.class);
            intent.putExtra("type","categories");
            startActivity(intent);
            finish();
        }
        else if (id==R.id.nav_records)
        {
            Intent intent = new Intent(getApplicationContext(),records.class);
            startActivity(intent);
        }
        else if (id==R.id.nav_charts)
        {
            Intent intent = new Intent(getApplicationContext(),charts.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int z=5;
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor c1 = db.rawQuery("select * from transactions where username = '" + u + "'", null);
        if (c1.getCount() != 0) {
            sum=0;
            c1.moveToLast();
            while (c1.isBeforeFirst() == false) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("description", c1.getString(c1.getColumnIndex("description")));
                System.out.print(hm.get("description"));
                Double l = c1.getDouble(c1.getColumnIndex("amount"));
                hm.put("amount", l.toString());
                hm.put("category", c1.getString(c1.getColumnIndex("category")));
                hm.put("account", c1.getString(c1.getColumnIndex("account")));
                String typ = c1.getString(c1.getColumnIndex("type"));
                if (typ.equals("expense"))
                    sum = sum - l;
                else
                    sum = sum + l;
                hm.put("type", typ);
                hm.put("date", c1.getString(c1.getColumnIndex("lineardate")));
                hm.put("time", c1.getString(c1.getColumnIndex("lineartime")));
                if(z>0)
                arrayList.add(hm);
                c1.moveToPrevious();
                z=z-1;
            }
            bal.setText("₹ "+Double.toString(sum));
            ListAdapter listAdapter = new SimpleAdapter(getApplicationContext(), arrayList, R.layout.custom_list_item, new String[]
                    {"description", "amount", "category", "account", "type", "date", "time"}, new int[]{R.id.de, R.id.rs, R.id.cat, R.id.acc, R.id.type, R.id.dt, R.id.time});
            listView.setAdapter(listAdapter);

        }
    }
}


