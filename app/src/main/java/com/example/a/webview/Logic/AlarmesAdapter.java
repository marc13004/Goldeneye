package com.example.a.webview.Logic;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a.webview.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter pour la listView de l'activité alarmes
 */
public class AlarmesAdapter extends BaseAdapter {

    private List mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private AlarmesAdapter.BtnClickListener mClickListener = null;

    public AlarmesAdapter(Context context, List aList, AlarmesAdapter.BtnClickListener listener) {
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
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.adapter_alarmes, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView tv = (TextView)layoutItem.findViewById(R.id.textView4);

        tv.setText(mList.get(i).toString());


        Button button = (Button) layoutItem.findViewById(R.id.play);

        button.setTag(i);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mClickListener != null)
                    mClickListener.onBtnClick(mList.get((Integer) v.getTag()).toString(), (Integer) v.getTag(), v);
            }
        });

        return layoutItem;
    }

    public interface AlarmesAdapterListener {
    }
    public interface BtnClickListener {
        void onBtnClick(String son, int position, View v);
    }

    private ArrayList<AlarmesAdapter.AlarmesAdapterListener> mListListener = new ArrayList<AlarmesAdapter.AlarmesAdapterListener>();

    public void addListener(AlarmesAdapter.AlarmesAdapterListener aListener) {
        mListListener.add(aListener);
    }

}