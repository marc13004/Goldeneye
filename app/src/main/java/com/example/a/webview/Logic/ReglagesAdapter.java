package com.example.a.webview.Logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;
import java.util.List;


public class ReglagesAdapter extends BaseAdapter {

    private List mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private BtnClickListener mClickListener = null;

    public ReglagesAdapter(Context context, List aList, BtnClickListener listener) {
        mContext = context;
        mList = aList;
        mInflater = LayoutInflater.from(mContext);
        mClickListener = listener;
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
        final LinearLayout layoutItem;

        if (convertView == null) {
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.adapter_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView tv = (TextView)layoutItem.findViewById(R.id.textView4);

        tv.setText(mList.get(i).toString());


        Button button = (Button) layoutItem.findViewById(R.id.button6);

        button.setTag(i);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mClickListener != null)
                    mClickListener.onBtnClick(mList.get((Integer) v.getTag()).toString(), (Integer) v.getTag(), v);
            }
        });

        Button delete = (Button) layoutItem.findViewById(R.id.button3);

        delete.setTag(i);

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mClickListener != null)
                    mClickListener.onBtnClick(mList.get((Integer) v.getTag()).toString(), (Integer) v.getTag(), v);
            }
        });

        return layoutItem;
    }

    public interface ReglagesAdapterListener {
    }
    public interface BtnClickListener {
        public abstract void onBtnClick(String url, int position, View v);
    }

    private ArrayList<ReglagesAdapterListener> mListListener = new ArrayList<ReglagesAdapterListener>();

    public void addListener(ReglagesAdapterListener aListener) {
        mListListener.add(aListener);
    }

}
