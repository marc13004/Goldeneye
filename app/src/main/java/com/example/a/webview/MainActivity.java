package com.example.a.webview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{
    EditText identifiant;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        identifiant = (EditText) findViewById(R.id.identifiant);
        password = (EditText) findViewById(R.id.password);
        Button bConnect = (Button)findViewById(R.id.bLogin);
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

                    String Server_Rest_Address="http://"+ReglagesActivity.urlchecked+":3000/users/login";
                    AsyncTask loginReturn = myDAOPostServerRest.execute(String.valueOf(post_form),Server_Rest_Address);
                    Object resultTask = null;
                    try {
                        resultTask = loginReturn.get();
                        Log.i("***resultTask***",resultTask.toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    // ... and parse the String into a JSON Object
                    JSONObject task = null;
                    try {
                        task = new JSONObject(resultTask.toString());
                        String status = task.getString("status");
                        int pid = Integer.parseInt(status);
                        Log.i("***id int***",pid+"");
                        if(pid == 1){
                            Log.i("***Log status not ok***", pid+"");
                            Toast.makeText(MainActivity.this,"Connexion failed",Toast.LENGTH_LONG).show();
                        }
                        if(pid == 0){
                            Log.i("***Log status ok***", pid+"");
                            Toast.makeText(MainActivity.this,"Connexion success",Toast.LENGTH_LONG).show();
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