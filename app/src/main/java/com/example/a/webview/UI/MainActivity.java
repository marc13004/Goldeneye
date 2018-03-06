package com.example.a.webview.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a.webview.RESTService.PostServer;
import com.example.a.webview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText identifiant;
    EditText password;
    Button bConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Barre d'outils
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Identification
        identifiant = (EditText) findViewById(R.id.identifiant);
        password = (EditText) findViewById(R.id.password);
        bConnect = (Button)findViewById(R.id.bLogin);
        if(ReglagesActivity.urlchecked!=null){
            bConnectListener();
        }
        else{
            bConnectListenerNoURL();
        }
    }

    private void bConnectListenerNoURL(){
        bConnect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Please check URL in settings", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void bConnectListener(){
        bConnect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                JSONObject post_form = new JSONObject();
                PostServer myDAOPostServerRest = new PostServer();
                try {
                    String sidentifiant = identifiant.getText().toString();
                    String spassword = password.getText().toString();

                    post_form.put("identifiant" , sidentifiant);
                    post_form.put("pwd", spassword);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_form.length() > 0) {

                    // Vérifie que l'identifiant est dans la bdd et que le mot de passe correspond
                    String Server_Rest_Address="http://"+ReglagesActivity.urlchecked+":3000/users/login";
                    AsyncTask loginReturn = myDAOPostServerRest.execute(String.valueOf(post_form),Server_Rest_Address);
                    Object resultTask = null;
                    try {
                        resultTask = loginReturn.get();
                        // Log.i("***resultTask***",resultTask.toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    JSONObject task = null;
                    try {
                        task = new JSONObject(resultTask.toString());
                        String status = task.getString("status");
                        String message = task.getString("message");
                        int statusId = Integer.parseInt(status);
                        Log.i("***id int***",statusId+"");
                        // Si identification erronée
                        if(statusId == 1){
                            Log.i("***Log status not ok***", statusId+"");
                            Toast.makeText(MainActivity.this,"login failed, message: "+ message+ " http status: "+ loginReturn.getStatus() ,Toast.LENGTH_LONG).show();
                        }
                        else if(statusId == 2){
                            Log.i("***Log status not ok***", statusId+"");
                            Toast.makeText(MainActivity.this,"login failed, message: "+ message+ " http status: "+ loginReturn.getStatus() ,Toast.LENGTH_LONG).show();
                        }
                        // Si identification ok
                        else if(statusId == 0){
                            Log.i("***Log status ok***", statusId+"");
                            Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                            Intent activityChangeIntent = new Intent(MainActivity.this, VuePrincipaleActivity.class);
                            MainActivity.this.startActivity(activityChangeIntent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent activityChangeIntent = new Intent(MainActivity.this, ReglagesActivity.class);
                MainActivity.this.startActivity(activityChangeIntent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}