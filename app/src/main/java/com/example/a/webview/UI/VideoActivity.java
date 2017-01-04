package com.example.a.webview.UI;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.webview.Logic.Download;
import com.example.a.webview.Logic.DownloadService;
import com.example.a.webview.Logic.HttpHandler;
import com.example.a.webview.Logic.VideoAdapter;
import com.example.a.webview.R;
import com.example.a.webview.RESTService.PostServer;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class VideoActivity extends AppCompatActivity {

    public static String sVideo;
    ArrayList<HashMap<String, String>> objetList;
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;
    //ProgressBar mProgressBar;
    CircularProgressBar circularProgressBar;
    TextView mProgressText,tChrono,recsecond;
    Button bDownload,brecording;
    ListView LView;
    ImageView teleok;
    int timeRec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        objetList = new ArrayList<>();
        new GetVideo().execute();

        circularProgressBar = (CircularProgressBar)findViewById(R.id.progress);
        circularProgressBar.setColor(circularProgressBar.getColor());
        circularProgressBar.setBackgroundColor(circularProgressBar.getBackgroundColor());
        circularProgressBar.setProgressBarWidth(16);
        circularProgressBar.setBackgroundProgressBarWidth(8);

        mProgressText = (TextView) findViewById(R.id.progress_text);
        tChrono = (TextView) findViewById(R.id.tChrono);
        bDownload = (Button) findViewById(R.id.btn_download);
        recsecond = (TextView) findViewById(R.id.recsecond);
        brecording = (Button) findViewById(R.id.brecording);
        LView = (ListView) findViewById(R.id.LView);

        brecording.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               String tRec = (String) recsecond.getText();

                if (!tRec.equals("") && !tRec.contains(".")) {
                    timeRec = Integer.parseInt(tRec);
                }else{
                    //todo->toast
                }
                JSONObject post_dict = new JSONObject();
                PostServer myDAOPostServerRest = new PostServer();
                        try {
                            post_dict.put("time", timeRec);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (post_dict.length() > 0) {
                            String Server_Rest_Address = "http://"+ReglagesActivity.urlchecked+":3002/camera/rec";
                            AsyncTask loginReturn = myDAOPostServerRest.execute(String.valueOf(post_dict), Server_Rest_Address);
                            Object resultTask = null;
                            try {
                                resultTask = loginReturn.get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            // ... and parse the String into a JSON Object
                            JSONObject task = null;
                            try {
                                task = new JSONObject(resultTask.toString());
                                String id = task.getString("status");
                                int boolDownload = Integer.parseInt(id);
                                if (boolDownload == 0) {}
                                Log.i("***id int***", id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //myDAOPostServerRest.execute(String.valueOf(post_dict),Server_Rest_Address);
                        }
            }
        });
        registerReceiver();
        //new GetVideo().execute();
        openPostVideo();

    }


    private void startDownload(){

        Intent intent = new Intent(VideoActivity.this,DownloadService.class);
        startService(intent);

    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if(intent.getAction().equals(MESSAGE_PROGRESS)){
                Download download = intent.getParcelableExtra("download");
                circularProgressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){

                    mProgressText.setText("File Download Complete");

                } else {

                    mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                }
            }
        }
    };

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDownload();
                } else {

                    Snackbar.make(findViewById(R.id.coordinatorLayout),"Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }
    public void openPostVideo(){
        ListAdapter adapter = new VideoAdapter(VideoActivity.this, objetList,
                R.layout.list_item, new String[]{"id", "video"},
                new int[]{R.id.titre, R.id.description});
        LView.setAdapter(adapter);
        LView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {

                sVideo = objetList.get(position).get("video");

                bDownload.setBackgroundColor(Color.parseColor("#77BFA3"));
                bDownload.setText("Confirm");
                bDownload.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        bDownload.setText("Wait");
                        JSONObject post_dict = new JSONObject();
                        PostServer myDAOPostServerRest = new PostServer();
                        try {
                            post_dict.put("video", sVideo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (post_dict.length() > 0) {
                            String Server_Rest_Address = "http://"+ReglagesActivity.urlchecked+":8085/video";
                            AsyncTask loginReturn = myDAOPostServerRest.execute(String.valueOf(post_dict), Server_Rest_Address);
                            Object resultTask = null;
                            try {
                                resultTask = loginReturn.get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            // ... and parse the String into a JSON Object
                            JSONObject task = null;
                            try {
                                task = new JSONObject(resultTask.toString());
                                String id = task.getString("status");
                                int boolDownload = Integer.parseInt(id);
                                if (boolDownload == 0) {
                                    bDownload.setBackgroundColor(Color.BLUE);
                                    bDownload.setText("Download");
                                    bDownload.setOnClickListener(new View.OnClickListener()
                                    {
                                        public void onClick(View v)
                                        {
                                            Log.i("***download***", "onclick");
                                            if(checkPermission()){

                                                startDownload();
                                                Log.i("***download***", "Started");
                                                new CountDownTimer(15000, 1000) {

                                                    public void onTick(long millisUntilFinished) {
                                                        tChrono.setText("retry in "+(millisUntilFinished / 1000));
                                                        bDownload.setOnClickListener(new View.OnClickListener() {
                                                            public void onClick(View v) {
                                                                bDownload.setText("Wait");
                                                            }
                                                        });
                                                    }

                                                    public void onFinish() {
                                                        tChrono.setText("");
                                                        bDownload.setBackgroundColor(Color.BLUE);
                                                        bDownload.setText("Select a Video");
                                                        bDownload.setOnClickListener(new View.OnClickListener() {
                                                            public void onClick(View v) {
                                                                VideoActivity.this.openPostVideo();
                                                            }
                                                        });
                                                    }
                                                }.start();
                                            } else {
                                                requestPermission();
                                            }
                                        }
                                    });
                                }
                                Log.i("***id int***", id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //myDAOPostServerRest.execute(String.valueOf(post_dict),Server_Rest_Address);
                        }
                    }
                });
            }
        });
    }

    class GetVideo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler http = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://"+ReglagesActivity.urlchecked+":3006/camera";
            String jsonStr = http.getRequest(url);


            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray objets = new JSONArray(jsonStr);
                    Log.i("***array***", objets.length() + "");

                    // looping through All Contacts
                    for (int i = 0; i < objets.length(); i++) {
                        JSONObject c = objets.getJSONObject(i);
                        String id = c.getString("idCAMERA");
                        String video = c.getString("video");


                        // tmp hash map for single contact
                        HashMap<String, String> videoMap = new HashMap<>();

                        // adding each child node to HashMap key => value
                        videoMap.put("id", id);
                        videoMap.put("video", video);


                        // adding contact to contact list
                        objetList.add(videoMap);
                    }
                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

}
