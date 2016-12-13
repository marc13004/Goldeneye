package com.example.a.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        boolean stream = false;

        String URL = "http://10.111.61.96:8081/streaming";
        WebServiceGET WebServiceGET = new WebServiceGET();
        WebServiceGET.execute(URL);

        if (WebServiceGET.httpStatus == 200) {
            stream = true;
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

}
