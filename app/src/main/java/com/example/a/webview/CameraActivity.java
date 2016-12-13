package com.example.a.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        String URL = "http://10.111.61.96:8081/streaming";
        WebServiceGET WebServiceGET = new WebServiceGET();
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
                    webview.loadUrl("http://10.111.61.96:8090/");
                }
            });
        }
        else {
            Intent activityChangeIntent = new Intent(CameraActivity.this, VuePrincipaleActivity.class);
            CameraActivity.this.startActivity(activityChangeIntent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String URL = "http://10.111.61.96:8081/streaming/stop";
                WebServiceGET WebServiceGET = new WebServiceGET();
                WebServiceGET.execute(URL);
                if (WebServiceGET.httpStatus == 200) {
                    this.finish();
                }
                else{
                    Toast.makeText(CameraActivity.this,"Streaming always running, do it again",Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
