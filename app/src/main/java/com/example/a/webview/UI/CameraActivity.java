package com.example.a.webview.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.a.webview.RESTService.WebServiceGET;
import com.example.a.webview.R;

public class CameraActivity extends AppCompatActivity {

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
        String URL = "http://"+ReglagesActivity.urlchecked+":3000/streaming";
        final WebServiceGET WebServiceGET = new WebServiceGET();
        WebServiceGET.execute(URL);

        if (WebServiceGET.httpStatus == 200) {
            final WebView webview = (WebView) findViewById(R.id.webview);
            int default_zoom_level = 100;
            webview.setInitialScale(default_zoom_level);
            webview.post(new Runnable() {

                @Override
                public void run() {
                    int width = webview.getWidth();
                    int height = webview.getHeight();
                    webview.loadUrl("http://"+ReglagesActivity.urlchecked+":8090/");
                }
            });
        }
        else {
            Intent activityChangeIntent = new Intent(CameraActivity.this, CameraActivity.class);
            CameraActivity.this.startActivity(activityChangeIntent);
        }

        Button rec = (Button)findViewById(R.id.button4);
        rec.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String URL = "http://"+ReglagesActivity.urlchecked+":3000/camera/rec";
                WebServiceGET WebServiceGET = new WebServiceGET();
                WebServiceGET.execute(URL);
                Toast.makeText(CameraActivity.this,"L'enregistrement d'une vidéo de 20s a démarré",Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // Arrêt du serveur de streaming
                String URL = "http://"+ReglagesActivity.urlchecked+":3000/streaming/stop";
                WebServiceGET WebServiceGET = new WebServiceGET();
                WebServiceGET.execute(URL);

                if (WebServiceGET.httpStatus == 200) {
                    Intent activityChangeIntent = new Intent(CameraActivity.this, VuePrincipaleActivity.class);
                    CameraActivity.this.startActivity(activityChangeIntent);
                }
                else{
                    Toast.makeText(CameraActivity.this,"Streaming always running, do it again",Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.action_video:
                Intent activityChangeIntent = new Intent(CameraActivity.this, VideoActivity.class);
                CameraActivity.this.startActivity(activityChangeIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
