package com.example.a.webview.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.a.webview.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoriqueActivity extends AppCompatActivity {

    String[] sweek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        // Barre d'outils
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        sweek = new String[7];
        sweek[0]="Monday";
        sweek[1]="Tuesday";
        sweek[2]="Wednesday";
        sweek[3]="Thursday";
        sweek[4]="Friday";
        sweek[5]="Saturday";
        sweek[6]="Sunday";

        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.barchart);
        BarChart bchart = (BarChart) findViewById(R.id.barchart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);


        // create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup1 = new ArrayList<BarEntry>();
        bargroup1.add(new BarEntry(0.2f, 25));
        bargroup1.add(new BarEntry(1.2f, 25));
        bargroup1.add(new BarEntry(2.2f, 25));
        bargroup1.add(new BarEntry(3.2f, 25));
        bargroup1.add(new BarEntry(4.2f, 25));
        bargroup1.add(new BarEntry(5.2f, 25));
        bargroup1.add(new BarEntry(6.2f, 25));


        // create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup2 = new ArrayList<BarEntry>();
        bargroup2.add(new BarEntry(0.5f, 35));
        bargroup2.add(new BarEntry(1.5f, 35));
        bargroup2.add(new BarEntry(2.5f, 35));
        bargroup2.add(new BarEntry(3.5f, 35));
        bargroup2.add(new BarEntry(4.5f, 35));
        bargroup2.add(new BarEntry(5.5f, 35));
        bargroup2.add(new BarEntry(6.5f, 35));


        // create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup3 = new ArrayList<BarEntry>();
        bargroup3.add(new BarEntry(0.8f, 15));
        bargroup3.add(new BarEntry(1.8f, 15));
        bargroup3.add(new BarEntry(2.8f, 15));
        bargroup3.add(new BarEntry(3.8f, 15));
        bargroup3.add(new BarEntry(4.8f, 15));
        bargroup3.add(new BarEntry(5.8f, 15));
        bargroup3.add(new BarEntry(6.8f, 15));




        BarDataSet barDataSet3 = new BarDataSet(bargroup3, "Matin");
        barDataSet3.setColor(Color.rgb(102, 205, 0));

        BarDataSet barDataSet2 = new BarDataSet(bargroup2, "Midi");
        barDataSet2.setColor(Color.rgb(255, 127, 36));

        BarDataSet barDataSet1 = new BarDataSet(bargroup1, "Soir");
        barDataSet1.setColor(Color.rgb(32, 178, 170));



        BarDataSet set1;
        BarDataSet set2;
        BarDataSet set3;



        set3 = new BarDataSet(bargroup3, "Matin");
        set3.setColors(Color.rgb(102, 205, 0));
        set2 = new BarDataSet(bargroup2, "Midi");
        set2.setColors(Color.rgb(255, 127, 36));
        set1 = new BarDataSet(bargroup1, "Soir");
        set1.setColors(Color.rgb(32, 178, 170));



        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);



        BarData data = new BarData(dataSets);

        data.setValueTextSize(10f);
        //data.setValueTextColor(Color.BLUE);
        data.setBarWidth(0.1f);

        bchart.setTouchEnabled(false);
        bchart.setData(data);
        //bchart.animateX(5000);
        bchart.animateY(2000);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if(dayOfTheWeek.equals(sweek[3])){
            Log.i("**day**", dayOfTheWeek);
        }

    }
}
