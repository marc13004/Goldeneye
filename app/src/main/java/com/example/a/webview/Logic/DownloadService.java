package com.example.a.webview.Logic;

/**
 * Created by 34011-82-08 on 19/12/2016.
 */
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.a.webview.R;
import com.example.a.webview.RESTService.RetrofitInterface;
import com.example.a.webview.UI.ReglagesActivity;
import com.example.a.webview.UI.VideoActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Retrofit;

public class DownloadService extends IntentService {

    public DownloadService() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;
    private Download download;

    @Override
    protected void onHandleIntent(Intent intent) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());


        initDownload();

    }

    private void initDownload(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ ReglagesActivity.urlchecked+":8086")
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Log.i("***inside service***", "Started");
        Call<ResponseBody> request = retrofitInterface.downloadFile();
        try {

            downloadFile(request.execute().body());

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/VideoSurveillance"+File.separator);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        new File(dir + VideoActivity.sVideo+".mp4").delete();
        File outputFile = new File(dir, VideoActivity.sVideo+".mp4");
        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    private void sendNotification(Download download){

        sendIntent(download);
        notificationBuilder.setProgress(10,download.getProgress(),false);
        notificationBuilder.setContentText("Downloading file "+ download.getCurrentFileSize() +"/"+totalFileSize +" MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download){

        Intent intent = new Intent(VideoActivity.MESSAGE_PROGRESS);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(){

        download = new Download();
        download.setProgress(100);
        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}