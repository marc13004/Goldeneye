package com.example.a.webview;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlarmeActivity extends AppCompatActivity implements AlarmesAdapter.AlarmesAdapterListener {

    AlarmesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        List<String> maListe = new ArrayList<>();

        maListe.add(0,"Elvis");
        maListe.add(1,"Bob");
        maListe.add(2,"Alerte");

        adapter = new AlarmesAdapter(this, maListe, new AlarmesAdapter.BtnClickListener() {
            @Override
            public void onBtnClick (String son, final int position, View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmeActivity.this);
                builder.setTitle("Alarmes");

                String nom = son;
                builder.setMessage("Confirmer la lecture de: " + nom);
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String URL = "http://"+ReglagesActivity.urlchecked+":3000/alarmes/"+(position+1);
                        WebServiceGET WebServiceGET = new WebServiceGET();
                        WebServiceGET.execute(URL);
                        try {
                            Log.i("alarme", WebServiceGET.get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                };
                builder.setPositiveButton("Oui", listener);
                builder.setNegativeButton("Non", null);
                builder.show();

            }
        });

        adapter.addListener(this);

        ListView list = (ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);
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
