package com.example.a.webview.UI;

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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.a.webview.RESTService.WebServiceGET;
import com.example.a.webview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    protected void onPause() {
        super.onPause();

        // Arrêt du serveur de streaming
        String URL = "http://"+ReglagesActivity.urlchecked+":3000/streaming/stop";
        final WebServiceGET WebServiceGET = new WebServiceGET();
        AsyncTask stopStreamingReturn = WebServiceGET.execute(URL);
        Object resultTask = null;
        try {
            resultTask = stopStreamingReturn.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        JSONObject task = null;
        try {
            task = new JSONObject(resultTask.toString());
            String id = task.getString("status");
            String message = task.getString("message");
            int boolDownload = Integer.parseInt(id);
            if (boolDownload == 0) {
                Toast.makeText(CameraActivity.this, "Streaming stopped", Toast.LENGTH_SHORT).show();

            }else if(boolDownload == 1){
                Toast.makeText(CameraActivity.this,"streaming failed, message: "+ message+ " http status: "+ stopStreamingReturn.getStatus() ,Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Barre d'outils
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Activation serveur de streaming
        getStreaming();

        Button refresh = (Button)findViewById(R.id.button4);
        refresh.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Activation serveur de streaming
                getStreaming();
            }
        });
    }

    private void getStreaming(){
        String URL = "http://"+ReglagesActivity.urlchecked+":3000/streaming";
        final WebServiceGET WebServiceGET = new WebServiceGET();
        AsyncTask streamingReturn = WebServiceGET.execute(URL);
        Object resultTask = null;
        try {
            resultTask = streamingReturn.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        JSONObject task = null;
        try {
            task = new JSONObject(resultTask.toString());
            String id = task.getString("status");
            String message = task.getString("message");
            int boolDownload = Integer.parseInt(id);
            if (boolDownload == 0) {
                final WebView webview = (WebView) findViewById(R.id.webview);
                int default_zoom_level = 100;
                webview.setInitialScale(default_zoom_level);
                webview.post(new Runnable() {

                    @Override
                    public void run() {
                        webview.loadUrl("http://"+ReglagesActivity.urlchecked+":8090/");
                    }
                });

            }else if(boolDownload == 1){
                Log.i("***streaming failed***", "message "+message+" http status "+streamingReturn.getStatus());
                Toast.makeText(CameraActivity.this,"streaming failed, message: "+ message+ " http status: "+ streamingReturn.getStatus() ,Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                        Intent activityChangeIntent = new Intent(CameraActivity.this, VuePrincipaleActivity.class);
                        CameraActivity.this.startActivity(activityChangeIntent);

            case R.id.action_video:
                        Intent activityChangeIntent2 = new Intent(CameraActivity.this, VideoActivity.class);
                        CameraActivity.this.startActivity(activityChangeIntent2);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
