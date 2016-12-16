package com.example.a.webview;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;


public class ReglagesActivity extends AppCompatActivity implements ReglagesAdapter.ReglagesAdapterListener {

    GestionSQLite urlsBdd;
    String nom;
    ReglagesAdapter adapter;
    static String urlchecked = "10.111.61.96";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglages);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Création d'une instance de ma classe GestionSQLite
        urlsBdd = new GestionSQLite(this);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.editText);
                String value = et.getText().toString();

                //On ouvre la base de données pour écrire dedans
                urlsBdd.open();

                String url = value;
                urlsBdd.insertUrl(url);

                urlsBdd.close();

                Intent intent = new Intent(ReglagesActivity.this, ReglagesActivity.class);
                startActivity(intent);
            }
        });


        urlsBdd.open();
        List maListe =  urlsBdd.getAllUrls();
        urlsBdd.close();

        adapter = new ReglagesAdapter(this, maListe, new ReglagesAdapter.BtnClickListener() {
            @Override
            public void onBtnClick (String url, int position, View v) {
                switch (v.getId()) {
                    //depending on click
                    case R.id.button6:

                        AlertDialog.Builder builder = new AlertDialog.Builder(ReglagesActivity.this);
                        builder.setTitle("IP/URL");

                        nom = url;
                        builder.setMessage("Confirmer l'utilisation de l'ip/url: " + nom);
                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ReglagesActivity.this, MainActivity.class);
                                urlchecked = nom;
                                startActivity(intent);
                            }
                        };
                        builder.setPositiveButton("Oui", listener);
                        builder.setNegativeButton("Non", null);
                        builder.show();
                        break;

                    case R.id.button3:

                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ReglagesActivity.this);
                        builder2.setTitle("Suppression");

                        nom = url;
                        builder2.setMessage("Confirmer la suppression de: " + nom);
                        DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GestionSQLite itemsBdd = new GestionSQLite(ReglagesActivity.this);
                                itemsBdd.open();
                                int id = itemsBdd.getUrlWithNom(nom);
                                itemsBdd.removeUrlWithId(id);
                                itemsBdd.close();
                                Intent intent = new Intent(ReglagesActivity.this, ReglagesActivity.class);
                                startActivity(intent);
                            }
                        };
                        builder2.setPositiveButton("Oui", listener2);
                        builder2.setNegativeButton("Non", null);
                        builder2.show();
                        break;
                }
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
                Intent intent = new Intent(ReglagesActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
