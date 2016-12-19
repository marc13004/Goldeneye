package com.example.a.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class DataActivity extends AppCompatActivity {
    /* titres et labels textview*/
    TextView labelTemp;
    TextView labelHumidite;
    TextView labelBarometre;
    TextView labelTemp2;
    /*raspi Data textview*/
    TextView raspiTemp;
    TextView raspiHumi;
    TextView raspiBaro;
    TextView raspiTemp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        labelTemp = (TextView) findViewById(R.id.labelTemp);
        labelTemp.setText("Température");
        labelHumidite = (TextView) findViewById(R.id.labelHumidite);
        labelHumidite.setText("Humidité");
        labelBarometre = (TextView) findViewById(R.id.labelBarometre);
        labelBarometre.setText("Pression");
        labelTemp2 = (TextView) findViewById(R.id.labelTemp2);
        labelTemp2.setText("Température");

        String URL = "http://"+ReglagesActivity.urlchecked+":3000/dht";
        WebServiceGET WebServiceGET = new WebServiceGET();
        WebServiceGET.execute(URL);

        String temp = "";
        String humi = "";
        while (WebServiceGET.jo == null) {

        }
        if (WebServiceGET.httpStatus == 200) {
            try {
                temp = WebServiceGET.jo.getString("temperature");
                humi = WebServiceGET.jo.getString("humidity");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        raspiTemp = (TextView) findViewById(R.id.temp);
        raspiTemp.setText(temp);
        raspiHumi = (TextView) findViewById(R.id.humi);
        raspiHumi.setText(humi);

        String URL2 = "http://"+ReglagesActivity.urlchecked+":3000/bmp";
        WebServiceGET WebServiceGET2 = new WebServiceGET();
        WebServiceGET2.execute(URL2);

        String baro = "";
        String temp2 = "";
        while (WebServiceGET2.jo == null) {

        }
        if (WebServiceGET2.httpStatus == 200) {
            try {
                baro = WebServiceGET.jo.getString("pressure");
                temp2 = WebServiceGET.jo.getString("temperature");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        raspiBaro = (TextView) findViewById(R.id.baro);
        raspiBaro.setText(baro);
        raspiTemp2 = (TextView) findViewById(R.id.temp2);
        raspiTemp2.setText(temp2);


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
