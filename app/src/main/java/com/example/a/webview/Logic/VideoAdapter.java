package com.example.a.webview.Logic;



import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.a.webview.R;
import com.example.a.webview.RESTService.DeleteServer;
import com.example.a.webview.UI.ReglagesActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class VideoAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> mList;
    int mlayout;
    String[] mString;
    int[] mint;
    private Context mContext;
    private LayoutInflater mInflater;


    public VideoAdapter(Context context, ArrayList<HashMap<String, String>> aList, int aLayout,String[] tabString, int[] tabint ) {
        mContext = context;
        mList = aList;
        mInflater = LayoutInflater.from(mContext);
        mlayout = aLayout;
        mString=tabString;
        mint = tabint;//

    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final LinearLayout layoutItem;


        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.titre = (TextView) convertView.findViewById(R.id.titre);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.teleok = (ImageView) convertView.findViewById(R.id.teleok);
            holder.deleteicon = (ImageView) convertView.findViewById(R.id.deleteicon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titre.setText(mList.get(i).get("id"));
        final String sidCamera = holder.titre.getText().toString();
        holder.description.setText(mList.get(i).get("video"));
        final String sVideo = holder.description.getText().toString();
        holder.teleok.setVisibility(View.GONE);
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/VideoSurveillance"+File.separator);
        File existFile = new File(dir, holder.description.getText()+".mp4");

        if(existFile.exists()){

            holder.teleok.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }

        holder.deleteicon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File root = android.os.Environment.getExternalStorageDirectory();
                File dir = new File(root.getAbsolutePath() + "/VideoSurveillance"+File.separator);
                File fileToDelete = new File(dir, holder.description.getText()+".mp4");

                if(fileToDelete.exists()){

                    fileToDelete.delete();
                }
                JSONObject post_form = new JSONObject();
                DeleteServer deleteService = new DeleteServer();
                try {
                    post_form.put("idCamera" , sidCamera);
                    post_form.put("video" , sVideo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_form.length() > 0) {

                    // Vérifie que l'identifiant est dans la bdd et que le mot de passe correspond
                    String Server_Rest_Address="http://"+ReglagesActivity.urlchecked+":3000/camera";
                    AsyncTask deleteReturn = deleteService.execute(String.valueOf(post_form),Server_Rest_Address);
                    Object resultTask = null;
                    try {
                        resultTask = deleteReturn.get();
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
                        int pid = Integer.parseInt(status);
                        Log.i("***id int***",pid+"");
                        // Si delete erronée
                        if(pid == 1){
                            Log.i("***delete failed*** ",message+"");
                        }
                        // Si delete ok
                        if(pid == 0){
                            Log.i("***video deleted*** ",message+"");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        notifyDataSetChanged();

        return convertView;
    }
    private static class ViewHolder {
        public TextView titre;
        public TextView description;
        public ImageView teleok;
        public ImageView deleteicon;
    }


}
