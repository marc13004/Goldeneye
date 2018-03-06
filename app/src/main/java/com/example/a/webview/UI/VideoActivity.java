package com.example.a.webview.UI;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.webview.Logic.Download;
import com.example.a.webview.Logic.DownloadService;
import com.example.a.webview.RESTService.HttpHandler;
import com.example.a.webview.Logic.VideoAdapter;
import com.example.a.webview.R;
import com.example.a.webview.RESTService.PostServer;
import com.example.a.webview.RESTService.WebServiceGET;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class VideoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SeekBar.OnSeekBarChangeListener {

    public static String sVideo;
    public static ArrayList<HashMap<String, String>> objetList;
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static long filesize;
    public static int filesizemb;
    ProgressBar progressBar, progressBar2;
    CircularProgressBar circularProgressBar;
    SwipeRefreshLayout swipeAndRefresh;
    TextView mProgressText, recsecond;
    public static Button bDownload;
    ImageView brecording;
    SeekBar seekBar;
    ListView LView;
    int timeRec;
    CountDownTimer timer;
    Boolean stopRec, wait = false;
    public static Boolean pending = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        stopRec = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Barre d'outils
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        objetList = new ArrayList<>();
        new GetVideo().execute();

        circularProgressBar = (CircularProgressBar)findViewById(R.id.progress);
        circularProgressBar.setColor(circularProgressBar.getColor());
        circularProgressBar.setBackgroundColor(circularProgressBar.getBackgroundColor());
        circularProgressBar.setProgressBarWidth(16);
        circularProgressBar.setBackgroundProgressBarWidth(8);
        mProgressText = (TextView) findViewById(R.id.progress_text);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar2.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        bDownload = (Button) findViewById(R.id.btn_download);
        brecording = (ImageView) findViewById(R.id.brecording);

        recsecond = (TextView) findViewById(R.id.recsecond);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(59);
        seekBar.setOnSeekBarChangeListener(this);

        LView = (ListView) findViewById(R.id.LView);

        swipeAndRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeAndRefresh.setOnRefreshListener(this);

        bRecordingListener();
        registerReceiver();
        openPostVideo();

    }

    @Override
    public void onPause() {
        super.onPause();
        startDownload();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        int min = 1;
        int progres = min + progress;
        String minutes = String.valueOf(progres);
        recsecond.setText(minutes);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        recsecond.setTextColor(Color.RED);
        recsecond.setTextSize(18);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        recsecond.setTextColor(getResources().getColor(R.color.green));
        recsecond.setTextSize(14);
        int mess = seekBar.getProgress()+1;
        if (mess==1){
            Toast.makeText(VideoActivity.this,"La video durera au maximum 1 minute",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(VideoActivity.this,"La video durera au maximum "+ String.valueOf(mess) +" minutes",Toast.LENGTH_SHORT).show();
        }

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
                    openPostVideo();
                    bDownload.setBackgroundColor(Color.WHITE);
                    bDownload.setText("Select a video");


                } else {
                    mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),filesizemb));
                    bDownload.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            bDownload.setText("Wait");
                            Toast.makeText(VideoActivity.this, "Download in progress please wait", Toast.LENGTH_LONG).show();

                        }
                    });
                    LView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        }
                    });

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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                for (int i = 0; i < LView.getChildCount(); i++) {
                    View listItem = LView.getChildAt(i);
                    listItem.setBackgroundColor(Color.WHITE);
                }
                view.setBackgroundColor(Color.parseColor("#99FF66"));

                sVideo = objetList.get(position).get("video");
                bDownload.setBackgroundColor(Color.parseColor("#99FF66"));
                bDownload.setText("Confirm");
                bDownload.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        bDownload.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                bDownload.setText("Wait");
                                Toast.makeText(VideoActivity.this, "One video already confirmed ", Toast.LENGTH_LONG).show();

                            }
                        });
                        JSONObject post_dict = new JSONObject();
                        PostServer myDAOPostServerRest = new PostServer();
                        try {
                            post_dict.put("video", sVideo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (post_dict.length() > 0) {
                            String Server_Rest_Address = "http://" + ReglagesActivity.urlchecked + ":3000/video";
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
                                String size = task.getString("size");
                                String sizemb = task.getString("sizemb");
                                try {
                                    double fileSizeDouble = Double.parseDouble(size);
                                    double fileSizeMBDouble = Double.parseDouble(sizemb);
                                    filesizemb = ((int) fileSizeMBDouble);
                                    filesize = ((long) fileSizeDouble);
                                    Log.i("fileSizeDouble", fileSizeDouble + "****" + filesize);
                                    Log.i("fileSizeMBDouble", fileSizeMBDouble + "****" + ((int) fileSizeMBDouble));
                                } catch (NumberFormatException e) {
                                    Log.i("NumberFormatException", "not a number");
                                }

                                int boolDownload = Integer.parseInt(id);
                                if (boolDownload == 1) {
                                    Log.i("***Log status not ok***", boolDownload + "");
                                    Toast.makeText(VideoActivity.this, "Connexion failed", Toast.LENGTH_LONG).show();
                                }
                                if (boolDownload == 0) {
                                    bDownload.setBackgroundColor(Color.parseColor("#CC9933"));
                                    bDownload.setText("Download");

                                    bDownload.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            Log.i("***download***", "onclick");
                                            if (checkPermission()) {
                                                startDownload();
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

    @Override
    public void onRefresh() {
        // create a handler to run after some milli seconds
        // get data
        swipeAndRefresh.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                objetList.clear();

                // call methods for updated data for the list view
                new GetVideo().execute();
                openPostVideo();

                swipeAndRefresh.setRefreshing(false);
            }
        }, 2000);

    }



    public class GetVideo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler httpHandler = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://"+ReglagesActivity.urlchecked+":3000/camera";
            String jsonStr = httpHandler.getRequest(url);


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
    void startProgress()
    {

        timer = new CountDownTimer((timeRec*1000)+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("*** on tick***", "");

                progressBar.setMax(timeRec*1000);
                progressBar.setProgress(progressBar.getProgress()+1000);
            }

            public void onFinish() {
                // brecording.setImageResource(R.drawable.recicon);
                bRecordingListener();
                progressBar.setProgress(0);
            }
        }.start();
    }
    void bRecordingListener(){
        brecording.setImageResource(R.drawable.recicon);
        brecording.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                progressBar2.setVisibility(View.VISIBLE);
                String tRec = (String) recsecond.getText();

                if (!tRec.equals("") && !tRec.contains(".")) {
                    timeRec = (Integer.parseInt(tRec))*60;
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
                    String Server_Rest_Address = "http://"+ReglagesActivity.urlchecked+":3000/camera/rec";
                    AsyncTask recReturn = myDAOPostServerRest.execute(String.valueOf(post_dict), Server_Rest_Address);
                    Object resultTask = null;
                    try {
                        resultTask = recReturn.get();

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
                        String message = task.getString("message");
                        int boolDownload = Integer.parseInt(id);
                        if (boolDownload == 0) {
                            brecording.setImageResource(R.drawable.stoprec);
                            progressBar2.setVisibility(View.GONE);
                            bStopRecordingListener();
                            startProgress();
                            Log.i("***rec ok***","message "+message+" http status: "+recReturn.getStatus());
                            //Toast.makeText(VideoActivity.this,"stop rec ok, message: "+ message+ " http status: "+ stopRecReturn.getStatus() ,Toast.LENGTH_LONG).show();

                        }else if(boolDownload == 1){
                            Log.i("***rec failed***","message "+message+" http status: "+recReturn.getStatus());
                            Toast.makeText(VideoActivity.this,"rec failed, message: "+ message+ " http status: "+ recReturn.getStatus() ,Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    void bStopRecordingListener(){
        brecording.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timer.cancel();
                progressBar2.setVisibility(View.VISIBLE);
                String URL = "http://"+ReglagesActivity.urlchecked+":3000/video/stoprec";
                WebServiceGET serviceGET = new WebServiceGET();
                AsyncTask stopRecReturn = serviceGET.execute(URL);
                Object resultTask = null;
                try {
                    resultTask = stopRecReturn.get();

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
                        progressBar2.setVisibility(View.GONE);
                        progressBar.setProgress(0);
                        bRecordingListener();
                        Log.i("***stop rec ok***","message "+message+" http status: "+stopRecReturn.getStatus());
                        //Toast.makeText(VideoActivity.this,"stop rec ok, message: "+ message+ " http status: "+ stopRecReturn.getStatus() ,Toast.LENGTH_LONG).show();

                    }else if(boolDownload == 1){
                        Log.i("***stop rec failed***", "message "+message+" http status "+stopRecReturn.getStatus());
                        Toast.makeText(VideoActivity.this,"stop rec failed, message: "+ message+ " http status: "+ stopRecReturn.getStatus() ,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Démarrage du serveur de streaming
                String URL = "http://" + ReglagesActivity.urlchecked + ":3000/streaming";
                final WebServiceGET WebServiceGET = new WebServiceGET();
                AsyncTask startStreamingReturn = WebServiceGET.execute(URL);
                Object resultTask = null;
                try {
                    resultTask = startStreamingReturn.get();

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
                        Intent activityChangeIntent = new Intent(VideoActivity.this, CameraActivity.class);
                        VideoActivity.this.startActivity(activityChangeIntent);
                        return true;

                    } else if (boolDownload == 1) {
                        Log.i("stream failed to start", "msg " + message + " http " + startStreamingReturn.getStatus());
                        Toast.makeText(VideoActivity.this, "streaming failed, message: " + message + " http status: " + startStreamingReturn.getStatus(), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
