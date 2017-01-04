package com.example.a.webview.Logic;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a.webview.R;
import com.example.a.webview.UI.ReglagesActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
        ViewHolder holder;
        final LinearLayout layoutItem;


        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.titre = (TextView) convertView.findViewById(R.id.titre);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.teleok = (ImageView) convertView.findViewById(R.id.teleok);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titre.setText(mList.get(i).get("id"));
        holder.description.setText(mList.get(i).get("video"));
        holder.teleok.setVisibility(View.GONE);
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/VideoSurveillance"+File.separator);
        File existFile = new File(dir, holder.description.getText()+".mp4");
        String Server_Rest_Address = "http://"+ ReglagesActivity.urlchecked+":3002/camera/rec";

        if(existFile.exists()){

            holder.teleok.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }
        notifyDataSetChanged();


        return convertView;
    }
    private static class ViewHolder {
        public TextView titre;
        public TextView description;
        public ImageView teleok;
    }


}
