package com.example.a.webview.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.a.webview.RESTService.WebServiceGET;
import com.example.a.webview.R;

/**
 * activité qui permet d'accéder à d'autres activités
 */
public class VuePrincipaleActivity extends AppCompatActivity {

    private TextView switchStatus;
    private Switch mySwitch;
    private TextView httpStatus;
    private TextView labelHttpStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue_principale);

        switchStatus = (TextView) findViewById(R.id.switchStatus);
        labelHttpStatus = (TextView) findViewById(R.id.labelHttpStatus);
        httpStatus = (TextView) findViewById(R.id.httpStatus);
        labelHttpStatus.setText("Status: ");
        httpStatus.setText("PRET");

        mySwitch = (Switch) findViewById(R.id.alarmeSwitch);
        //set the switch to OFF
        mySwitch.setChecked(false);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    // Activation du système de détection
                    String URL = "http://"+ReglagesActivity.urlchecked+":3000/pir";
                    WebServiceGET WebServiceGET = new WebServiceGET();
                    WebServiceGET.execute(URL);
                    //if the connexion is running
                    if (WebServiceGET.httpStatus == 0){

                        httpStatus.setText("/pir "+new WebServiceGET().execute(URL).getStatus()+"");
                    }
                    //if connexion ok
                    else if (WebServiceGET.httpStatus == 200){
                        switchStatus.setTextColor(getResources().getColor(R.color.green));
                        switchStatus.setText("ON");
                        httpStatus.setText("détection activée");
                    }
                    //else we show the httpstatus we got
                    else{
                        httpStatus.setText(WebServiceGET.httpStatus+"");
                    }

                    // if it is un-checked
                }else{
                    // Arrêt du système de détection
                    String URL = "http://"+ReglagesActivity.urlchecked+":3000/pir/stop";
                    WebServiceGET WebServiceGet2 = new WebServiceGET();
                    WebServiceGet2.execute(URL);
                    //if connexion ok
                    if (WebServiceGET.httpStatus == 0){
                        mySwitch.setChecked(false);
                        httpStatus.setText("/pir/stop "+new WebServiceGET().execute(URL).getStatus()+"");
                    }
                    //we show the http status
                    else if (WebServiceGET.httpStatus == 200){
                        switchStatus.setTextColor(getResources().getColor(R.color.red));
                        switchStatus.setText("OFF");
                        httpStatus.setText("détection stop");
                    }
                    else{
                        mySwitch.setChecked(false);
                        httpStatus.setText(WebServiceGet2.httpStatus+"");
                    }
                }

            }
        });

        //check the current state before we display the screen
        if(mySwitch.isChecked()){
            switchStatus.setTextColor(getResources().getColor(R.color.green));
            switchStatus.setText("ON");
        }
        else {
            switchStatus.setTextColor(getResources().getColor(R.color.red));
            switchStatus.setText("OFF");
        }

        // Accès activité camera
        Button bCamera = (Button)findViewById(R.id.bCamera);
        bCamera.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent activityChangeIntent = new Intent(VuePrincipaleActivity.this, CameraActivity.class);
                VuePrincipaleActivity.this.startActivity(activityChangeIntent);
            }
        });
        // Accès activité data
        Button bData = (Button)findViewById(R.id.bData);
        bData.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent activityChangeIntent = new Intent(VuePrincipaleActivity.this, DataActivity.class);
                VuePrincipaleActivity.this.startActivity(activityChangeIntent);
            }
        });
        // Accès activité alarmes
        Button bAlarme = (Button)findViewById(R.id.bAlarme);
        bAlarme.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent activityChangeIntent = new Intent(VuePrincipaleActivity.this, AlarmeActivity.class);
                VuePrincipaleActivity.this.startActivity(activityChangeIntent);
            }
        });
        // Retour à l'identification
        Button bDeconnexion = (Button)findViewById(R.id.bDeconnexion);
        bDeconnexion.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent activityChangeIntent = new Intent(VuePrincipaleActivity.this, MainActivity.class);
                VuePrincipaleActivity.this.startActivity(activityChangeIntent);
            }
        });
    }
}


