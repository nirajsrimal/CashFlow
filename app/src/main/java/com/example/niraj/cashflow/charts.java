package com.example.niraj.cashflow;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class charts extends Activity {

    TextView tv;
    private String [] xData = new String[9];
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    //to modify
    private float[] yData = new float[9];

    int k=0;
    private String[] xData1;
    Spinner sp1;
    String u,account;
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        //pieChart.setDescription("lol");

        tv = (TextView)findViewById(R.id.sum);
        pieChart.animateXY(1400, 1400);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(10);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        //pieChart.spin( 500,0,360, Easing.EasingOption.EaseInOutQuad );

        //pieChart.animate();
       /* pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e ,Highlight h) {
                if (e == null)
                    return;
                //Toast.makeText(TotalBalance.this,e,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });*/
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        u = sharedPreferences.getString("username", "");
        account="All accounts";
        generate_chart();
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
                generate_chart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                account = "Account1";
                Toast.makeText(getApplicationContext(),"Nothing ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generate_chart()
    {
        pieChart.setDescription(null);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ac="";
        if(account.equalsIgnoreCase("All Accounts"))
            ac="";
        else
            ac =" and account ='"+account+"'";
        String user = sharedPreferences.getString("username", "");
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
        float totalSum = 0;
        xData1 = new String[9];
        k = 0;
        for (int i = 0; i < arrayList.size(); i++){
            Cursor c1 = db.rawQuery("select * from transactions where type = 'expense' and username ='"+user+"' and category = '"+arrayList.get(i)+"'"+ac,null);
            if(c1.getCount()!=0) {
                xData1[k++] = arrayList.get(i).toString();
            }
        }
        for (int i = 0; i < k; i++ ){
            Cursor c1 = db.rawQuery("select * from transactions where type = 'expense' and username ='"+user+"' and  category = '"+xData1[i]+"'"+ac,null);
            c1.moveToFirst();
            float sumc =0;
            while(c1.isAfterLast()==false) {
                sumc = sumc + Float.parseFloat(c1.getString(c1.getColumnIndex("amount")));
                c1.moveToNext();
            }
            yData[i] = sumc;
            totalSum += yData[i];
        }
        Legend l = pieChart.getLegend();
        // l.setEnabled(false);
        tv.setText("Total Expense : ₹ "+totalSum);

        addData();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }
    public void addData(){
        List<PieEntry> yVals1 =new ArrayList<>();

        for(int i = 0; i < k; i++){
            yVals1.add(new PieEntry(yData[i],xData1[i]));

        }

        ArrayList<String> xVals = new ArrayList<String>();

        for(int i = 0; i < k; i++){
            xVals.add(xData1[i]);

            PieDataSet dataSet = new PieDataSet(yVals1,"");
            dataSet.setSliceSpace(3);
            dataSet.setSelectionShift(5);

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);



            // instantiate pie data object now
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.GRAY);

            pieChart.setData(data);
            pieChart.setDrawSliceText(false);

            // undo all highlights
            pieChart.highlightValues(null);

            // update pie chart
            pieChart.invalidate();

        }
    }
}