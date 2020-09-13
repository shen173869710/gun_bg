package com.auto.di.guan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.auto.di.guan.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MyListAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private int selectedPosition = -1;// 选中的位置
    private Context context;
    private String [] datas;
    public MyListAdapter(Context context, String [] datas) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public Object getItem(int position) {
        return datas[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder.item_text = (TextView) convertView.findViewById(R.id.list_item_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_text.setText(datas[position]);
        if (selectedPosition == position) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.black));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        return convertView;
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    class ViewHolder {
        public TextView item_text;
    }
}
