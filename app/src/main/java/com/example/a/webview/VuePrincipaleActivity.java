package com.example.a.webview;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

/**
 * classe qui permet d'acceder a d'autres vues
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
        labelHttpStatus.setText("HTTP Status: ");
        httpStatus.setText("connectez-vous");

        mySwitch = (Switch) findViewById(R.id.alarmeSwitch);
        //set the switch to OFF
        mySwitch.setChecked(false);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    String URL = "http://10.111.61.96:8082/pir";
                    WebServiceGET WebServiceGET = new WebServiceGET();
                    WebServiceGET.execute(URL);
                    //if the connexion is running
                    if (WebServiceGET.httpStatus == 0){

                        httpStatus.setText("/pir"+new WebServiceGET().execute(URL).getStatus()+"");
                    }
                    //if connexion ok
                    else if (WebServiceGET.httpStatus == 200){
                        switchStatus.setText("ON");
                        httpStatus.setText("connexion to /pir ok");
                    }
                    //else we show the httpstatus we got
                    else{
                        httpStatus.setText(WebServiceGET.httpStatus+"");
                    }
                    // if it is un-checked
                }else{
                    WebServiceGET WebServiceGet2 = new WebServiceGET();

                    String URL = "http://10.111.61.96:8082/pir/stop";
                    WebServiceGet2.execute(URL);
                    //if connexion ok
                    if (WebServiceGET.httpStatus == 0){
                        mySwitch.setChecked(false);
                        httpStatus.setText("/pir/stop"+new WebServiceGET().execute(URL).getStatus()+"");
                    }
                    //we show the http status
                    else if (WebServiceGET.httpStatus == 200){

                        switchStatus.setText("OFF");
                        httpStatus.setText("connexion to pir/stop ok");
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
            switchStatus.setText("ON");
        }
        else {
            switchStatus.setText("OFF");
        }


        Button bCamera = (Button)findViewById(R.id.bCamera);
        bCamera.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent activityChangeIntent = new Intent(VuePrincipaleActivity.this, CameraActivity.class);
                VuePrincipaleActivity.this.startActivity(activityChangeIntent);
            }
        });
        Button bData = (Button)findViewById(R.id.bData);
        bData.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent activityChangeIntent = new Intent(VuePrincipaleActivity.this, DataActivity.class);
                VuePrincipaleActivity.this.startActivity(activityChangeIntent);
            }
        });
        Button bAlarme = (Button)findViewById(R.id.bAlarme);
        bAlarme.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent activityChangeIntent = new Intent(VuePrincipaleActivity.this, AlarmeActivity.class);
                VuePrincipaleActivity.this.startActivity(activityChangeIntent);
            }
        });
    }
}


