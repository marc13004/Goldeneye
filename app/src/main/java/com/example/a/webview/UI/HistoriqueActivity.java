package com.example.a.webview.UI;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.a.webview.R;
import com.example.a.webview.RESTService.HttpHandler;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HistoriqueActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> objetList;
    ArrayList<String> listDate;
    static float j11,j12,j13,j21,j22,j23,j31,j32,j33,j41,j42,j43,j51,j52,j53,j61,j62,j63,j71,j72,j73;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        // Barre d'outils
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        objetList = new ArrayList<>();
        listDate = new ArrayList<String>();
        new GetContacts().execute();

        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.barchart);
        BarChart bchart = (BarChart) findViewById(R.id.barchart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        // create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup1 = new ArrayList<BarEntry>();
        bargroup1.add(new BarEntry(0.2f, j73));
        bargroup1.add(new BarEntry(1.2f, j63));
        bargroup1.add(new BarEntry(2.2f, j53));
        bargroup1.add(new BarEntry(3.2f, j43));
        bargroup1.add(new BarEntry(4.2f, j33));
        bargroup1.add(new BarEntry(5.2f, j23));
        bargroup1.add(new BarEntry(6.2f, j13));


        // create BarEntry for Bar Group 2
        ArrayList<BarEntry> bargroup2 = new ArrayList<BarEntry>();
        bargroup2.add(new BarEntry(0.5f, j72));
        bargroup2.add(new BarEntry(1.5f, j62));
        bargroup2.add(new BarEntry(2.5f, j52));
        bargroup2.add(new BarEntry(3.5f, j42));
        bargroup2.add(new BarEntry(4.5f, j32));
        bargroup2.add(new BarEntry(5.5f, j22));
        bargroup2.add(new BarEntry(6.5f, j12));

        // create BarEntry for Bar Group 3
        ArrayList<BarEntry> bargroup3 = new ArrayList<BarEntry>();
        bargroup3.add(new BarEntry(0.8f, j71));
        bargroup3.add(new BarEntry(1.8f, j61));
        bargroup3.add(new BarEntry(2.8f, j51));
        bargroup3.add(new BarEntry(3.8f, j41));
        bargroup3.add(new BarEntry(4.8f, j31));
        bargroup3.add(new BarEntry(5.8f, j21));
        bargroup3.add(new BarEntry(6.8f, j11));




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



    }

    private class GetContacts extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(HistoriqueActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler httpHandler = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://"+ReglagesActivity.urlchecked+":3000/dht/lastdays";
            String jsonStr = httpHandler.getRequest(url);

            Log.e("***", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray objets = new JSONArray(jsonStr) ;
                    // looping through All Contacts
                    for (int i = 0; i < objets.length(); i++) {
                        JSONObject c = objets.getJSONObject(i);
                        Log.i("**c****",c +"");
                        String id = c.getString("idDHT11");
                        String temperature = c.getString("temperature");
                        String humidity = c.getString("humidity");
                        String datetime = c.getString("datetime");

                        // tmp hash map for single contact
                        HashMap<String, String> dhtData = new HashMap<>();

                        // adding each child node to HashMap key => value
                        dhtData.put("id", id);
                        dhtData.put("temperature", temperature);
                        dhtData.put("humidity", humidity);
                        dhtData.put("datetime", datetime);

                        // adding contact to contact list
                        objetList.add(dhtData);
                        Log.i("**objetList****",objetList.size()+"");
                    }
                } catch (final JSONException e) {
                    Log.e("***", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e("***", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdfNow = new SimpleDateFormat("dd");
            String currentDay = sdfNow.format(new Date());
            for(int i =0; i<objetList.size();i++){

                String sdate = objetList.get(i).get("datetime");
                String sdate2 = sdate.substring(0, 10)+" "+sdate.substring(11,(sdate.length()-5));
                listDate.add(sdate2);
                java.util.Collections.sort(listDate);
                String sDay = sdate.substring(8,10);
                String sHour = sdate.substring(11,13);
                int icurrentDay = Integer.parseInt(currentDay);
                int iDay = Integer.parseInt(sDay);

                int iHour = Integer.parseInt(sHour);

                if(icurrentDay>=1&&icurrentDay<=7){
                    String sMonth = sdate.substring(5, 7);
                    int iMonth = Integer.parseInt(sMonth);

                    switch (iMonth){
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            Log.i("","entre 1 et 31");
                            switch(icurrentDay){
                                case 1: icurrentDay = 32;
                                    break;
                                case 2: switch (iDay){
                                    case 31:
                                    case 30:
                                    case 29:
                                    case 28:
                                    case 27:
                                    case 26:
                                        icurrentDay = 33;
                                        break;
                                }break;
                                case 3: switch (iDay){
                                    case 31:
                                    case 30:
                                    case 29:
                                    case 28:
                                    case 27:
                                        icurrentDay = 34;
                                        break;
                                }break;
                                case 4: switch (iDay){
                                    case 31:
                                    case 30:
                                    case 29:
                                    case 28:
                                        icurrentDay = 35;
                                        break;
                                }break;
                                case 5: switch (iDay){
                                    case 31:
                                    case 30:
                                    case 29:
                                        icurrentDay = 36;
                                        break;
                                }break;
                                case 6: switch (iDay){
                                    case 31:
                                    case 30:
                                        icurrentDay = 37;
                                        break;
                                }break;
                                case 7: switch (iDay){
                                    case 31:
                                        icurrentDay = 38;
                                        break;
                                }break;
                            }

                            break;
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            Log.i("","entre 1 et 30");
                            switch(icurrentDay){
                                case 1: icurrentDay = 31;
                                    break;
                                case 2: switch (iDay){
                                    case 30:
                                    case 29:
                                    case 28:
                                    case 27:
                                    case 26:
                                    case 25:
                                        icurrentDay = 32;
                                        break;
                                }break;
                                case 3: switch (iDay){
                                    case 30:
                                    case 29:
                                    case 28:
                                    case 27:
                                    case 26:
                                        icurrentDay = 33;
                                        break;
                                }break;
                                case 4: switch (iDay){
                                    case 30:
                                    case 29:
                                    case 28:
                                    case 27:
                                        icurrentDay = 34;
                                        break;
                                }break;
                                case 5: switch (iDay){
                                    case 30:
                                    case 29:
                                    case 28:
                                        icurrentDay = 35;
                                        break;
                                }break;
                                case 6: switch (iDay){
                                    case 30:
                                    case 29:
                                        icurrentDay = 36;
                                        break;
                                }break;
                                case 7: switch (iDay){
                                    case 30:
                                        icurrentDay = 37;
                                        break;
                                }break;
                            }
                            break;


                    }
                }

                int range = icurrentDay - iDay;
                switch (range){
                    case 1:
                        if(iHour>5 && iHour<10){
                            String sTempMorning1 = objetList.get(i).get("temperature");
                            Log.i("case 1",sTempMorning1+" range "+ range );
                            int itempMorning = Integer.parseInt(sTempMorning1);
                            HistoriqueActivity.j11=itempMorning;

                        }
                        if(iHour>10 && iHour<14){
                            String sTempAfternoon1 = objetList.get(i).get("temperature");
                            Log.i("case 1",sTempAfternoon1+" range "+ range );
                            int itempAfternoon = Integer.parseInt(sTempAfternoon1);
                            HistoriqueActivity.j12=itempAfternoon;

                        }
                        if(iHour>17 && iHour<20){
                            String sTempNight1 = objetList.get(i).get("temperature");
                            Log.i("case 1",sTempNight1+" range "+ range );
                            int itempNight = Integer.parseInt(sTempNight1);
                            HistoriqueActivity.j13=itempNight;

                        }
                        break;
                    case 2:
                        if(iHour>5 && iHour<10){
                            String sTempMorning2 = objetList.get(i).get("temperature");
                            Log.i("case 2",sTempMorning2+" range "+ range );
                            int itempMorning2 = Integer.parseInt(sTempMorning2);
                            HistoriqueActivity.j21=itempMorning2;
                        }
                        if(iHour>10 && iHour<14){
                            String sTempAfternoon2 = objetList.get(i).get("temperature");
                            Log.i("case 2",sTempAfternoon2+" range "+ range );
                            int itempAfternoon2 = Integer.parseInt(sTempAfternoon2);
                            HistoriqueActivity.j22=itempAfternoon2;
                        }
                        if(iHour>17 && iHour<20){
                            String sTempNight2 = objetList.get(i).get("temperature");
                            Log.i("case 2",sTempNight2+" range "+ range );
                            int itempNight2 = Integer.parseInt(sTempNight2);
                            HistoriqueActivity.j23=itempNight2;
                        }
                        break;
                    case 3:
                        if(iHour>5 && iHour<10){
                            String sTempMorning3 = objetList.get(i).get("temperature");
                            Log.i("case 3",sTempMorning3+" range "+ range );
                            int itempMorning3 = Integer.parseInt(sTempMorning3);
                            HistoriqueActivity.j31=itempMorning3;
                        }
                        if(iHour>10 && iHour<14){
                            String sTempAfternoon3 = objetList.get(i).get("temperature");
                            Log.i("case 2",sTempAfternoon3+" range "+ range );
                            int itempAfternoon3 = Integer.parseInt(sTempAfternoon3);
                            HistoriqueActivity.j32=itempAfternoon3;
                        }
                        if(iHour>17 && iHour<20){
                            String sTempNight3 = objetList.get(i).get("temperature");
                            Log.i("case 2",sTempNight3+" range "+ range );
                            int itempNight3 = Integer.parseInt(sTempNight3);
                            HistoriqueActivity.j33=itempNight3;
                        }
                        break;
                    case 4:
                        if(iHour>5 && iHour<10){
                            String sTempMorning4 = objetList.get(i).get("temperature");
                            Log.i("case 4 sTempMorning ",sTempMorning4+" range "+ range );
                            int itempMorning4 = Integer.parseInt(sTempMorning4);
                            HistoriqueActivity.j41=itempMorning4;
                        }
                        if(iHour>10 && iHour<14){
                            String sTempAfternoon4 = objetList.get(i).get("temperature");
                            Log.i("case 4 sTempAfternoon ",sTempAfternoon4+" range "+ range );
                            int itempAfternoon4 = Integer.parseInt(sTempAfternoon4);
                            HistoriqueActivity.j42=itempAfternoon4;
                        }
                        if(iHour>17 && iHour<20){
                            String sTempNight4 = objetList.get(i).get("temperature");
                            Log.i("case 4 sTempNight ",sTempNight4+" range "+ range );
                            int itempNight4 = Integer.parseInt(sTempNight4);
                            HistoriqueActivity.j43=itempNight4;
                        }
                        break;
                    case 5:
                        if(iHour>5 && iHour<10){
                            String sTempMorning5 = objetList.get(i).get("temperature");
                            Log.i("case 5 sTempMorning ",sTempMorning5+" range "+ range );
                            int itempMorning5 = Integer.parseInt(sTempMorning5);
                            HistoriqueActivity.j51=itempMorning5;
                        }
                        if(iHour>10 && iHour<14){
                            String sTempAfternoon5 = objetList.get(i).get("temperature");
                            Log.i("case 5 sTempAfternoon ",sTempAfternoon5+" range "+ range );
                            int itempAfternoon5 = Integer.parseInt(sTempAfternoon5);
                            HistoriqueActivity.j52=itempAfternoon5;
                        }
                        if(iHour>17 && iHour<20){
                            String sTempNight5 = objetList.get(i).get("temperature");
                            Log.i("case 5 sTempNight ",sTempNight5+" range "+ range );
                            int itempNight5 = Integer.parseInt(sTempNight5);
                            HistoriqueActivity.j53=itempNight5;
                        }
                        break;
                    case 6:
                        if(iHour>5 && iHour<10){
                            String sTempMorning6 = objetList.get(i).get("temperature");
                            Log.i("case 6 sTempMorning ",sTempMorning6+" range "+ range );
                            int itempMorning6 = Integer.parseInt(sTempMorning6);
                            HistoriqueActivity.j61=itempMorning6;
                        }
                        if(iHour>10 && iHour<14){
                            String sTempAfternoon6 = objetList.get(i).get("temperature");
                            Log.i("case 6 sTempAfternoon ",sTempAfternoon6+" range "+ range );
                            int itempAfternoon6 = Integer.parseInt(sTempAfternoon6);
                            HistoriqueActivity.j62=itempAfternoon6;
                        }
                        if(iHour>17 && iHour<20){
                            String sTempNight6 = objetList.get(i).get("temperature");
                            Log.i("case 6 sTempNight ",sTempNight6+" range "+ range );
                            int itempNight6 = Integer.parseInt(sTempNight6);
                            HistoriqueActivity.j63=itempNight6;
                        }
                        break;
                    case 7:
                        if(iHour>5 && iHour<10){
                            String sTempMorning7 = objetList.get(i).get("temperature");
                            Log.i("case 7 sTempMorning ",sTempMorning7+" range "+ range );
                            int itempMorning7 = Integer.parseInt(sTempMorning7);
                            HistoriqueActivity.j71=itempMorning7;
                        }
                        if(iHour>10 && iHour<14){
                            String sTempAfternoon7 = objetList.get(i).get("temperature");
                            Log.i("case 7 sTempAfternoon ",sTempAfternoon7+" range "+ range );
                            int itempAfternoon7 = Integer.parseInt(sTempAfternoon7);
                            HistoriqueActivity.j72=itempAfternoon7;
                        }
                        if(iHour>17 && iHour<20){
                            String sTempNight7 = objetList.get(i).get("temperature");
                            Log.i("case 7 sTempNight ",sTempNight7+" range "+ range );
                            int itempNight7 = Integer.parseInt(sTempNight7);
                            HistoriqueActivity.j73=itempNight7;
                        }
                        break;
                }
                java.util.Collections.sort(listDate);
            }
            Log.i("***date***", listDate.toString());
        }
    }

}