package com.example.a.webview.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a.webview.RESTService.WebServiceGET;
import com.example.a.webview.R;

import org.json.JSONException;


public class DataActivity extends AppCompatActivity {

    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    String temp = "";
    String humi = "";
    String baro = "";
    String tempbaro = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView6);
        tv4 = (TextView) findViewById(R.id.textView7);
        tv1.setText("DHT11");
        tv2.setText("BMP180");

        ImageView iv1 = (ImageView) findViewById(R.id.imageView5);
        iv1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String URL = "http://"+ReglagesActivity.urlchecked+":3000/dht";
                WebServiceGET dht = new WebServiceGET();
                dht.execute(URL);

                while (dht.jo == null) {

                }
                if (dht.httpStatus == 200) {
                    try {
                        temp = dht.jo.getString("temp");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        humi = dht.jo.getString("humidity");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tv1.setText("T°: " + temp);
                tv3.setText("H%: " + humi);
            }
        });

        ImageView iv2 = (ImageView) findViewById(R.id.imageView6);
        iv2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String URL2 = "http://" + ReglagesActivity.urlchecked + ":3000/bmp";
                WebServiceGET bmp = new WebServiceGET();
                bmp.execute(URL2);

                while (bmp.jo == null) {

                }
                if (bmp.httpStatus == 200) {
                    try {
                        baro = bmp.jo.getString("pressure");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        tempbaro = bmp.jo.getString("temperature");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tv2.setText("T°: " + tempbaro);
                tv4.setText("hPa: " + baro);
            }
        });


        Button bHistorique = (Button)findViewById(R.id.button);
        bHistorique.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent activityChangeIntent = new Intent(DataActivity.this, HistoriqueActivity.class);
                DataActivity.this.startActivity(activityChangeIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
