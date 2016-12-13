package com.example.a.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        titreData = (TextView) findViewById(R.id.titre);
        titreData.setText("Données");
        labelTemp = (TextView) findViewById(R.id.labelTemp);
        labelTemp.setText("Température");
        labelHumidite = (TextView) findViewById(R.id.labelHumidite);
        labelHumidite.setText("Humidité");
        labelBarometre = (TextView) findViewById(R.id.labelBarometre);
        labelBarometre.setText("Pression");
        labelAltitude = (TextView) findViewById(R.id.labelAltitude);
        labelAltitude.setText("Altitude");
        raspiTemp = (TextView) findViewById(R.id.temp);
        raspiHumi = (TextView) findViewById(R.id.humi);
        raspiBaro = (TextView) findViewById(R.id.baro);
        raspiAlti = (TextView) findViewById(R.id.alti);
    }
}
