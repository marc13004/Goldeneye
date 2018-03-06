package com.example.a.webview.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.webview.RESTService.WebServiceGET;
import com.example.a.webview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DataActivity extends AppCompatActivity {

    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    String temp = "";
    String humi = "";
    String baro = "";
    String tempBaro = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        // Barre d'outils
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
                // Récupération des valeurs du dht
                String URL = "http://"+ReglagesActivity.urlchecked+":3000/dht";
                final WebServiceGET WebServiceGET = new WebServiceGET();
                AsyncTask getDhtReturn = WebServiceGET.execute(URL);
                Object resultTask = null;
                try {
                    resultTask = getDhtReturn.get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                JSONObject task = null;
                try {
                    task = new JSONObject(resultTask.toString());
                    Log.i("***task***",task+"");
                    String status = task.getString("status");
                    int iStatus = Integer.parseInt(status);
                    if (iStatus == 0) {
                        temp = task.getString("temp");
                        humi = task.getString("humidity");
                        // Affichage des valeurs
                        tv1.setText("T°: " + temp);
                        tv3.setText("H%: " + humi);
                    }else if(iStatus == 1){
                        String message = task.getString("message");
                        Toast.makeText(DataActivity.this,"server message "+message+ "*** http status: "+ getDhtReturn.getStatus() +" ***",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView iv2 = (ImageView) findViewById(R.id.imageView6);
        iv2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String URL = "http://"+ReglagesActivity.urlchecked+":3000/bmp";
                final WebServiceGET WebServiceGET = new WebServiceGET();
                AsyncTask getDhtReturn = WebServiceGET.execute(URL);
                Object resultTask = null;
                try {
                    resultTask = getDhtReturn.get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                JSONObject task = null;
                try {
                    task = new JSONObject(resultTask.toString());
                    Log.i("***task***",task+"");
                    String status = task.getString("status");
                    int iStatus = Integer.parseInt(status);
                    if (iStatus == 0) {
                        baro = task.getString("pressure");
                        tempBaro = task.getString("temperature");
                        tv2.setText("T°: " + tempBaro);
                        tv4.setText("hPa: " + baro);
                    }else if(iStatus == 1){
                        String message = task.getString("message");
                        Toast.makeText(DataActivity.this,"server message "+message+ "*** http status: "+ getDhtReturn.getStatus() +" ***",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Accès à l'HistoriqueActivity
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
