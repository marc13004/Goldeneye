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


public class DataActivity extends AppCompatActivity {
    /* titre et labels textview*/
    TextView titreData;
    TextView labelTemp;
    TextView labelHumidite;
    TextView labelBarometre;
    TextView labelAltitude;
    /*raspi Data textview*/
    TextView raspiTemp;
    TextView raspiHumi;
    TextView raspiBaro;
    TextView raspiAlti;

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
        labelAltitude = (TextView) findViewById(R.id.labelTemp2);
        labelAltitude.setText("Température");
        raspiTemp = (TextView) findViewById(R.id.temp);
        raspiHumi = (TextView) findViewById(R.id.humi);
        raspiBaro = (TextView) findViewById(R.id.baro);
        raspiAlti = (TextView) findViewById(R.id.temp2);

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
